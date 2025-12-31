package plus.jboard.net.handler;

import java.util.UUID;

@FunctionalInterface
public interface PlayerJoinHandler {
    void handle(UUID playerId);
}
