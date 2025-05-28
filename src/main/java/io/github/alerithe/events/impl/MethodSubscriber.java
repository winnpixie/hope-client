package io.github.alerithe.events.impl;

import java.lang.invoke.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class MethodSubscriber extends Subscriber {
    private static final Map<Class<?>, MethodHandles.Lookup> LOOKUP_CACHE = new HashMap<>();

    private final Object parent;
    private final Class<?> target;

    private MethodHandle handle;
    private BiConsumer<Object, Object> callback;

    public MethodSubscriber(Object parent, Method method) {
        this.parent = parent;
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
    }

    @Override
    public Class<?> getTargetClass() {
        return target;
    }

    @Override
    public void handle(Object object) {
        try {
            if (callback != null) {
                callback.accept(parent, object);
            } else if (handle != null) {
                handle.invoke(parent, object);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
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
