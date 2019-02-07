package org.kiomnd2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class AuctionSniperEndToEndTest {
    private final FakeAuctionServer auction = new FakeAuctionServer("item-56789");
    private final ApplicationRunner application = new ApplicationRunner();


    @Test
    public void testJoinAndNotBidding() throws Exception {
        //판매시작
        auction.startSellingItem();
        //참여
        application.startBiddingIn(auction);
        //가입했오?
        auction.hasReceivedJoinRequestFromSniper();
        //문닫았어요
        auction.announceClosed();
        //ㅠ
        application.showsSniperHasLostAuction();

    }
    @Test
    public void sniperMakesAHigherBidButLoses() throws Exception {
        auction.startSellingItem();

        application.startBiddingIn(auction);

        auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);

        auction.reportPrice(1000,98, "other bidder");

        application.hasShownSniperIsBidding();

        auction.hasReceivedBid(1098,ApplicationRunner.SNIPER_XMPP_ID);

        auction.announceClosed();

        application.showsSniperHasLostAuction();
    }

    @Test
    public void sniperWinsAnAuctionByBiddingHigher() throws Exception {
        auction.startSellingItem();

        application.startBiddingIn(auction);

        auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);

        auction.reportPrice(1000,98, "other bidder");

        application.hasShownSniperIsBidding();

        auction.hasReceivedBid(1098,ApplicationRunner.SNIPER_XMPP_ID);

        auction.reportPrice(1098,97,ApplicationRunner.SNIPER_XMPP_ID);

        application.hasShownSniperIsWinning();

        auction.announceClosed();

        application.showsSniperHasLostAuction();

    }

    @AfterEach
    public void stopAuction() {
        auction.stop();
    }

    @AfterEach
    public void stopApplication() {
        application.stop();
    }



}
