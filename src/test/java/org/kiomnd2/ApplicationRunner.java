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

    public void startBiddingIn(final FakeAuctionServer... auctions)  {
        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try{ // 메인클래스 접근, 후 서버 접근
                    Main.main(arguments(auctions));
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };

        thread.setDaemon(true); // 데몬세팅
        thread.start();
        driver = new AuctionSniperDriver(1000);
        driver.hasTitle(MainWindow.APPLICATION_TITLE);
        driver.hasColumnTitles();

        for( FakeAuctionServer auction : auctions) {
            driver.showsSniperStatus(auction.getItemId(),0,0, textFor(JOINING));
        }

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
