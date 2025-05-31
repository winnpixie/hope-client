package io.github.alerithe.events;

public interface EventBus {
    <T> void subscribe(Class<T> event, Subscriber<T> handler);

    <T> void unsubscribe(Subscriber<T> handler);

    <T> T post(T value);
}
