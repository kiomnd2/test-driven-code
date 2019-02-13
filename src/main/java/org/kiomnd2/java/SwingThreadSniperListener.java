package org.kiomnd2.java;


import javax.swing.*;

public class SwingThreadSniperListener implements SniperListener{
    private SniperListener listener;

    public SwingThreadSniperListener(SniperListener listener) {
        this.listener = listener;
    }

    @Override
    public void sniperStateChanged(final SniperSnapshot snapshot) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                    listener.sniperStateChanged(snapshot);
            }
        });
    }
}
