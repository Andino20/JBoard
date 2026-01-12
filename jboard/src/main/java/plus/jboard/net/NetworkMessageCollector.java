package plus.jboard.net;

import plus.jboard.net.handler.MessageHandler;

public interface NetworkMessageCollector extends MessageHandler<NetworkMessage> {

    NetworkMessage poll();

}
