package io.github.alerithe.client.features.properties;

import io.github.alerithe.client.features.Feature;

public class Property<T> extends Feature {
    private T value;

    public Property(String name, String[] aliases, T value) {
        super(name, aliases);
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
