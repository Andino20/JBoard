package plus.jboard.net.handler;

import plus.jboard.net.NetworkEnvelope;
import plus.jboard.net.NetworkMessage;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageCollector implements MessageHandler<NetworkMessage> {

    private final ConcurrentLinkedQueue<NetworkEnvelope<NetworkMessage>> messageQueue = new ConcurrentLinkedQueue<>();

    public NetworkEnvelope<NetworkMessage> poll() {
        return messageQueue.poll();
    }

    @Override
    public Class<NetworkMessage> getAssociatedMessageType() {
        return NetworkMessage.class;
    }

    @Override
    public void handle(NetworkEnvelope<NetworkMessage> envelope) {
        if (envelope != null)
            messageQueue.add(envelope);
    }

}
