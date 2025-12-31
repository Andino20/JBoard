package plus.jboard.net;

import plus.jboard.net.client.SessionClient;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

class MockSessionClient implements SessionClient {

    private final MockSessionServer server;

    public MockSessionClient(MockSessionServer server) {
        this.server = server;
    }

    @Override
    public CompletableFuture<PlayerSession> joinSession(SessionEndpoint endpoint) {
        return Objects.nonNull(server)
                ? CompletableFuture.completedFuture(new PlayerSession(server.acceptClient()))
                : CompletableFuture.failedFuture(new RuntimeException("Attempt to connect to session failed."));
    }
}
