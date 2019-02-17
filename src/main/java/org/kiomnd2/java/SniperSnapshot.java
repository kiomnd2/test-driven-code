package org.kiomnd2.java;

import com.objogate.exception.Defect;

public class SniperSnapshot {
    public final String item ;
    public final int lastPrice;
    public final int lastBid;
    public final SniperState state;

    public SniperSnapshot(String item, int lastPrice, int lastBid, SniperState state){
        this.item = item;
        this.lastPrice = lastPrice;
        this.lastBid = lastBid;
        this.state = state;
    }

    public SniperSnapshot bidding(int newLastPrice, int newLastBid) {
        return new SniperSnapshot(item, newLastPrice, newLastBid,SniperState.BIDDING);
    }

    public SniperSnapshot winning(int newLastPrice) {
        return new SniperSnapshot(item, newLastPrice, newLastPrice, SniperState.WINNING);
    }

    public static SniperSnapshot joining(String item) {
        return new SniperSnapshot(item, 0, 0, SniperState.JOINING);
    }

    public SniperSnapshot closed() {
        return new SniperSnapshot(item,lastPrice,lastBid, state.whenAuctionClosed());
    }

    public SniperSnapshot losing(int newLastPrice) {
        return new SniperSnapshot(item, newLastPrice,lastBid, SniperState.LOSING);
    }

    public boolean isForSameItemAs(SniperSnapshot snapshot) {
        return snapshot.item == this.item;
    }

    public enum SniperState {
        JOINING {
            @Override
            public SniperState whenAuctionClosed() {
                return LOST;
            }
        },
        BIDDING {
            @Override
            public SniperState whenAuctionClosed() {
                return LOST;
            }
        },
        WINNING {
            @Override
            public SniperState whenAuctionClosed() {
                return WON;
            }
        },
        LOSING {
            @Override
            public SniperState whenAuctionClosed() {
                return LOST;
            }
        },
        LOST,
        WON;

        public SniperState whenAuctionClosed() {
            throw new Defect("Auction is already closed");
        }
    }
}
