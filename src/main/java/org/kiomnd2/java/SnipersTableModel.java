package org.kiomnd2.java;

import com.objogate.exception.Defect;

import javax.swing.table.AbstractTableModel;

import java.util.ArrayList;
import java.util.List;


public class SnipersTableModel extends AbstractTableModel implements SniperListener{

    private static String[] STATUS_TEXT = {"Joining","Bidding", "Winning","Lost", "Won" };
    private List<SniperSnapshot> sniperSnapshots = new ArrayList<>();

    @Override
    public String getColumnName(int column) {
        return Column.at(column).name;
    }

    public int getColumnCount() {
        return Column.values().length;
    }

    public int getRowCount() {
        return sniperSnapshots.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return Column.at(columnIndex).valueIn(sniperSnapshots.get(rowIndex));
    }

    public void addSniper(SniperSnapshot snapshot) {
        sniperSnapshots.add(snapshot);
    }

    public void sniperStateChanged(SniperSnapshot newSnapshot) {
        int row = rowMatching(newSnapshot);
        sniperSnapshots.set(row, newSnapshot);
        fireTableRowsUpdated(row,row);
    }

    private  int rowMatching(SniperSnapshot newSnapshot) {
        for (int i =0; i < sniperSnapshots.size() ; i++) {
            if(newSnapshot.isForSameItemAs(sniperSnapshots.get(i))){
                return i;
            }
        }
        throw new Defect("Cannot Find match For"+ sniperSnapshots);
    }
    public static String textFor(SniperSnapshot.SniperState state) {
        return STATUS_TEXT[state.ordinal()];
    }


}