package io.github.alerithe.events.impl;

import io.github.alerithe.events.Subscriber;

import java.lang.invoke.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class MethodSubscriber implements ReflectSubscriber {
    private static final Map<Class<?>, MethodHandles.Lookup> LOOKUP_CACHE = new HashMap<>();

    private final Class<?> target;
    private final Subscriber<?> subscriber;

    private MethodHandle handle;
    private BiConsumer<Object, Object> callback;

    public MethodSubscriber(Object parent, Method method) {
        this.target = method.getParameterTypes()[0];

        try {
            MethodHandles.Lookup lookup = getLookup(parent.getClass());
            this.handle = lookup.unreflect(method);

            CallSite site = LambdaMetafactory.metafactory(lookup,
                    "accept",
                    MethodType.methodType(BiConsumer.class),
                    MethodType.methodType(void.class, Object.class, Object.class),
                    handle,
                    handle.type());

            this.callback = (BiConsumer<Object, Object>) site.getTarget().invokeExact();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        this.subscriber = value -> {
            try {
                if (callback != null) {
                    callback.accept(parent, value);
                } else if (handle != null) {
                    handle.invoke(parent, value);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        };
    }

    @Override
    public Class<?> getTarget() {
        return target;
    }

    @Override
    public Subscriber<?> getSubscriber() {
        return subscriber;
    }

    private static MethodHandles.Lookup getLookup(Class<?> owner) {
        return LOOKUP_CACHE.computeIfAbsent(owner, v -> {
            try {
                Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
                constructor.setAccessible(true);
                return constructor.newInstance(owner, -1);
            } catch (Exception e) {
                e.printStackTrace();
                return MethodHandles.lookup();
            }
        });
    }
}
