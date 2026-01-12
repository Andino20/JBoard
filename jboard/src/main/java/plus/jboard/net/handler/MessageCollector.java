package plus.jboard.net.handler;

import plus.jboard.net.NetworkMessage;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageCollector implements MessageHandler<NetworkMessage> {

    private final ConcurrentLinkedQueue<NetworkMessage> messageQueue = new ConcurrentLinkedQueue<>();

    public NetworkMessage poll() {
        return messageQueue.poll();
    }

    @Override
    public Class<NetworkMessage> getAssociatedMessageType() {
        return NetworkMessage.class;
    }

    @Override
    public void handle(NetworkMessage msg) {
        if (msg != null)
            messageQueue.add(msg);
    }

}
