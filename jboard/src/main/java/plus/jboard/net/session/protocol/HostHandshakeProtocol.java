package plus.jboard.net.session.protocol;

import lombok.extern.slf4j.Slf4j;
import plus.jboard.net.NetworkConnection;
import plus.jboard.net.NetworkEnvelope;
import plus.jboard.net.handler.MessageHandler;
import plus.jboard.net.session.HostSession;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Slf4j
public class HostHandshakeProtocol implements MessageHandler<HandshakeMessage> {

    private enum HandshakeState {
        CONNECTED, READY, FAILED
    }

    private final NetworkConnection connection;
    private final HostSession session;

    private HandshakeState state = HandshakeState.CONNECTED;

    private BiConsumer<UUID, NetworkConnection> onSuccess;
    private Consumer<NetworkConnection> onFailure;
    private Runnable onCleanup;

    public HostHandshakeProtocol(NetworkConnection connection, HostSession session) {
        this.connection = connection;
        this.session = session;
    }

    public void start() {
        if (this.session.isFull()) {
            sendServerReject();
            this.state = HandshakeState.READY;
        } else {
            sendServerAccept();
            this.state = HandshakeState.FAILED;
        }
        onCleanup.run();
    }

    private void sendServerReject() {
        connection.send(ServerRejectMessage.builder().reason("Session is full").build());
        onFailure.accept(connection);
    }

    private void sendServerAccept() {
        UUID playerId = UUID.randomUUID();
        connection.send(ServerAcceptMessage.builder().clientId(playerId).build());
        onSuccess.accept(playerId, connection);
    }

    @Override
    public Class<HandshakeMessage> getAssociatedMessageType() {
        return HandshakeMessage.class;
    }

    @Override
    public void handle(NetworkEnvelope<HandshakeMessage> msg) {
        if (msg.channel() != connection)
            return;

        log.warn("Unexpected client handshake message received {}", msg);
    }

    public HostHandshakeProtocol onSuccess(BiConsumer<UUID, NetworkConnection> callback) {
        this.onSuccess = callback;
        return this;
    }

    public HostHandshakeProtocol onFailure(Consumer<NetworkConnection> callback) {
        this.onFailure = callback;
        return this;
    }

    public HostHandshakeProtocol onCleanup(Runnable callback) {
        this.onCleanup = callback;
        return this;
    }

}
