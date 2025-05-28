package io.github.alerithe.events.impl;

import io.github.alerithe.events.ISubscriber;

import java.lang.reflect.ParameterizedType;

public abstract class Subscriber<T> implements ISubscriber<T> {
    private Class<T> targetClass;

    public Class<T> getTargetClass() {
        if (targetClass == null) {
            targetClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }

        return targetClass;
    }
}
