package plus.jboard.net;

import java.util.function.Consumer;

public interface GameServer {

    void onNewClient(Consumer<NetworkConnection> handler);

    void start();

}
