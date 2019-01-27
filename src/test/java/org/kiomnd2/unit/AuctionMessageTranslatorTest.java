package org.kiomnd2.unit;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.junit.jupiter.api.Test;
import org.kiomnd2.java.AuctionEventListener;
import org.kiomnd2.java.AuctionMessageTranslator;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class AuctionMessageTranslatorTest {
    public static final Chat UNUSED_CHAT = null;

    private final AuctionEventListener listener = Mockito.mock(AuctionEventListener.class);
    private final AuctionMessageTranslator translator = new AuctionMessageTranslator(listener);

    @Test
    public void notifiesAuctionClosedWhenCloseMessageReceived() {


        Message message = new Message();
        message.setBody("SOLVersion:1.1; Event:CLOSE;");
        translator.processMessage(UNUSED_CHAT, message);

        verify(listener, times(1)).auctionClosed();
    }

}
