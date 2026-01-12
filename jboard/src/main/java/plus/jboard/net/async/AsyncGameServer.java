package plus.jboard.net.async;

import lombok.extern.slf4j.Slf4j;
import plus.jboard.core.GameApplication;
import plus.jboard.net.NetworkConnection;
import plus.jboard.net.GameServer;
import plus.jboard.net.handler.MessageCollector;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@Slf4j
public class AsyncGameServer implements GameServer {

    private final int port;
    private final ExecutorService serverExecutor = Executors.newSingleThreadExecutor();

    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private Consumer<NetworkConnection> connectionHandler = NetworkConnection::close;

    public AsyncGameServer(int port) {
        this.port = port;
    }

    @Override
    public void onNewClient(Consumer<NetworkConnection> handler) {
        if (handler != null)
            this.connectionHandler = handler;
    }

    public void start() {
        serverExecutor.submit(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                log.info("Game server running at {}", serverSocket.getInetAddress());
                isRunning.set(true);
                serverLoop(serverSocket);
            } catch (IOException e) {
                log.error("Failed to create game server, {}", e.getMessage());
            }
        });
    }

    private void serverLoop(ServerSocket serverSocket) throws IOException {
        MessageCollector collector = GameApplication.getInstance().getMessageCollector();
        while (isRunning.get()) {
            Socket socket = serverSocket.accept();
            NetworkConnection connection = new AsyncSocketConnection(socket, collector);
            connectionHandler.accept(connection);
        }
    }

}
