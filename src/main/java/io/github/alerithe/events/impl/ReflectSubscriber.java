package io.github.alerithe.events.impl;

import io.github.alerithe.events.Subscriber;

public interface ReflectSubscriber<T> {
    Class<T> getTarget();

    Subscriber<T> getSubscriber();
}
