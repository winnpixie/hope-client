package io.github.alerithe.client.events.bus;

import io.github.alerithe.client.events.bus.reflect.FieldSubscriber;
import io.github.alerithe.client.events.bus.reflect.MethodSubscriber;
import io.github.alerithe.client.events.bus.reflect.ReflectSubscriber;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {
    private static final Map<Object, List<ReflectSubscriber<?>>> REFLECT_CACHE = new ConcurrentHashMap<>();

    private final Map<Class<?>, List<Subscriber<?>>> registry = new ConcurrentHashMap<>();

    public void subscribeAll(Object parent) {
        List<ReflectSubscriber<?>> existingCache = REFLECT_CACHE.get(parent);
        if (existingCache != null) {
            for (ReflectSubscriber<?> subscriber : existingCache) {
                subscribeReflective(subscriber);
            }

            return;
        }

        List<ReflectSubscriber<?>> newCache = new ArrayList<>();

        for (Method method : parent.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Subscribe.class)
                    || method.getParameterCount() != 1) {
                continue;
            }

            MethodSubscriber methodSub = new MethodSubscriber(parent, method);
            subscribeReflective(methodSub);
            newCache.add(methodSub);
        }

        for (Field field : parent.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(Subscribe.class)) {
                continue;
            }

            FieldSubscriber fieldSub = new FieldSubscriber(parent, field);
            subscribeReflective(fieldSub);
            newCache.add(fieldSub);
        }

        REFLECT_CACHE.put(parent, newCache);
    }

    private <T> void subscribeReflective(ReflectSubscriber<T> subscriber) {
        subscribe(subscriber.getTarget(), subscriber.getSubscriber());
    }

    public <T> void subscribe(Class<T> event, Subscriber<T> subscriber) {
        registry.computeIfAbsent(event, k -> new CopyOnWriteArrayList<>())
                .add(subscriber);
    }

    public void unsubscribeAll(Object parent) {
        List<ReflectSubscriber<?>> cache = REFLECT_CACHE.get(parent);
        if (cache == null) return;

        for (ReflectSubscriber<?> subscriber : cache) {
            unsubscribe(subscriber.getSubscriber());
        }
    }

    public <T> void unsubscribe(Subscriber<T> subscriber) {
        for (List<Subscriber<?>> subscribers : registry.values()) {
            subscribers.remove(subscriber);
        }
    }

    public <T> T post(T value) {
        List<Subscriber<?>> list = registry.get(value.getClass());
        if (list == null) {
            return value;
        }

        for (Subscriber<?> subscriber : list) {
            ((Subscriber<T>) subscriber).handle(value);
        }

        return value;
    }
}
