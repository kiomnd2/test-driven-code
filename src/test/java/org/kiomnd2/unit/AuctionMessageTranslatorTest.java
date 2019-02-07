package org.kiomnd2.unit;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.jupiter.api.Test;
import org.kiomnd2.ApplicationRunner;
import org.kiomnd2.java.AuctionEventListener;
import org.kiomnd2.java.AuctionMessageTranslator;

public class AuctionMessageTranslatorTest {
    public static final Chat UNUSED_CHAT = null;
    private final Mockery context = new Mockery();
    private final AuctionEventListener listener = context.mock(AuctionEventListener.class);
    private final AuctionMessageTranslator translator = new AuctionMessageTranslator(ApplicationRunner.SNIPER_ID ,listener);

    @Test
    public void notifiesAuctionClosedWhenCloseMessageReceived() {
        context.checking(new Expectations(){{
//            oneOf(listener).auctionClosed();
            exactly(1).of(listener).currentPrice(192,7);
        }
        });

        Message message = new Message();
        message.setBody("SOLVersion:1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;");
        translator.processMessage(UNUSED_CHAT, message);

    }

}
