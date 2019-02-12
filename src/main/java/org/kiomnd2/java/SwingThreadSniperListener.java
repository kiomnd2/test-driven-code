package org.kiomnd2.java;

import javax.swing.*;

public class SwingThreadSniperListener implements SniperListener{
    private final SnipersTableModel snipers;

    public SwingThreadSniperListener(SnipersTableModel snipers) {
        this.snipers = snipers;
    }

    @Override
    public void sniperStateChanged(SniperSnapshot snapshot) {
        snipers.sniperStatusChanged(snapshot);
    }

}
