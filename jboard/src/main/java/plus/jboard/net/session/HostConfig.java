package plus.jboard.net.session;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HostConfig {
    private int port;
    private int maxClients;
}
