package org.kiomnd2.java;


import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;


public class MainWindow extends JFrame {
    static public final String SNIPERS_TABLE_NAME = "snipers table";
    static public final String APPLICATION_TITLE = "Auction Sniper";
    private SnipersTableModel snipers;


    private final JLabel sniperStatus = createLabel(Main.STATUS_JOINING);
    public MainWindow(SnipersTableModel snipers){
        super(APPLICATION_TITLE);
        setName(Main.MAIN_WINDOW_NAME);
        this.snipers = snipers;
        fillContentPane(makeSniperTable());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void fillContentPane(JTable snipersTable) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
    }

    private JTable makeSniperTable() {
        final JTable snipersTable = new JTable(snipers);
        snipersTable.setName("SNIPER Table");
        return snipersTable;
    }


    private static JLabel createLabel(String initialText) {
        JLabel result = new JLabel(initialText);
        result.setName(Main.SNIPER_STATUS_NAME);
        result.setBorder(new LineBorder(Color.BLACK));
        return result;
    }


}