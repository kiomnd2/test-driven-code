package org.kiomnd2.unit;

import com.objogate.wl.swing.probe.ValueMatcherProbe;
import org.junit.Test;
import org.kiomnd2.AuctionSniperDriver;
import org.kiomnd2.java.MainWindow;
import org.kiomnd2.java.SnipersTableModel;
import org.kiomnd2.java.UserRequestListener;

import static org.hamcrest.Matchers.equalTo;

public class MainWindowTest {

    static {
        System.setProperty("com.objogate.wl.keyboard", "GB");
    }

    private final SnipersTableModel tableModel = new SnipersTableModel();
    private final MainWindow mainWindow = new MainWindow(tableModel);
    private final AuctionSniperDriver driver = new AuctionSniperDriver(100);

    @Test
    public void makesUserRequestWhenJoinButtonClicked() {
        final ValueMatcherProbe<String> buttonProbe = new ValueMatcherProbe<String>(equalTo("an-item-id"),"join request");

        mainWindow.addUserRequestListener(
                new UserRequestListener() {
                    public void joinAuction(String itemId) {
                        buttonProbe.setReceivedValue(itemId);
                    }
                }
        );
        driver.startBiddingFor("an-item-id");
        driver.check(buttonProbe);
    }
}
