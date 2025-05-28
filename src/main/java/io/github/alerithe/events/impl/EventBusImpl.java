package io.github.alerithe.events.impl;

import io.github.alerithe.events.IEventBus;
import io.github.alerithe.events.ISubscriber;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBusImpl implements IEventBus {
    private static final Map<Object, Subscriber<?>[]> CACHE = new HashMap<>();

    private final Map<Class<?>, List<ISubscriber<?>>> REGISTRY = new HashMap<>();

    public void subscribe(Object parent) {
        if (CACHE.containsKey(parent)) {
            for (Subscriber<?> handler : CACHE.get(parent)) subscribe(handler);
            return;
        }

        List<Subscriber<?>> cache = new ArrayList<>();

        for (Method method : parent.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Subscribe.class)) continue;

            if (!method.isAccessible()) method.setAccessible(true);

            MethodSubscriber methodSubscriber = new MethodSubscriber(parent, method);
            subscribe(methodSubscriber);
            cache.add(methodSubscriber);
        }

        for (Field field : parent.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(Subscribe.class)) continue;

            if (!field.isAccessible()) field.setAccessible(true);

            try {
                Subscriber<?> handler = (Subscriber<?>) field.get(parent);
                subscribe(handler);
                cache.add(handler);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        CACHE.put(parent, cache.toArray(new Subscriber<?>[0]));
    }

    public <T> void subscribe(Subscriber<T> handler) {
        subscribe(handler.getTargetClass(), handler);
    }

    public <T> void subscribe(Class<T> event, ISubscriber<T> handler) {
        REGISTRY.computeIfAbsent(event, v -> new CopyOnWriteArrayList<>())
                .add(handler);
    }

    public void unsubscribe(Object parent) {
        if (!CACHE.containsKey(parent)) return;

        for (Subscriber<?> handler : CACHE.get(parent)) unsubscribe(handler);
    }

    @Override
    public <T> void unsubscribe(ISubscriber<T> handler) {
        REGISTRY.values().forEach(list -> list.remove(handler));
    }

    @Override
    public <T> T post(T value) {
        List<ISubscriber<?>> list = REGISTRY.get(value.getClass());
        if (list == null || list.isEmpty()) return value;

        for (ISubscriber<?> handler : list) ((ISubscriber<T>) handler).handle(value);

        return value;
    }
}
