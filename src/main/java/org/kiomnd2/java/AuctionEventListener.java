package org.kiomnd2.java;

public interface AuctionEventListener {
    enum PriceSource {
        FromSniper, FromOtherBidder
    }
    void auctionClosed();

    void currentPrice(int price, int increment, PriceSource priceSource);
}
