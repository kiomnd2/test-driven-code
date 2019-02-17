package org.kiomnd2;

import org.kiomnd2.java.*;

import static org.kiomnd2.FakeAuctionServer.XMPP_HOSTNAME;
import static org.kiomnd2.java.SniperSnapshot.SniperState.*;
import static org.kiomnd2.java.SnipersTableModel.textFor;


/*
    org.kiomnd2.ApplicationRunner - 테스트 러너
 */
public class ApplicationRunner {
    public static final String SNIPER_ID ="sniper5";
    public static final String SNIPER_PASSWORD="sniper";
    public static final String SNIPER_XMPP_ID ="sniper5@antop.org/Auction";

    private String itemId;
    private AuctionSniperDriver driver;

    public void startBiddingIn(final FakeAuctionServer... auctions) throws Exception {
        startSniper();
        for ( FakeAuctionServer auction : auctions){
            final String itemId = auction.getItemId();
            driver.startBiddingFor(itemId,Integer.MAX_VALUE);
            driver.showsSniperStatus(itemId,0,0,textFor(JOINING));
        }


    }

    private void startSniper() throws Exception {
        Main.main(FakeAuctionServer.XMPP_HOSTNAME,SNIPER_ID,SNIPER_PASSWORD);
        driver = new AuctionSniperDriver(1000);
        driver.hasTitle(MainWindow.APPLICATION_TITLE);
        driver.hasColumnTitles();
    }

    protected static String[] arguments(FakeAuctionServer... auctions) {
        String[] arguments = new String[auctions.length +3];
        arguments[0] = XMPP_HOSTNAME;
        arguments[1] = SNIPER_ID;
        arguments[2] = SNIPER_PASSWORD;

        for(int i =0; i< auctions.length ; i++) {
            arguments[i+3] = auctions[i].getItemId();
        }
        return arguments;
    }

    public void hasShownSniperIsBidding(FakeAuctionServer auction,int lastPrice, int lastBid) {
        driver.showsSniperStatus(auction.getItemId(),lastPrice,lastBid, SnipersTableModel.textFor(BIDDING));
    }
    public void hasShownSniperIsWinning(FakeAuctionServer auction, int winningBid) {
        driver.showsSniperStatus(auction.getItemId(), winningBid,winningBid,SnipersTableModel.textFor(WINNING));
    }
    public void hasShownSniperIsLosing(FakeAuctionServer auction, int lastPrice, int lastBid) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, SnipersTableModel.textFor(LOSING));
    }

    public void showsSniperHasWonAuction(FakeAuctionServer auction,int lastPrice) {
        driver.showsSniperStatus(auction.getItemId() , lastPrice, lastPrice, SnipersTableModel.textFor(WON));
    }

    public void hasShownSniperHasLostAuction(FakeAuctionServer auction, int lastPrice, int lastBid) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, SnipersTableModel.textFor(LOST));
    }

    public void startBiddingWithStopPrice(FakeAuctionServer auction, int stopPrice) throws Exception {
        startSniper();
        driver.startBiddingFor(auction.getItemId() , stopPrice);
        driver.showsSniperStatus(auction.getItemId(),0,0, SnipersTableModel.textFor(JOINING));
    }




    public void stop() {
        if(driver != null){
            driver.dispose();
        }

    }


}
