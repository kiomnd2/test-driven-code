package org.kiomnd2.java;

public class AuctionSniper implements AuctionEventListener{
    private boolean isWinning =false;
    private SniperSnapshot snapshot;
    private final SniperListener sniperListener;
    private final Auction auction;

    public AuctionSniper(String itemId, Auction auction ,SniperListener sniperListener){

        this.auction = auction;
        this.sniperListener = sniperListener;
        this.snapshot = snapshot.joining(itemId);
    }
    @Override
    public void auctionClosed() {
        snapshot = snapshot.closed();
        nofityChange();
    }
    @Override
    public void currentPrice(int price, int increment, PriceSource priceSource) {
        switch(priceSource){
            case FromSniper:
                snapshot = snapshot.winning(price);
                break;
            case FromOtherBidder:
                int bid = price+increment;
                auction.bid(bid);
                snapshot = snapshot.bidding(price,bid);
                break;
        }
        nofityChange();

        // 자동 생성된 메서드 스텁
    }
    private void nofityChange() {
        sniperListener.sniperStateChanged(snapshot);

    }
}
