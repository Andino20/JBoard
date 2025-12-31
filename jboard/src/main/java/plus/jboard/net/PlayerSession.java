package plus.jboard.net;

import com.google.protobuf.InvalidProtocolBufferException;
import plus.jboard.net.protocol.session.ClientJoinRequest;
import plus.jboard.net.protocol.session.HandshakeEnvelope;

public class PlayerSession {
    private final NetworkConnection connection;

    public PlayerSession(NetworkConnection connection) {
        this.connection = connection;
        connection.setOnReceiveHandler(data -> {
            try {
                HandshakeEnvelope response = HandshakeEnvelope.parseFrom(data);
                switch (response.getPayloadCase()) {
                    case SERVER_ACCEPTED -> {
                        // TODO: somehow notify the user that this session is ready
                    }
                    case SERVER_REJECTED -> {
                            connection.close();
                            throw new SessionException("Host rejected: " + response.getServerRejected().getMessage());
                    }
                    default -> throw new SessionException("Unexpected error occurred while connecting to host");
                }

                connection.setOnReceiveHandler(null);
            } catch (InvalidProtocolBufferException e) {
                throw new SessionException("Session error: invalid server response");
            }
        });

        connection.send(HandshakeEnvelope.newBuilder()
                .setProtocolVersion(1)
                .setClientJoin(ClientJoinRequest.getDefaultInstance())
                .build().toByteArray());
    }

}
