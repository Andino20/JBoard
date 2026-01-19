package plus.jboard.net.async;

import lombok.extern.slf4j.Slf4j;
import plus.jboard.net.NetworkConnection;
import plus.jboard.net.NetworkEnvelope;
import plus.jboard.net.NetworkMessage;
import plus.jboard.net.handler.MessageHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class AsyncSocketConnection implements NetworkConnection {

    private final Socket socket;
    private final AtomicBoolean isRunning = new AtomicBoolean(true);
    private final ExecutorService ioThreadExecutor = Executors.newFixedThreadPool(2);

    private final LinkedBlockingQueue<NetworkMessage> sendQueue = new LinkedBlockingQueue<>();
    private final MessageHandler<NetworkMessage> messageHandler;

    public AsyncSocketConnection(Socket socket, MessageHandler<NetworkMessage> messageHandler) {
        this.socket = socket;
        this.messageHandler = messageHandler;

        ioThreadExecutor.submit(this::reader);
        ioThreadExecutor.submit(this::writer);
    }

    @Override
    public void send(NetworkMessage data) {
        if (data != null)
            sendQueue.add(data);
    }

    @Override
    public void close() {
        cleanUp();
    }

    private void reader() {
        try (InputStream input = socket.getInputStream();
             ObjectInputStream ois = new ObjectInputStream(input)) {
            readLoop(ois);
        } catch (IOException e) {
            if (isRunning.get()) {
                log.warn("Socket connection read error: {}", e.getMessage());
            }
        }
    }

    private void readLoop(ObjectInputStream ois) throws IOException {
        while (isRunning.get() && !socket.isClosed()) {
            try {
                Object o = ois.readObject();
                if (o instanceof NetworkMessage msg) {
                    messageHandler.handle(new NetworkEnvelope<>(this, msg));
                }
            } catch (ClassNotFoundException e) {
                log.error("Unknown object type received", e);
            }
        }
    }

    private void writer() {
        try (OutputStream out = socket.getOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(out)) {
            while (isRunning.get() && !socket.isClosed()) {
                NetworkMessage data = sendQueue.take();
                oos.writeObject(data);
                oos.flush();
            }
        } catch (IOException | InterruptedException e) {
            cleanUp();
            Thread.currentThread().interrupt();
        }
    }

    public void cleanUp() {
        isRunning.set(false);

        try {
            if (socket != null && !socket.isClosed()) {
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
            }
        } catch (IOException e) {
            log.error("Error during socket closure: {}", e.getMessage());
        }

        ioThreadExecutor.shutdown();
        log.info("Cleanup complete. Connection resources released.");
    }

}
