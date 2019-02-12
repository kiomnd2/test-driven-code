package org.kiomnd2;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JTableDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;
import org.hamcrest.CoreMatchers;
import org.kiomnd2.java.Main;

import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching;
import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;
import static java.lang.String.valueOf;


public class AuctionSniperDriver extends JFrameDriver {
    public AuctionSniperDriver(int timeoutMills) {
        super(new GesturePerformer(),
                JFrameDriver.topLevelFrame( //최상위 프레임 가져옴
                        named(Main.MAIN_WINDOW_NAME), //org.kiomnd2.java.Main.MAIN_WINDOW_NAME
                        showingOnScreen()),
                        new AWTEventQueueProber(timeoutMills, 100));
    }

    public void showsSniperStatus(String statusText) {
        new JTableDriver(this).hasCell(withLabelText(CoreMatchers.equalTo(statusText)));
    }


    public void showsSniperStatus(String itemId, int lastPrice, int lastBid, String statusText) {
        JTableDriver table = new JTableDriver(this);
        table.hasRow(
                matching(withLabelText( itemId),
                        withLabelText(valueOf(lastPrice)),
                        withLabelText(valueOf(lastBid)),
                        withLabelText(statusText))
        );

    }

}
