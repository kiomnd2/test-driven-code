package org.kiomnd2;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.*;
import com.objogate.wl.swing.gesture.GesturePerformer;
import org.hamcrest.CoreMatchers;
import org.kiomnd2.java.MainWindow;
import org.kiomnd2.java.XMPPAuctionHouse;

import javax.swing.*;
import javax.swing.table.JTableHeader;

import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching;
import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;
import static org.kiomnd2.java.MainWindow.NEW_ITEM_ID_NAME;
import static org.kiomnd2.java.MainWindow.NEW_ITEM_STOP_PRICE_NAME;


public class AuctionSniperDriver extends JFrameDriver {
    public AuctionSniperDriver(int timeoutMills) {
        super(new GesturePerformer(),
                JFrameDriver.topLevelFrame( //최상위 프레임 가져옴
                        named(XMPPAuctionHouse.MAIN_WINDOW_NAME), //org.kiomnd2.java.Main.MAIN_WINDOW_NAME
                        showingOnScreen()),
                        new AWTEventQueueProber(timeoutMills, 100));
    }

    public void startBiddingFor(String itemId,int stopPrice) {
        textField(NEW_ITEM_ID_NAME).replaceAllText(itemId);
        textField(NEW_ITEM_STOP_PRICE_NAME).replaceAllText(String.valueOf(stopPrice));
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

    private JTextFieldDriver textField(String name){
        JTextFieldDriver textField = new JTextFieldDriver(this, JTextField.class, named(name));
        textField.focusWithMouse();
        return textField;
    }



    private JButtonDriver bidButton() {
        return new JButtonDriver(this,JButton.class, named(MainWindow.JOIN_BUTTON_NAME));
    }

}
