package org.kiomnd2.java;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class Main{
    private  final SnipersTableModel snipers = new SnipersTableModel();
    private MainWindow ui;

    public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
    public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;";

    private static final int ARG_HOSTNAME = 0 ;
    private static final int ARG_USERNAME = 1 ;
    private static final int ARG_PASSWORD = 2 ;
    private static final int ARG_ITEM_ID = 3;

    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;



    public final static String MAIN_WINDOW_NAME ="window";
    public static final String SNIPER_STATUS_NAME = "status-label";
    public static final String SNIPER_TABLE_NAME = "Auction Sniper";


    public static final String STATUS_JOINING = "Joining";
    public static final String STATUS_LOST = "Lost";
    public static final String STATUS_BIDDING = "Bidding";
    public static final String STATUS_WINNING = "Winning";

    public static final String STATUS_WON = "Won";


    @SuppressWarnings("unused") private ArrayList<Auction> notToBeGCd = new ArrayList<>();

    public Main() throws Exception{
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                ui = new MainWindow(snipers);
            }
        });
    }

    public static void main(String... args) throws Exception {
        Main main = new Main();
        XMPPConnection connection = connection(args[ARG_HOSTNAME],args[ARG_USERNAME],args[ARG_PASSWORD]);
        main.disconnectWhenUICloses(connection);
        main.addUserRequestListenerFor(connection);

     }



    private void disconnectWhenUICloses(final XMPPConnection connection) {
        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                connection.disconnect();
            }
        });

    }

    private void addUserRequestListenerFor(final XMPPConnection connection) {
        ui.addUserRequestListener(new UserRequestListener() {
            @Override
            public void joinAuction(String itemId) {
                snipers.addSniper(SniperSnapshot.joining(itemId));
                Auction auction = new XMPPAuction(connection,itemId);
                notToBeGCd.add(auction);





                /*
                snipers.addSniper(SniperSnapshot.joining(itemId));
                Chat chat = connection.getChatManager()
                        .createChat(auctionId(itemId, connection),null);
                Announcer<AuctionEventListener> auctionEventListeners = Announcer.to(AuctionEventListener.class);
                chat.addMessageListener(new AuctionMessageTranslator(
                        connection.getUser(),
                        auctauction.addAuctionEvionEventListeners.announce()
                ));
                notToBeGCd.add(chat);
                Auction auction = new XMPPAuction(chat);
                auctionEventListeners.addListener(
                        new AuctionSniper(itemId, auction,
                                new SwingThreadSniperListener(snipers))
                );
                auction.join();*/
            }
        });
    }

    private static XMPPConnection connection(String hostname, String username, String password) throws XMPPException {
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE);
        return connection;
    }

    private static String auctionId(String itemId, XMPPConnection connection){
        return String.format(AUCTION_ID_FORMAT, itemId,connection.getServiceName());
    }

    // 중첩 클래스
    public static class XMPPAuction implements Auction {
        private final Chat chat;
        private final Announcer<AuctionEventListener> auctionEventListeners= Announcer.to(AuctionEventListener.class);


        public XMPPAuction(XMPPConnection connection, String itemId) {
            chat = connection.getChatManager().createChat(
                    auctionId(itemId,connection),
                    new AuctionMessageTranslator(connection.getUser(),
                            auctionEventListeners.announce())
            );
        }
        private static String auctionId(String itemId, XMPPConnection connection) {
            return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
        }
        public XMPPAuction(Chat chat) {
            this.chat = chat;
        }

        public void bid(int amount) {
            sendMessage(String.format(BID_COMMAND_FORMAT,amount));
        }

        public void join(){
            sendMessage(JOIN_COMMAND_FORMAT);
        }


        private void sendMessage(final String message) {
            try{
                chat.sendMessage(message);
            } catch(XMPPException e) {
                e.printStackTrace();
            }
        }
    }
}
