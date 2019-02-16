package org.kiomnd2;

import org.kiomnd2.java.*;

import static org.kiomnd2.FakeAuctionServer.XMPP_HOSTNAME;
import static org.kiomnd2.java.SniperSnapshot.SniperState.JOINING;
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

        driver = new AuctionSniperDriver(1000);
        driver.hasTitle(MainWindow.APPLICATION_TITLE);
        driver.hasColumnTitles();
        for ( FakeAuctionServer auction : auctions){
            final String itemId = auction.getItemId();
            driver.startBiddingFor(itemId);
            driver.showsSniperStatus(itemId,0,0,textFor(JOINING));
        }


    }

    private void startSniper() throws Exception {
        Main.main(FakeAuctionServer.XMPP_HOSTNAME,SNIPER_ID,SNIPER_PASSWORD);
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
        driver.showsSniperStatus(auction.getItemId(),lastPrice,lastBid, Main.STATUS_BIDDING);
    }
    public void hasShownSniperIsWinning(FakeAuctionServer auction, int winningBid) {
        driver.showsSniperStatus(auction.getItemId(), winningBid,winningBid, Main.STATUS_WINNING);
    }

    public void showsSniperHasWonAuction(FakeAuctionServer auction,int lastPrice) {
        driver.showsSniperStatus(auction.getItemId() , lastPrice, lastPrice, Main.STATUS_WON);
    }

    public void showsSniperHasLostAuction() {
//        driver.showsSniperStatus(Main.STATUS_LOST); //STATUS_LOST
        driver.showsSniperStatus(Main.STATUS_LOST);
    }


    public void stop() {
        if(driver != null){
            driver.dispose();
        }

    }
}
