package plus.jboard.net.session;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import plus.jboard.net.NetworkMessage;

@Builder
@Getter
@Setter
public class SessionMessage implements NetworkMessage {

    private String message;

}
