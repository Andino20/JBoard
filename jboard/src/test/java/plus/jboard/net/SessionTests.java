package plus.jboard.net;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import plus.jboard.net.client.SessionClient;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

class SessionTests {

    private static final SessionEndpoint MOCK_ENDPOINT = SessionEndpoint.of("localhost", 25565);

    private MockSessionServer mockServer;
    private HostSettings defaultHostSettings;

    @BeforeEach
    void setupTest() {
        mockServer = new MockSessionServer();
        defaultHostSettings = HostSettings.builder()
                .id(UUID.fromString("9b0c2d4e-7f90-41fb-93d8-8f6342e96836"))
                .maxClients(2)
                .serverSupplier(() -> mockServer)
                .build();
    }

    @Test
    void hostAndJoinSessionTest() {
        HostSession host = new HostSession(defaultHostSettings);
        AtomicBoolean hasSuccessfullyJoined = new AtomicBoolean(false);
        host.addOnPlayerJoinHandler((UUID playerId) -> {
            Assertions.assertNotNull(playerId);
            hasSuccessfullyJoined.set(true);
        });
        host.start();

        MockSessionClient mockClient = new MockSessionClient(mockServer);
        mockClient.joinSession(MOCK_ENDPOINT).thenAccept(Assertions::assertNotNull);

        if (!hasSuccessfullyJoined.get()) {
            Assertions.fail("Expected client to join but they did not");
        }
    }

    @Test
    void joinFullSessionTest() {
        HostSession host = new HostSession(defaultHostSettings);
        host.start();

        List<SessionClient> clients = IntStream.range(0, defaultHostSettings.getMaxClients())
                .mapToObj(i -> (SessionClient) new MockSessionClient(mockServer))
                .toList();
        for (SessionClient c : clients) {
            c.joinSession(MOCK_ENDPOINT).thenAccept(Assertions::assertNotNull);
        }

        SessionClient oneTooMany = new MockSessionClient(mockServer);
        Assertions.assertThrows(SessionException.class, () -> oneTooMany.joinSession(MOCK_ENDPOINT));
    }

}
