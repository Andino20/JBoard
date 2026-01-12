package plus.jboard.net;

public interface NetworkConnection {
    void send(NetworkMessage msg);
    void close();
}
