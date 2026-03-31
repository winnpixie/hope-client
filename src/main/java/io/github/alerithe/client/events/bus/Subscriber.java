package io.github.alerithe.client.events.bus;

public interface Subscriber<T> {
    void handle(T value);
}
