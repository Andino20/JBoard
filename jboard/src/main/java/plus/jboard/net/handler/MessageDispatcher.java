package plus.jboard.net.handler;

import lombok.extern.slf4j.Slf4j;
import plus.jboard.core.Updatable;
import plus.jboard.net.NetworkMessage;
import plus.jboard.net.NetworkEnvelope;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public class MessageDispatcher implements Updatable {

    private final List<MessageHandler<?>> handlers = new LinkedList<>();
    private final Queue<MessageHandler<?>> pendingUnregisters = new ConcurrentLinkedQueue<>();
    private final Queue<MessageHandler<?>> pendingRegisters = new ConcurrentLinkedQueue<>();

    public void register(MessageHandler<?> handler) {
        if (handler != null && !handlers.contains(handler)) {
            handlers.add(handler);
            log.info("New message handler registered for messages of type {}", handler.getAssociatedMessageType().getTypeName());
        }
    }

    public void unregister(MessageHandler<?> handler) {
        if (handlers.remove(handler))
            log.info("Removed a message handler for messages of type {}", handler.getAssociatedMessageType().getTypeName());
    }

    public void lateUnregister(MessageHandler<?> handler) {
        pendingUnregisters.add(handler);
    }

    public void lateRegister(MessageHandler<?> handler) {
        pendingRegisters.add(handler);
    }

    public void dispatch(NetworkEnvelope<NetworkMessage> envelope) {
        handlers.stream()
                .filter(x -> canDispatchTo(x, envelope.message()))
                .forEach(x -> {
                    try {
                        @SuppressWarnings("unchecked") MessageHandler<NetworkMessage> handler = (MessageHandler<NetworkMessage>) x;
                        handler.handle(envelope);
                    } catch (ClassCastException e) {
                        log.error("An unexpected error occurred when dispatch a network message", e);
                    }
                });
    }

    private boolean canDispatchTo(MessageHandler<?> handler, NetworkMessage msg) {
        return handler.getAssociatedMessageType().isInstance(msg);
    }

    @Override
    public void update() {
        MessageHandler<?> handler;
        while ((handler = pendingUnregisters.poll()) != null)
            unregister(handler);

        while ((handler = pendingRegisters.poll()) != null)
            register(handler);
    }
}
