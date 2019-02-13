package org.kiomnd2.java;

import javax.swing.table.AbstractTableModel;

import static org.kiomnd2.java.SniperSnapshot.SniperState.JOINING;

public class SnipersTableModel extends AbstractTableModel implements SniperListener{
    private SniperSnapshot snapshots = new SniperSnapshot("",0,0, JOINING);

    private static String[] STATUS_TEXT = {"Joining","Bidding", "Winning","Lost", "Won" };


    @Override
    public String getColumnName(int column) {
        return Column.at(column).name;
    }

    public int getColumnCount() {
        return Column.values().length;
    }

    public int getRowCount() {
        return 1;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return Column.at(columnIndex).valueIn(snapshots);
    }
    public void sniperStateChanged(SniperSnapshot newSnapshot) {
        this.snapshots = newSnapshot;
        fireTableRowsUpdated(0,0);
    }

    public static String textFor(SniperSnapshot.SniperState state) {
        return STATUS_TEXT[(state).ordinal()];
    }


}