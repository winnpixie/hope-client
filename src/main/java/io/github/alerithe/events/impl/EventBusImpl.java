package io.github.alerithe.events.impl;

import io.github.alerithe.events.EventBus;
import io.github.alerithe.events.Subscriber;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBusImpl implements EventBus {
    private static final Map<Object, ReflectSubscriber<?>[]> REFLECTION_CACHE = new HashMap<>();

    private final Map<Class<?>, List<Subscriber<?>>> REGISTRY = new HashMap<>();

    public void subscribeAll(Object parent) {
        if (REFLECTION_CACHE.containsKey(parent)) {
            for (ReflectSubscriber<?> subscriber : REFLECTION_CACHE.get(parent)) subscribeReflective(subscriber);
            return;
        }

        List<ReflectSubscriber<?>> reflectionCache = new ArrayList<>();

        for (Method method : parent.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Subscribe.class)) continue;
            if (method.getParameterCount() != 1) continue;

            if (!method.isAccessible()) method.setAccessible(true);

            MethodSubscriber methodSub = new MethodSubscriber(parent, method);
            subscribeReflective(methodSub);
            reflectionCache.add(methodSub);
        }

        for (Field field : parent.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(Subscribe.class)) continue;

            if (!field.isAccessible()) field.setAccessible(true);

            FieldSubscriber fieldSub = new FieldSubscriber(parent, field);
            subscribeReflective(fieldSub);
            reflectionCache.add(fieldSub);
        }

        REFLECTION_CACHE.put(parent, reflectionCache.toArray(new ReflectSubscriber[0]));
    }

    private <T> void subscribeReflective(ReflectSubscriber<T> subscriber) {
        subscribe(subscriber.getTarget(), subscriber.getSubscriber());
    }

    public <T> void subscribe(Class<T> event, Subscriber<T> subscriber) {
        REGISTRY.computeIfAbsent(event, v -> new CopyOnWriteArrayList<>())
                .add(subscriber);
    }

    public void unsubscribeAll(Object parent) {
        if (!REFLECTION_CACHE.containsKey(parent)) return;

        for (ReflectSubscriber<?> subscriber : REFLECTION_CACHE.get(parent))
            unsubscribe(subscriber.getSubscriber());
    }

    @Override
    public <T> void unsubscribe(Subscriber<T> subscriber) {
        REGISTRY.values().forEach(list -> list.remove(subscriber));
    }

    @Override
    public <T> T post(T value) {
        List<Subscriber<?>> list = REGISTRY.get(value.getClass());
        if (list == null || list.isEmpty()) return value;

        for (Subscriber<?> subscriber : list) ((Subscriber<T>) subscriber).handle(value);

        return value;
    }
}
