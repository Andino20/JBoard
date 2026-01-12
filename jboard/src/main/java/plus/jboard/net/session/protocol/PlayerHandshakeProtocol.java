package plus.jboard.net.session.protocol;

import lombok.extern.slf4j.Slf4j;
import plus.jboard.net.NetworkConnection;
import plus.jboard.net.handler.MessageHandler;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

@Slf4j
public class PlayerHandshakeProtocol implements MessageHandler<HandshakeMessage> {

    private enum HandshakeState {
        CONNECTED, READY, FAILED
    }

    private final NetworkConnection connection;
    private HandshakeState state = HandshakeState.CONNECTED;

    private Consumer<UUID> onSuccess;
    private Consumer<NetworkConnection> onFailure;
    private Runnable onCleanup;

    public PlayerHandshakeProtocol(NetworkConnection connection) {
        this.connection = connection;
    }

    @Override
    public Class<HandshakeMessage> getAssociatedMessageType() {
        return HandshakeMessage.class;
    }

    @Override
    public void handle(HandshakeMessage msg) {
        if (Objects.requireNonNull(state) == HandshakeState.CONNECTED) {
            handleServerResponse(msg);
        } else {
            log.error("Received unexpected host message: {}", msg);
            throw new IllegalStateException("Unexpected host message");
        }
    }

    private void handleServerResponse(HandshakeMessage msg) {
        if (msg instanceof ServerAcceptMessage acceptMessage) {
            log.info("Connected to host. Player ID = {}", acceptMessage.getClientId());

            this.state = HandshakeState.READY;
            this.onSuccess.accept(acceptMessage.getClientId());
            this.onCleanup.run();
        } else if (msg instanceof ServerRejectMessage rejectMessage) {
            log.info("Connection refused by the host: {}", rejectMessage.getReason());

            this.state = HandshakeState.FAILED;
            this.onFailure.accept(connection);
            this.onCleanup.run();
        }
    }

    public PlayerHandshakeProtocol onSuccess(Consumer<UUID> callback) {
        this.onSuccess = callback;
        return this;
    }

    public PlayerHandshakeProtocol onFailure(Consumer<NetworkConnection> callback) {
        this.onFailure = callback;
        return this;
    }

    public PlayerHandshakeProtocol onCleanup(Runnable callback) {
        this.onCleanup = callback;
        return this;
    }

}
