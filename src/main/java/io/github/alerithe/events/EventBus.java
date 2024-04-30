package io.github.alerithe.events;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {
    private static final Map<Class<?>, List<EventHandler<?>>> registry = new HashMap<>();
    private static final Map<Object, List<EventHandler<?>>> cache = new HashMap<>();
    private static final Comparator<EventHandler<?>> sorter = Comparator.comparingInt(EventHandler::getPriority);

    public static void register(Object parent) {
        if (cache.containsKey(parent)) {
            cache.get(parent).forEach(EventBus::register);
            return;
        }

        List<EventHandler<?>> list = cache.computeIfAbsent(parent, v -> new CopyOnWriteArrayList<>());

        for (Method method : parent.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Register.class)) continue;

            if (!method.isAccessible()) method.setAccessible(true);

            MethodHandler eventHandler = new MethodHandler(parent, method);
            register(eventHandler);
            list.add(eventHandler);
        }

        for (Field field : parent.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(Register.class)) continue;

            if (!field.isAccessible()) field.setAccessible(true);

            try {
                EventHandler<?> eventHandler = (EventHandler<?>) field.get(parent);
                register(eventHandler);
                list.add(eventHandler);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static void register(EventHandler<?> eventHandler) {
        List<EventHandler<?>> list = registry.computeIfAbsent(eventHandler.getTarget(), v -> new CopyOnWriteArrayList<>());
        list.add(eventHandler);
        list.sort(sorter);
    }

    public static void unregister(Object parent) {
        if (cache.containsKey(parent)) cache.get(parent).forEach(EventBus::unregister);
    }

    public static void unregister(EventHandler<?> eventHandler) {
        registry.values().forEach(list -> list.remove(eventHandler));
    }

    public static <T> T dispatch(T value) {
        List<EventHandler<?>> list = registry.get(value.getClass());
        if (list == null || list.isEmpty()) return value;

        for (EventHandler<?> handler : list) {
            ((EventHandler<T>) handler).handle(value);
        }

        return value;
    }
}
