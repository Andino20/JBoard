package plus.jboard.net.dispatch;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import plus.jboard.net.NetworkEnvelope;
import plus.jboard.net.handler.MessageDispatcher;
import plus.jboard.net.handler.MessageHandler;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

class MessageDispatchTest {

    static class MockGameMessageHandler implements MessageHandler<GameMessage> {

        private final Consumer<GameMessage> callback;

        public MockGameMessageHandler(Consumer<GameMessage> callback) {
            this.callback = callback;
        }

        @Override
        public Class<GameMessage> getAssociatedMessageType() {
            return GameMessage.class;
        }

        @Override
        public void handle(NetworkEnvelope<GameMessage> envelope) {
            callback.accept(envelope.message());
        }

    }

    static class MockSessionMessageHandler implements MessageHandler<SessionMessage> {

        private final Consumer<SessionMessage> callback;

        public MockSessionMessageHandler(Consumer<SessionMessage> callback) {
            this.callback = callback;
        }

        @Override
        public Class<SessionMessage> getAssociatedMessageType() {
            return SessionMessage.class;
        }

        @Override
        public void handle(NetworkEnvelope<SessionMessage> envelope) {
            this.callback.accept(envelope.message());
        }

    }

    @Test
    void dispatchMessageTest() {
        MessageDispatcher dispatcher = new MessageDispatcher();

        AtomicBoolean success = new AtomicBoolean(false);
        MockGameMessageHandler gameHandler = new MockGameMessageHandler(msg -> Assertions.fail("Dispatched to wrong handler"));
        MockSessionMessageHandler sessionHandler = new MockSessionMessageHandler(msg -> {
            Assertions.assertEquals(10, msg.getId());
            success.set(true);
        });
        dispatcher.register(gameHandler);
        dispatcher.register(sessionHandler);

        dispatcher.dispatch(new NetworkEnvelope<>(null, new SessionMessage(10)));
        Assertions.assertTrue(success.get());
    }

}
