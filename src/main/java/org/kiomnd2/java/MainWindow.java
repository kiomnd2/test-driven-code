package org.kiomnd2.java;


import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;


public class MainWindow extends JFrame {
    private final Announcer<UserRequestListener> userRequests = Announcer.to(UserRequestListener.class);

    static public final String SNIPERS_TABLE_NAME = "snipers table";
    static public final String APPLICATION_TITLE = "Auction Sniper";
    public static final String NEW_ITEM_ID_NAME = "new-item-id-field";
    public static final String NEW_ITEM_STOP_PRICE_NAME = "new-item-stop-price";
    public static final String JOIN_BUTTON_NAME = "join";
    private SnipersTableModel snipers;
    private final JLabel sniperStatus = createLabel(XMPPAuctionHouse.STATUS_JOINING);

    public MainWindow(SniperPortfolio portfolio){
        super(APPLICATION_TITLE);
        setName(XMPPAuctionHouse.MAIN_WINDOW_NAME);
        fillContentPane(makeSniperTable(portfolio) ,makeControls());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel makeControls() {
        JPanel controls = new JPanel();

        final JTextField itemIdField = new JTextField();
        itemIdField.setColumns(10);
        itemIdField.setName(NEW_ITEM_ID_NAME);

        final JFormattedTextField stopPriceField = new JFormattedTextField(NumberFormat.getInstance());
        stopPriceField.setColumns(7);
        stopPriceField.setName(NEW_ITEM_STOP_PRICE_NAME);



        JButton joinAuctionButton = new JButton("Join Action");
        joinAuctionButton.setName(JOIN_BUTTON_NAME);
        joinAuctionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userRequests.announce().joinAuction(new Item(itemId(), stopPrice()));
            }

            private String itemId() {
                return itemIdField.getText();
            }

            private int stopPrice() {
                return ((Number) stopPriceField.getValue()).intValue();
            }
        });

        controls.add(new JLabel("Item:"));
        controls.add(itemIdField);
        controls.add(new JLabel("Stop price:"));
        controls.add(stopPriceField);
        controls.add(joinAuctionButton);

        return controls;
    }

    private void fillContentPane(JTable snipersTable, JPanel panel) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(panel,BorderLayout.NORTH);
        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
    }

    private JTable makeSniperTable(SniperPortfolio portfolio) {
        SnipersTableModel model = new SnipersTableModel();
        portfolio.addPortfolioListener(model);

        final JTable snipersTable = new JTable(model);
        snipersTable.setName(XMPPAuctionHouse.SNIPER_TABLE_NAME);



        return snipersTable;
    }


    private static JLabel createLabel(String initialText) {
        JLabel result = new JLabel(initialText);
        result.setName(XMPPAuctionHouse.SNIPER_STATUS_NAME);
        result.setBorder(new LineBorder(Color.BLACK));
        return result;
    }


    public void addUserRequestListener(UserRequestListener userRequestListener) {
        userRequests.addListener(userRequestListener);
    }
}