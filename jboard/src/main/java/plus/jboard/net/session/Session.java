package plus.jboard.net.session;

import plus.jboard.net.NetworkMessage;

import java.util.UUID;

public interface Session {
    void broadcast(NetworkMessage message);
    void unicast(UUID target, NetworkMessage message);
}
