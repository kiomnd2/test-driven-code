package org.kiomnd2.unit;

import org.hamcrest.CoreMatchers;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.junit.jupiter.api.Test;
import org.kiomnd2.java.*;
import org.kiomnd2.java.AuctionEventListener.*;

import static org.kiomnd2.java.SniperSnapshot.SniperState.*;
import static org.kiomnd2.java.SniperSnapshot.*;


public class AuctionSniperTest {
    private final Mockery context = new Mockery();
    private final Auction auction = context.mock(Auction.class);
    private final SniperListener sniperListener = context.mock(SniperListener.class);
    private final States sniperState =context.states("sniper5");
    private final String ITEM_ID = "test-item";
    private final Item item = new Item(ITEM_ID, 789);
    private final AuctionSniper sniper= new AuctionSniper(item,auction);

    @Test
    public void reportsLostWhenAuctionClosesImmediately() {
        context.checking(new Expectations(){{
            oneOf(sniperListener).sniperStateChanged(with(aSniperThatis(LOST)));
        }});

        sniper.auctionClosed();
    }

    @Test
    public void reportsLostIfAuctionClosesWhenBidding() {
        context.checking(new Expectations(){{
            ignoring(auction);
            allowing(sniperListener).sniperStateChanged(
                    with(aSniperThatis(BIDDING))
            );
            then(sniperState.is("bidding"));

            atLeast(1).of(sniperListener).sniperStateChanged(with(aSniperThatis(LOST)));
            when(sniperState.is("bidding"));
        }});
        sniper.currentPrice(123,45,PriceSource.FromOtherBidder);
        sniper.auctionClosed();
    }
    @Test
    public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        final int price = 1001;
        final int increment = 25;
        final int bid = price + increment;

        context.checking(new Expectations(){{
            oneOf(auction).bid(bid);
            atLeast(1).of(sniperListener).sniperStateChanged(with(aSniperThatis(BIDDING)));
        }});

        sniper.currentPrice(price,increment,PriceSource.FromOtherBidder);
    }

    @Test
    public void reportsIsWinningWhenCurrentPriceComesFromSniper() {
        context.checking(new Expectations(){{
            ignoring(auction);
            allowing(sniperListener).sniperStateChanged(with(aSniperThatis(BIDDING)));
            then(sniperState.is("Bidding"));
            atLeast(1).of(sniperListener).sniperStateChanged(new SniperSnapshot(item.getIdentifier(),135,135,WINNING));
            when(sniperState.is("Bidding"));
        }});
        sniper.currentPrice(123,12, PriceSource.FromOtherBidder);
        sniper.currentPrice(135,45, PriceSource.FromSniper);
    }

    @Test
    public void reportsWonIfAuctionClosesWhenWinning() {
        context.checking(new Expectations(){{
            ignoring(auction);
            allowing(sniperListener).sniperStateChanged(with(aSniperThatis(WINNING))); then(sniperState.is("winning"));
            atLeast(1).of(sniperListener).sniperStateChanged(with(aSniperThatis(WON))); when(sniperState.is("winning"));
        }});
        sniper.currentPrice(123,45,PriceSource.FromSniper);
        sniper.auctionClosed();
    }

    private Matcher<SniperSnapshot> aSniperThatis(final SniperState state) {
        return new FeatureMatcher<SniperSnapshot, SniperState>(
                CoreMatchers.equalTo(state), "sniper that is ","was"){
            @Override
            protected SniperState featureValueOf(SniperSnapshot actual) {
                return actual.state;
            }
        };

    }

}
