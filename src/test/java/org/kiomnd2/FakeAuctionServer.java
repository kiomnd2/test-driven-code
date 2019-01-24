package org.kiomnd2;

import org.hamcrest.CoreMatchers;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.kiomnd2.java.Main;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class FakeAuctionServer {
    private final SingleMessageListener messageListener = new SingleMessageListener();

    public static final String XMPP_HOSTNAME = "openfie.antop.org";
    public static final String AUCTION_PASSWORD = "auction";

    private final String itemId;
    private final XMPPConnection connection;
    private Chat currentChat;

    public FakeAuctionServer(String itemId){
        this.itemId = itemId;
        this.connection = new XMPPConnection(XMPP_HOSTNAME);
    }


    public void startSellingItem() throws XMPPException {
        connection.connect();
        connection.login(String.format(Main.ITEM_ID_AS_LOGIN,itemId),AUCTION_PASSWORD, Main.AUCTION_RESOURCE);
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

    public void hasReceivedJoinRequestFromSniper() throws InterruptedException {
        messageListener.receivesAMessage();
    }

    public void announceClosed() throws XMPPException {
        currentChat.sendMessage(new Message());
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

        public void receivesAMessage() throws InterruptedException {
            assertThat("Message",messages.poll(5, TimeUnit.SECONDS), is(CoreMatchers.notNullValue()));
        }

    }
}
