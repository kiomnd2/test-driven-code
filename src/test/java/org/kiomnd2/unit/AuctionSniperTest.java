package org.kiomnd2.unit;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.junit.jupiter.api.Test;
import org.kiomnd2.java.Auction;
import org.kiomnd2.java.AuctionEventListener;
import org.kiomnd2.java.AuctionSniper;
import org.kiomnd2.java.SniperListener;
import org.kiomnd2.java.AuctionEventListener.*;

public class AuctionSniperTest {
    private final Mockery context = new Mockery();
    private final Auction auction = context.mock(Auction.class);
    private final SniperListener sniperListener = context.mock(SniperListener.class);
    private final AuctionSniper sniper= new AuctionSniper(auction,sniperListener);
    private final States sniperState =context.states("sniper5");

    @Test
    public void reportsLostWhenAuctionClosesImmediately() {
        context.checking(new Expectations(){{
            oneOf(sniperListener).sniperLost();
        }});

        sniper.auctionClosed();
    }

    @Test
    public void reportsLostIfAuctionClosesWhenBidding() {
        context.checking(new Expectations(){{
            ignoring(auction);
            allowing(sniperListener).sniperBidding();
                                        then(sniperState.is("bidding"));
            atLeast(1).of(sniperListener).sniperLost();
            when(sniperState.is("bidding"));
        }});
        sniper.currentPrice(123,45,PriceSource.FromOtherBidder);
        sniper.auctionClosed();
    }
    @Test
    public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        final int price = 1001;
        final int increment = 25;
        context.checking(new Expectations(){{
            oneOf(auction).bid(price + increment);
            atLeast(1).of(sniperListener).sniperBidding();
        }});

        sniper.currentPrice(price,increment,PriceSource.FromOtherBidder);
    }

    @Test
    public void reportsIsWinningWhenCurrentPriceComesFromSniper() {
        context.checking(new Expectations(){{
            atLeast(1).of(sniperListener).sniperWinning();
        }});
        sniper.currentPrice(123,45, PriceSource.FromSniper);
    }

    @Test
    public void reportsWonIfAuctionClosesWhenWinning() {
        context.checking(new Expectations(){{
            ignoring(auction);
            allowing(sniperListener).sniperWinning(); then(sniperState.is("winning"));
            atLeast(1).of(sniperListener).sniperWon(); when(sniperState.is("winning"));
        }});
        sniper.currentPrice(123,45,PriceSource.FromSniper);
        sniper.auctionClosed();
    }
}
