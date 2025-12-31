package plus.jboard.net.client;

import plus.jboard.net.PlayerSession;
import plus.jboard.net.SessionEndpoint;

import java.util.concurrent.CompletableFuture;

public interface SessionClient {

    CompletableFuture<PlayerSession> joinSession(SessionEndpoint endpoint);

}
