package plus.jboard.net.session;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import plus.jboard.core.GameApplication;
import plus.jboard.net.NetworkConnection;
import plus.jboard.net.NetworkMessage;
import plus.jboard.net.async.AsyncSocketConnection;
import plus.jboard.net.handler.MessageCollector;
import plus.jboard.net.handler.MessageDispatcher;
import plus.jboard.net.session.protocol.PlayerHandshakeProtocol;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;
import java.util.function.Consumer;

@Slf4j
public class PlayerSession implements Session {

    private final PlayerConfig config;
    private NetworkConnection connection;

    @Getter
    @Setter
    private UUID playerId;

    private Consumer<UUID> onJoinSuccess;

    public PlayerSession(PlayerConfig config) {
        this.config = config;
    }

    public void start() {
        try {
            MessageCollector collector = GameApplication.getInstance().getMessageCollector();
            Socket socket = new Socket(config.getTargetHost(), config.getTargetPort());
            connection = new AsyncSocketConnection(socket, collector);
            log.info("Connected to host {}:{}.", config.getTargetHost(), config.getTargetPort());

            doHandshake();
        } catch (IOException e) {
            log.error("Failed to connect to host {}:{}.", config.getTargetHost(), config.getTargetPort());
        }
    }

    public void onJoinSuccess(Consumer<UUID> callback) {
        this.onJoinSuccess = callback;
    }

    private void doHandshake() {
        MessageDispatcher dispatcher = GameApplication.getInstance().getMessageDispatcher();
        PlayerHandshakeProtocol protocol = new PlayerHandshakeProtocol(connection)
                .onSuccess(id -> {
                    this.setPlayerId(id);
                    this.onJoinSuccess.accept(id);
                })
                .onFailure(NetworkConnection::close);
        protocol.onCleanup(() -> dispatcher.lateUnregister(protocol));
        dispatcher.register(protocol);
    }

    @Override
    public void broadcast(NetworkMessage message) {
        connection.send(message);
    }

    @Override
    public void unicast(UUID target, NetworkMessage message) {
        connection.send(message);
    }
}
