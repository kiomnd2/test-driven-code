package org.kiomnd2.java;

public class AuctionSniper implements AuctionEventListener{
    private boolean isWinning =false;
    private SniperSnapshot snapshot;
    private final Auction auction;
    private final Item item;
    private final Announcer<SniperListener> listeners = Announcer.to(SniperListener.class);

    public AuctionSniper(Item item, Auction auction){
        this.item = item;
        this.auction = auction;
        this.snapshot = SniperSnapshot.joining(item.getIdentifier());
    }

    @Override
    public void auctionClosed() {
        snapshot = snapshot.closed();
        notifyChange();
    }

    @Override
    public void auctionFailed() {
//        snapshot = snapshot.failed();

        notifyChange();
    }

    @Override
    public void currentPrice(int price, int increment, PriceSource priceSource) {
        switch(priceSource){
            case FromSniper:
                snapshot = snapshot.winning(price);
                break;
            case FromOtherBidder:
                int bid = price+increment;
                if( item.allowsBid(bid)){
                    auction.bid(bid);
                    snapshot = snapshot.bidding(price,bid);

                }
                else {
                    snapshot = snapshot.losing(price);
                }
                break;
        }
        notifyChange();

        // 자동 생성된 메서드 스텁
    }
    private void notifyChange() {
        listeners.announce().sniperStateChanged(snapshot);
    }

    public void addSniperListener(SniperListener listener) {
        listeners.addListener(listener);
    }

    public SniperSnapshot getSnapshot() {
        return snapshot;
    }
}
