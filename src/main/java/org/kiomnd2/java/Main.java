package org.kiomnd2.java;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main{
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


    public static final String STATUS_JOINING = "Joining";
    public static final String STATUS_LOST = "Lost";
    public static final String STATUS_BIDDING = "Bidding";
    public static final String STATUS_WINNING = "Winning";
    public static final String STATUS_WON = "Won";


    @SuppressWarnings("unused") private Chat notToBeGCd;

    public Main() throws Exception{
        startUserInterface();
    }

    public static void main(String... arg) throws Exception {
        Main main = new Main();
        main.joinAuction(
                connection(arg[ARG_HOSTNAME],arg[ARG_USERNAME],arg[ARG_PASSWORD]),
                arg[ARG_ITEM_ID]
        );
     }


    private void joinAuction(XMPPConnection connection, String itemId){
        disconnectWhenUICloses(connection);
        final Chat chat = connection.getChatManager().createChat(
                auctionId(itemId, connection),
                null);
        this.notToBeGCd = chat;

        Auction auction = new XMPPAuction(chat) ;

        chat.addMessageListener(
                new AuctionMessageTranslator(connection.getUser(), new AuctionSniper(auction , new SniperStateDisplayer())));
        auction.join();
    }



    private void startUserInterface() throws Exception{
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                ui = new MainWindow();
            }
        });

    }

    private void disconnectWhenUICloses(final XMPPConnection connection) {
        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                connection.disconnect();
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

    public class SniperStateDisplayer implements SniperListener {
        public void sniperBidding() {
            showState(Main.STATUS_BIDDING);
        }
        public void sniperLost() {
            showState(Main.STATUS_LOST);
        }
        public void sniperWinning() {
            showState(Main.STATUS_WINNING);
        }
        public void sniperWon() { showState(Main.STATUS_WON);}

        private void showState(final String status) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ui.showStatus(status);
                }
            });
        }
    }

    // 중첩 클래스
    public static class XMPPAuction implements Auction {
        private final Chat chat;

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
