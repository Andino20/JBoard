package plus.jboard.net;

public record NetworkEnvelope<T extends NetworkMessage>(NetworkConnection channel, T message) {
}
