package org.kiomnd2.unit;

import com.objogate.wl.swing.probe.ValueMatcherProbe;
import org.junit.Test;
import org.kiomnd2.AuctionSniperDriver;
import org.kiomnd2.java.*;

import static org.hamcrest.Matchers.equalTo;

public class MainWindowTest {

    static {
        System.setProperty("com.objogate.wl.keyboard", "GB");
    }

    private final SnipersTableModel tableModel = new SnipersTableModel();
    private final SniperPortfolio portfolio = new SniperPortfolio();
    private final MainWindow mainWindow = new MainWindow(portfolio);
    private final AuctionSniperDriver driver = new AuctionSniperDriver(100);

    @Test
    public void makesUserRequestWhenJoinButtonClicked() {
        final ValueMatcherProbe<Item> itemProbe = new ValueMatcherProbe<Item>(equalTo(new Item("an-item-id",789)),"item request");

        mainWindow.addUserRequestListener(
                new UserRequestListener() {
                    public void joinAuction(Item item) {
                        itemProbe.setReceivedValue(item);
                    }
                }
        );
        driver.startBiddingFor("an-item-id", 789);
        driver.check(itemProbe);
    }
}
