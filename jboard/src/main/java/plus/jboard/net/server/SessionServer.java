package plus.jboard.net.server;

import plus.jboard.net.NetworkConnection;

import java.util.function.Consumer;

public interface SessionServer {

    void onNewClient(Consumer<NetworkConnection> handler);

    void start();
}
