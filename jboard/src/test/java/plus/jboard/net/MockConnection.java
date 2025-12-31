package plus.jboard.net;

import lombok.Getter;
import lombok.Setter;
import plus.jboard.net.handler.ReceiveHandler;

@Setter
@Getter
public class MockConnection implements NetworkConnection {

    private MockConnection other;
    private ReceiveHandler receiver;

    public MockConnection() {
        other = null;
    }

    public MockConnection(MockConnection other) {
        this.other = other;
    }

    @Override
    public void send(byte[] data) {
        if (other != null)
            other.receive(data);
    }

    @Override
    public void setOnReceiveHandler(ReceiveHandler receiver) {
        this.receiver = receiver;
    }

    @Override
    public void close() {

    }

    private void receive(byte[] data) {
        receiver.receive(data);
    }
}
