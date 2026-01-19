package plus.jboard.net.handler;

import plus.jboard.net.NetworkMessage;
import plus.jboard.net.NetworkEnvelope;

public interface MessageHandler<T extends NetworkMessage> {

    Class<T> getAssociatedMessageType();
    void handle(NetworkEnvelope<T> messageContext);

}
