package io.github.alerithe.client.events.bus.reflect;

import io.github.alerithe.client.events.bus.Subscriber;

public interface ReflectSubscriber<T> {
    Class<T> getTarget();

    Subscriber<T> getSubscriber();
}
