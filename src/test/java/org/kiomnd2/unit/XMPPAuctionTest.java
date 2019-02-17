package org.kiomnd2.unit;

import org.jivesoftware.smack.XMPPException;
import org.junit.Test;
import org.kiomnd2.ApplicationRunner;
import org.kiomnd2.FakeAuctionServer;
import org.kiomnd2.java.Auction;
import org.kiomnd2.java.AuctionEventListener;
import org.kiomnd2.java.Item;
import org.kiomnd2.java.XMPPAuctionHouse;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class XMPPAuctionTest {

    private final FakeAuctionServer server = new FakeAuctionServer("item-54321");
    private final XMPPAuctionHouse auctionHouse = XMPPAuctionHouse.connect(FakeAuctionServer.XMPP_HOSTNAME, ApplicationRunner.SNIPER_ID, ApplicationRunner.SNIPER_PASSWORD);


    public XMPPAuctionTest() throws XMPPException {

    }

    @Test
    public void receivesEventFromAuctionServerAfterJoining() throws Exception {
        CountDownLatch auctionWasClosed = new CountDownLatch(1);

        Auction auction = auctionHouse.auctionFor(new Item("item-56789", 5678));
        auction.addAuctionEventListener(auctionClosedListener(auctionWasClosed));

        auction.join();

        server.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
        server.announceClosed();

        assertTrue("should have been closed",auctionWasClosed.await(2,TimeUnit.SECONDS));
    }

    private AuctionEventListener auctionClosedListener(final CountDownLatch auctionWasClosed) {
        return new AuctionEventListener() {
            @Override
            public void auctionClosed() {
               auctionWasClosed.countDown();
            }
            @Override
            public void currentPrice(int price, int increment, PriceSource priceSource) {
                //rngus X
            }
            @Override
            public void auctionFailed() {

            }
        };
    }
}
