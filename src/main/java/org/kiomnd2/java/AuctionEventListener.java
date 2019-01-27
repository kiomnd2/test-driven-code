package org.kiomnd2.java;

public interface AuctionEventListener {
    void auctionClosed();

    void currentPrice(int price, int increment);
}
