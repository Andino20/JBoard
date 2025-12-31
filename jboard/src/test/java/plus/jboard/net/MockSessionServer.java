package plus.jboard.net;

import plus.jboard.net.server.SessionServer;

import java.util.function.Consumer;

class MockSessionServer implements SessionServer {

    private Consumer<NetworkConnection> newClientHandler;

    public MockConnection acceptClient() {
        MockConnection hostConnection = new MockConnection();
        MockConnection clientConnection = new MockConnection(hostConnection);

        hostConnection.setOther(clientConnection);
        newClientHandler.accept(hostConnection);
        return clientConnection;
    }

    @Override
    public void onNewClient(Consumer<NetworkConnection> handler) {
        this.newClientHandler = handler;
    }

    @Override
    public void start() {

    }
}
