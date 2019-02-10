package org.kiomnd2.java;


import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

public class MainWindow extends JFrame {

    private SniperTableModel snipers = new SniperTableModel();
    private final JLabel sniperStatus = createLabel(Main.STATUS_JOINING);
    public MainWindow() {
        super("Action Sniper");
        setName(Main.MAIN_WINDOW_NAME);
        add(sniperStatus);
        fillContextPane(makeSnipersTable());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void fillContextPane(JTable snipersTable){
        final Container contextPane = getContentPane();
        contextPane.setLayout(new BorderLayout());
        contextPane.add(new JScrollPane(snipersTable),BorderLayout.CENTER);

    }

    private JTable makeSnipersTable() {
        final JTable snipersTable = new JTable(snipers);
        snipersTable.setName(Main.SNIPER_TABLE_NAME);
        return snipersTable;
    }

    public void showStatusText(String statusText) {
        snipers.setStatusText(statusText);
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

    public class SniperTableModel extends AbstractTableModel {
        private String statusText = Main.STATUS_JOINING;

        public int getColumnCount() {
            return 1;
        }

        public int getRowCount() {
            return 1;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return statusText;
        }

        public void setStatusText(String newStatusText) {
            statusText = newStatusText;
            fireTableRowsUpdated(0,0);
        }


    }
}