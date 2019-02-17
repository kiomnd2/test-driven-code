package org.kiomnd2.java;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;


public class XMPPAuctionHouse  implements AuctionHouse{

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


    private final XMPPConnection connection;

    private XMPPAuctionHouse(XMPPConnection connection) {
        this.connection = connection;
    }

    public static XMPPAuctionHouse connect(String hostname, String username, String password) throws XMPPException {
        XMPPConnection connection = connection(hostname, username, password);
        return new XMPPAuctionHouse(connection);
    }
    private static XMPPConnection connection(String hostname, String username, String password) throws XMPPException {
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE);
        return connection;
    }

    public void disconnect() {
        connection.disconnect();
    }

    @Override
    public Auction auctionFor(Item item) {
        return new XMPPAuction(connection, auctionId(item , connection) );
    }

    private String auctionId(Item item, XMPPConnection connection) {
        return String.format(AUCTION_ID_FORMAT,item.getIdentifier() ,connection.getServiceName());
    }
}
