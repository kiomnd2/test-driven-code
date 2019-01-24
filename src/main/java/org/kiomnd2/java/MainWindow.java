package org.kiomnd2.java;


import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MainWindow extends JFrame {

    private final JLabel sniperStatus = createLabel(Main.STATUS_JOINING);
    public MainWindow() {
        super("Action Sniper");
        setName(Main.MAIN_WINDOW_NAME);
        add(sniperStatus);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private static JLabel createLabel(String initialText) {
        JLabel result = new JLabel(initialText);
        result.setName(Main.SNIPER_STATUS_NAME);
        result.setBorder(new LineBorder(Color.BLACK));
        return result;
    }

    public void showStatus(String status){
        sniperStatus.setText(status);
    }
}