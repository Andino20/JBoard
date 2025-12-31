package plus.jboard.net.handler;

@FunctionalInterface
public interface ReceiveHandler {
    void receive(byte[] data);
}
