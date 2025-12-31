package plus.jboard.net;

import java.net.InetSocketAddress;

public record SessionEndpoint(InetSocketAddress address) {

    public static SessionEndpoint of(String host, int port) {
        return new SessionEndpoint(new InetSocketAddress(host, port));
    }

}
