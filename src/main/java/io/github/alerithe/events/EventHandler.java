package io.github.alerithe.events;

import java.lang.reflect.ParameterizedType;

public abstract class EventHandler<T> {
    private final byte priority;
    private Class<T> target;

    public EventHandler() {
        this(CallOrder.AVERAGE);
    }

    public EventHandler(byte priority) {
        this.priority = priority;
    }

    public byte getPriority() {
        return priority;
    }

    public Class<T> getTarget() {
        if (target == null) {
            target = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }

        return target;
    }

    public abstract void handle(T object);
}
