package java;

import com.sun.tools.javac.Main;
/*
    ApplicationRunner - 테스트 러너


 */
public class ApplicationRunner {
    public static final String SNIPER_ID ="sniper";
    public static final String SNIPER_PASSWORD="sniper";

    private ActionSniperDriver driver;

    public void startBiddingIn(final FakeAuctionServer auction)  {
        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try{ // 메인클래스 접근, 후 서버 접근
                    Main.main("localhost", SNIPER_ID, SNIPER_PASSWORD, auction.getId()); //HOSTNAME : localhost

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true); // 데몬세팅
        thread.start();
        driver = new (1000)


    }
}
