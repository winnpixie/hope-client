package io.github.alerithe.events;

public interface Subscriber<T> {
    void handle(T value);
}
