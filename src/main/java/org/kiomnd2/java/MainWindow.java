package org.kiomnd2.java;


import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainWindow extends JFrame {
    private final Announcer<UserRequestListener> userRequests = Announcer.to(UserRequestListener.class);

    static public final String SNIPERS_TABLE_NAME = "snipers table";
    static public final String APPLICATION_TITLE = "Auction Sniper";
    public static final String NEW_ITEM_ID_NAME = "new-item-id-field";
    public static final String JOIN_BUTTON_NAME = "join";
    private SnipersTableModel snipers;
    private final JLabel sniperStatus = createLabel(Main.STATUS_JOINING);

    public MainWindow(SnipersTableModel snipers){
        super(APPLICATION_TITLE);
        setName(Main.MAIN_WINDOW_NAME);
        fillContentPane(makeSniperTable(snipers) ,makeControls());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel makeControls() {
        JPanel controls = new JPanel(new FlowLayout());
        final JTextField itemIdField = new JTextField();
        itemIdField.setColumns(25);
        itemIdField.setName(NEW_ITEM_ID_NAME);
        controls.add(itemIdField);

        JButton joinAuctionButton = new JButton("Join Action");
        joinAuctionButton.setName(JOIN_BUTTON_NAME);
        joinAuctionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userRequests.announce().joinAuction(itemIdField.getText());
            }
        });
        controls.add(joinAuctionButton);

        return controls;
    }

    private void fillContentPane(JTable snipersTable, JPanel panel) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
        contentPane.add(panel,BorderLayout.CENTER);
    }

    private JTable makeSniperTable(SnipersTableModel snipers) {
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


    public void addUserRequestListener(UserRequestListener userRequestListener) {
        userRequests.addListener(userRequestListener);
    }
}