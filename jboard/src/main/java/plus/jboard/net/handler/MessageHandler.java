package plus.jboard.net.handler;

import plus.jboard.net.NetworkMessage;

public interface MessageHandler<T extends NetworkMessage> {

    Class<T> getAssociatedMessageType();
    void handle(T msg);

}
