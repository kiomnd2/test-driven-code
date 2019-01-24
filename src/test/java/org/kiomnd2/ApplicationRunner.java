package org.kiomnd2;

import org.kiomnd2.java.Main;

/*
    org.kiomnd2.ApplicationRunner - 테스트 러너
 */
public class ApplicationRunner {
    public static final String SNIPER_ID ="sniper";
    public static final String SNIPER_PASSWORD="sniper";

    private AuctionSniperDriver driver;

    public void startBiddingIn(final FakeAuctionServer auction)  {
        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try{ // 메인클래스 접근, 후 서버 접근
                    Main.main(FakeAuctionServer.XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true); // 데몬세팅
        thread.start();
        driver = new AuctionSniperDriver(1000);
        driver.showsSniperStatus(Main.STATUS_JOINING); // STATUS_LOINING
    }

    public void showsSniperHasLostAuction() {
        driver.showsSniperStatus(Main.STATUS_LOST); //STATUS_LOST
    }

    public void stop() {
        if(driver != null){
            driver.dispose();
        }

    }
}
