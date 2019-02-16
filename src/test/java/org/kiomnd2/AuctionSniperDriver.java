package org.kiomnd2;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.*;
import com.objogate.wl.swing.gesture.GesturePerformer;
import org.hamcrest.CoreMatchers;
import org.kiomnd2.java.Main;
import org.kiomnd2.java.MainWindow;

import javax.swing.*;
import javax.swing.table.JTableHeader;

import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching;
import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;


public class AuctionSniperDriver extends JFrameDriver {
    public AuctionSniperDriver(int timeoutMills) {
        super(new GesturePerformer(),
                JFrameDriver.topLevelFrame( //최상위 프레임 가져옴
                        named(Main.MAIN_WINDOW_NAME), //org.kiomnd2.java.Main.MAIN_WINDOW_NAME
                        showingOnScreen()),
                        new AWTEventQueueProber(timeoutMills, 100));
    }

    public void startBiddingFor(String itemId) {
        itemIdField().replaceAllText(itemId);
        bidButton().click();
    }

    public void showsSniperStatus(String statusText) {
        new JTableDriver(
                this).hasCell(withLabelText(CoreMatchers.equalTo(statusText))); //확인
    }

    public void showsSniperStatus(String itemId, int lastPrice, int lastBid, String statusText) {
        JTableDriver table = new JTableDriver(this);
        table.hasRow(matching(
                withLabelText(itemId),
                withLabelText(String.valueOf(lastPrice)),
                withLabelText(String.valueOf(lastBid)),
                withLabelText(statusText)
        ));
    }


    public void hasColumnTitles() {
        JTableHeaderDriver headers = new JTableHeaderDriver(this, JTableHeader.class);
        headers.hasHeaders(matching(withLabelText("Item"),
                withLabelText("Last Price"),
                withLabelText("Last Bid"),
                withLabelText("State")));

    }

    private JTextFieldDriver itemIdField(){
        JTextFieldDriver newItemId = new JTextFieldDriver(this, JTextField.class, named(MainWindow.NEW_ITEM_ID_NAME));
        newItemId.focusWithMouse();
        return newItemId;
    }

    private JButtonDriver bidButton() {
        return new JButtonDriver(this,JButton.class, named(MainWindow.JOIN_BUTTON_NAME));
    }

}
