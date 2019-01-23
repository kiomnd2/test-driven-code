package org.kiomnd2.java;

import javax.swing.*;

public class Main {
    public final static String MAIN_WINDOW_NAME ="window";
    public static final String SNIPER_STATUS_NAME = "status-label";

    public static final String STATUS_JOINING = "Joining";
    public static final String STATUS_LOST = "Lost";
    public static final String STATUS_BIDDING = "Bidding";

    private MainWindow ui;
    public Main() throws Exception{
        startUserInterface();
    }

    public static void main(String... arg) throws Exception {
        Main main = new Main();
    }

    private void startUserInterface() throws Exception{
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                ui = new MainWindow();
            }
        });

    }

    public class MainWindow extends JFrame {
        public MainWindow() {
            super("Action Sniper");
            setName(MAIN_WINDOW_NAME);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
        }
    }
}
