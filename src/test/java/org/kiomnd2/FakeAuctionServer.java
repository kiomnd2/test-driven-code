package org.kiomnd2;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.kiomnd2.java.XMPPAuctionHouse;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.kiomnd2.java.XMPPAuction.JOIN_COMMAND_FORMAT;

public class FakeAuctionServer {
    private final SingleMessageListener messageListener = new SingleMessageListener();

    public static final String XMPP_HOSTNAME = "antop.org";
    public static final String AUCTION_PASSWORD = "auction";

    private final String itemId;
    private final XMPPConnection connection;
    private Chat currentChat;

    public FakeAuctionServer(String itemId){
        this.itemId = itemId;
        this.connection = new XMPPConnection(XMPP_HOSTNAME);
    }

    private void receivesAMessageMatching(String sniperId, Matcher<? super String> messageMatcher) throws InterruptedException{
        messageListener.receivesAMessage(messageMatcher);
        assertThat(currentChat.getParticipant(), CoreMatchers.equalTo(sniperId));
    }


    public void startSellingItem() throws XMPPException {
        connection.connect();
        connection.login(String.format(XMPPAuctionHouse.ITEM_ID_AS_LOGIN, getItemId()),AUCTION_PASSWORD, XMPPAuctionHouse.AUCTION_RESOURCE);
        connection.getChatManager().addChatListener(
                new ChatManagerListener() {
                    @Override
                    public void chatCreated(Chat chat, boolean b) {
                        currentChat = chat;
                        chat.addMessageListener(messageListener);
                    }
                }
        );
    }
    public void reportPrice(int price, int increment, String bidder) throws XMPPException{
        currentChat.sendMessage(String.format("SOLVersion: 1.1; Event:PRICE; CurrentPrice:%d; Increment: %d; Bidder :%s;",price,increment,bidder ));
    }

    public void hasReceivedJoinRequestFrom(String sniperId) throws InterruptedException {
        receivesAMessageMatching(sniperId, equalTo(JOIN_COMMAND_FORMAT)); //Main.JOIN_COMMNAND_FORMAT
    }

    public void hasReceivedJoinRequestFromSniper() throws InterruptedException {
        messageListener.receivesAMessage(is(CoreMatchers.anything()));
    }

    public void hasReceivedBid(int bid, String sniperId) throws InterruptedException{
        assertThat(currentChat.getParticipant(), CoreMatchers.equalTo(sniperId));
        messageListener.receivesAMessage(
                CoreMatchers.equalTo(String.format("SOLVersion: 1.1; Command: BID; Price: %d;", bid))
        );
    }

    public void announceClosed() throws XMPPException {
        currentChat.sendMessage("SOLVersion: 1.1; Event:CLOSE");
    }

    public void stop() {
        connection.disconnect();
    }

    public String getItemId(){
        return itemId;
    }


    public class SingleMessageListener implements MessageListener {
        private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(1);

        public void processMessage(Chat chat, Message message) {
            messages.add(message);
        }

        public void receivesAMessage(Matcher<? super String> messageMatcher) throws InterruptedException {
            final Message message = messages.poll(5, TimeUnit.SECONDS);
            assertThat(message, hasProperty("body",messageMatcher));
        }

    }
}
