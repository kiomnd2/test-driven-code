package org.kiomnd2;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JLabelDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;
import org.hamcrest.CoreMatchers;
import org.kiomnd2.java.Main;


public class AuctionSniperDriver extends JFrameDriver {
    public AuctionSniperDriver(int timeoutMills) {
        super(new GesturePerformer(),
                JFrameDriver.topLevelFrame( //최상위 프레임 가져옴
                        named(Main.MAIN_WINDOW_NAME), //org.kiomnd2.java.Main.MAIN_WINDOW_NAME
                        showingOnScreen()),
                        new AWTEventQueueProber(timeoutMills, 100));
    }

    public void showsSniperStatus(String statusText) {
        new JLabelDriver(
                this, named(Main.SNIPER_STATUS_NAME)).hasText(CoreMatchers.equalTo(statusText)); //org.kiomnd2.java.Main.SNIPER_STATUS_NAME
    }

}
