package plus.jboard.net.dispatch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import plus.jboard.net.NetworkMessage;

@AllArgsConstructor
@Getter
public class SessionMessage implements NetworkMessage {
    private int id;
}
