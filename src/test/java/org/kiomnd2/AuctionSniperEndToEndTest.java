package org.kiomnd2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class AuctionSniperEndToEndTest {
    static {
        System.setProperty("com.objogate.wl.keyboard", "GB");
    }

    private final FakeAuctionServer auction = new FakeAuctionServer("item-56789");
    private final FakeAuctionServer auction2 = new FakeAuctionServer("item-67890");
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

        application.hasShownSniperIsBidding(auction,1000,1098);

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

        application.hasShownSniperIsBidding(auction,1000,1098); //최종 가격과 입찰

        auction.hasReceivedBid(1098,ApplicationRunner.SNIPER_XMPP_ID);

        auction.reportPrice(1098,97,ApplicationRunner.SNIPER_XMPP_ID);

        application.hasShownSniperIsWinning(auction,1098); //낙찰

        auction.announceClosed();

        application.showsSniperHasWonAuction(auction,1098);//최종가격

    }

    @Test
    public void hasShownSniperIsBidding() throws Exception{
        auction.startSellingItem();
        auction2.startSellingItem();

        application.startBiddingIn(auction,auction2);

        auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
        auction2.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);

        auction.reportPrice(1000,98,"other bidder");
        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);

        auction2.reportPrice(500,21,"other bidder");
        auction2.hasReceivedBid(521, ApplicationRunner.SNIPER_XMPP_ID);

        auction.reportPrice(1098,97, ApplicationRunner.SNIPER_XMPP_ID);
        auction2.reportPrice(521,22, ApplicationRunner.SNIPER_XMPP_ID);

        application.hasShownSniperIsWinning(auction,1098);
        application.hasShownSniperIsWinning(auction2, 521);

        auction.announceClosed();
        auction2.announceClosed();

        application.showsSniperHasWonAuction(auction, 1098);
        application.showsSniperHasWonAuction(auction2, 521);

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
