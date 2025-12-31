package plus.jboard.net;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import plus.jboard.net.server.SessionServer;

import java.util.UUID;
import java.util.function.Supplier;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostSettings {
    @Builder.Default
    private UUID id = UUID.randomUUID();

    private int maxClients;

    private Supplier<SessionServer> serverSupplier;
}
