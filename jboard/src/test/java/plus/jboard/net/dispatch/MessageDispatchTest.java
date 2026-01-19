package plus.jboard.net.dispatch;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import plus.jboard.net.NetworkEnvelope;
import plus.jboard.net.NetworkMessage;
import plus.jboard.net.handler.MessageDispatcher;
import plus.jboard.net.handler.MessageHandler;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

class MessageDispatchTest {

    static class MockMessageHandlerBase<T extends NetworkMessage> implements MessageHandler<T> {

        private final Class<T> clazz;
        private final Consumer<NetworkEnvelope<T>> callback;

        public MockMessageHandlerBase(Class<T> clazz, Consumer<NetworkEnvelope<T>> callback) {
            this.clazz = clazz;
            this.callback = callback;
        }

        @Override
        public Class<T> getAssociatedMessageType() {
            return clazz;
        }

        @Override
        public void handle(NetworkEnvelope<T> envelope) {
            this.callback.accept(envelope);
        }
    }

    @Test
    void dispatchMessageTest() {
        MessageDispatcher dispatcher = new MessageDispatcher();


        MockMessageHandlerBase<GameMessage> gameMessageHandler =
                new MockMessageHandlerBase<>(GameMessage.class, envelope -> {
                    Assertions.fail("Dispatched to wrong handler");
                });

        AtomicBoolean success = new AtomicBoolean(false);
        MockMessageHandlerBase<SessionMessage> sessionMessageHandler =
                new MockMessageHandlerBase<>(SessionMessage.class, envelope -> {
                    success.set(true);
                });

        dispatcher.register(gameMessageHandler);
        dispatcher.register(sessionMessageHandler);

        dispatcher.dispatch(new NetworkEnvelope<>(null, new SessionMessage()));
        Assertions.assertTrue(success.get());
    }

}
