package plus.jboard.net;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class NetworkMessageBase implements NetworkMessage {

    private UUID senderId;
    private transient NetworkConnection channel;

}
