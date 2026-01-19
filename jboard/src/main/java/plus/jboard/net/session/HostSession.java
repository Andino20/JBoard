package plus.jboard.net.session;

import lombok.extern.slf4j.Slf4j;
import plus.jboard.core.GameApplication;
import plus.jboard.core.Updatable;
import plus.jboard.net.GameServer;
import plus.jboard.net.NetworkConnection;
import plus.jboard.net.NetworkEnvelope;
import plus.jboard.net.NetworkMessage;
import plus.jboard.net.async.AsyncGameServer;
import plus.jboard.net.handler.MessageDispatcher;
import plus.jboard.net.handler.MessageHandler;
import plus.jboard.net.session.protocol.HostHandshakeProtocol;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

@Slf4j
public class HostSession implements Session, MessageHandler<SessionMessage>, Updatable {

    private final HostConfig config;
    private final GameServer server;
    private final Map<UUID, NetworkConnection> players = new ConcurrentHashMap<>();
    private final Queue<NetworkConnection> pendingClients = new ConcurrentLinkedQueue<>();

    private Consumer<UUID> onPlayerJoin;

    public HostSession(HostConfig config) {
        this.config = config;

        GameApplication.getInstance().getMessageDispatcher().register(this);
        server = new AsyncGameServer(config.getPort());
        server.onNewClient(pendingClients::add);
    }

    public void start() {
        server.start();
        log.info("Hosting on port {} for up to {} clients.", config.getPort(), config.getMaxClients());
    }

    public void update() {
        NetworkConnection connection;
        while ((connection = pendingClients.poll()) != null)
            handleClient(connection);
    }

    public void onPlayerJoin(Consumer<UUID> callback) {
        this.onPlayerJoin = callback;
    }

    public boolean isFull() {
        return players.size() >= config.getMaxClients();
    }

    @Override
    public Class<SessionMessage> getAssociatedMessageType() {
        return SessionMessage.class;
    }

    @Override
    public void handle(NetworkEnvelope<SessionMessage> envelope) {
        log.info("Received Session Message: {}", envelope.message());
    }

    private void handleClient(NetworkConnection connection) {
        MessageDispatcher dispatcher = GameApplication.getInstance().getMessageDispatcher();
        HostHandshakeProtocol protocol = new HostHandshakeProtocol(connection, this)
                .onSuccess((playerId, channel) -> {
                    this.players.put(playerId, channel);
                    if (onPlayerJoin != null)
                        onPlayerJoin.accept(playerId);
                })
                .onFailure(NetworkConnection::close);
        protocol.onCleanup(() -> dispatcher.lateUnregister(protocol));
        dispatcher.register(protocol);

        log.info("Initiating handshake with remote client.");
        protocol.start();
    }

    @Override
    public void broadcast(NetworkMessage message) {
        players.values().forEach(channel -> channel.send(message));
    }

    @Override
    public void unicast(UUID target, NetworkMessage message) {
        if (players.containsKey(target))
            players.get(target).send(message);
    }
}
