
import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JLabelDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;
import org.hamcrest.CoreMatchers;


public class AuctionSniperDriver extends JFrameDriver {
    public AuctionSniperDriver(int timeoutMills) {
        super(new GesturePerformer(),
                JFrameDriver.topLevelFrame( //최상위 프레임 가져옴
                        named("mainWindow"), //Main.MAIN_WINDOW_NAME
                        showingOnScreen()),
                        new AWTEventQueueProber(timeoutMills, 100));
    }

    public void showsSniperStatus(String statusText) {
        new JLabelDriver(
                this, named("sniperStatusName")).hasText(CoreMatchers.equalTo(statusText)); //Main.SNIPER_STATUS_NAME
    }

}
