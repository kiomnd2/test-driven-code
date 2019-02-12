package org.kiomnd2.java;

import javax.swing.table.AbstractTableModel;

import static org.kiomnd2.java.SniperSnapshot.SniperState.JOINING;

public class SnipersTableModel extends AbstractTableModel {
    private String state ;
    private SniperSnapshot snapshots = new SniperSnapshot("",0,0, JOINING);
    private static String[] STATUS_TEXT = {"Joining","Bidding", "Winning","Lost", "Won" };

    private String statusText = Main.STATUS_JOINING;


    public int getColumnCount() {
        return Column.values().length;
    }

    public int getRowCount() {
        return 1;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (Column.at(columnIndex)){
            case ITEM_IDENTIFIER:
                return snapshots.itemId;
            case LAST_PRICE:
                return snapshots.lastPrice;
            case LAST_BID:
                return snapshots.lastBid;
            case SNIPER_STATUS:
                return textFor(snapshots.state);
            default:
                throw new IllegalArgumentException("No Column at " +columnIndex);
        }
    }
    public void sniperStatusChanged(SniperSnapshot newSnapshot) {
        this.snapshots = newSnapshot;
        fireTableRowsUpdated(0,0);
    }

    public static String textFor(SniperSnapshot.SniperState state) {
        return STATUS_TEXT[(state).ordinal()];
    }

    public void setStatusText(String newStatusText) {
        statusText = newStatusText;
        fireTableRowsUpdated(0,0);
    }

}