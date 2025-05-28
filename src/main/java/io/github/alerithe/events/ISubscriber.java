package io.github.alerithe.events;

public interface ISubscriber<T> {
    void handle(T value);
}
