package plus.jboard.net.session.protocol;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ServerRejectMessage extends HandshakeMessage {

    private String reason;

}
