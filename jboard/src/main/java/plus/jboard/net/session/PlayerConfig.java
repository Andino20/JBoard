package plus.jboard.net.session;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PlayerConfig {

    private String targetHost;
    private int targetPort;

}
