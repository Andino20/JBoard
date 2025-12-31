package plus.jboard.net;

import com.google.protobuf.InvalidProtocolBufferException;
import plus.jboard.net.handler.PlayerJoinHandler;
import plus.jboard.net.handler.ReceiveHandler;
import plus.jboard.net.protocol.session.HandshakeEnvelope;
import plus.jboard.net.protocol.session.RejectReason;
import plus.jboard.net.protocol.session.ServerJoinAccepted;
import plus.jboard.net.protocol.session.ServerJoinRejected;
import plus.jboard.net.server.SessionServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HostSession {

    private static final int PROTOCOL_VERSION = 1;

    private final HostSettings settings;
    private final List<PlayerJoinHandler> joinHandlers = new ArrayList<>();
    private final Map<UUID, NetworkConnection> players = new ConcurrentHashMap<>();

    public HostSession(HostSettings settings) {
        this.settings = settings;
    }

    public void start() {
        SessionServer server = settings.getServerSupplier().get();
        server.onNewClient(this::handleNewClient);
        server.start();
    }

    private void handleNewClient(NetworkConnection clientConnection) {
        clientConnection.setOnReceiveHandler(data -> {
            try {
                HandshakeEnvelope greeting = HandshakeEnvelope.parseFrom(data);

                if (greeting.getProtocolVersion() != PROTOCOL_VERSION) {
                    rejectClient(clientConnection, ServerJoinRejected.newBuilder()
                            .setReason(RejectReason.VERSION_MISMATCH)
                            .setMessage("Expected protocol version " + PROTOCOL_VERSION).build());
                    return;
                }

                if (players.size() >= settings.getMaxClients()) {
                    rejectClient(clientConnection, ServerJoinRejected.newBuilder()
                            .setReason(RejectReason.SESSION_FULL)
                            .setMessage(String.format("Session is full (%d players)", players.size())).build());
                    return;
                }

                if (greeting.getPayloadCase() != HandshakeEnvelope.PayloadCase.CLIENT_JOIN) {
                    rejectClient(clientConnection, ServerJoinRejected.newBuilder()
                            .setReason(RejectReason.INVALID_REQUEST)
                            .setMessage("Request was invalid").build());
                    return;
                }

                acceptClient(clientConnection);
                clientConnection.setOnReceiveHandler(null);
            } catch (InvalidProtocolBufferException e) {
                rejectClient(clientConnection, ServerJoinRejected.newBuilder()
                        .setReason(RejectReason.INVALID_REQUEST)
                        .setMessage("Request was invalid").build());
            }
        });
    }

    private void acceptClient(NetworkConnection con) {
        UUID id = UUID.randomUUID();
        con.send(HandshakeEnvelope.newBuilder()
                .setProtocolVersion(PROTOCOL_VERSION)
                .setServerAccepted(ServerJoinAccepted.newBuilder().setPlayerId(id.toString()).build())
                .build().toByteArray());
        this.players.put(id, con);
        this.joinHandlers.forEach(h -> h.handle(id));
    }

    private void rejectClient(NetworkConnection con, ServerJoinRejected rejection) {
        HandshakeEnvelope response = HandshakeEnvelope.newBuilder()
                .setProtocolVersion(PROTOCOL_VERSION)
                .setServerRejected(rejection).build();
        con.send(response.toByteArray());
        con.close();
    }

    public void addOnPlayerJoinHandler(PlayerJoinHandler handler) {
        this.joinHandlers.add(handler);
    }

    public void removeOnPlayerJoinHandler(PlayerJoinHandler handler) {
        this.joinHandlers.remove(handler);
    }
}
