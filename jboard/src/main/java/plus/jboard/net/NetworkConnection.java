package plus.jboard.net;

import plus.jboard.net.handler.ReceiveHandler;

public interface NetworkConnection {
    void send(byte[] data);
    void setOnReceiveHandler(ReceiveHandler receiver);
    void close();
}
