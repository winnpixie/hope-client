package io.github.alerithe.events;

public interface IEventBus {
    <T> void subscribe(Class<T> event, ISubscriber<T> handler);

    <T> void unsubscribe(ISubscriber<T> handler);

    <T> T post(T value);
}
