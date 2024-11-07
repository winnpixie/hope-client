package io.github.alerithe.client.features.properties.impl;

import io.github.alerithe.client.features.properties.Property;

public class NumberProperty<T extends Number> extends Property<T> {
    private final T minimum;
    private final T maximum;

    public NumberProperty(String name, String[] aliases, T value, T minimum, T maximum) {
        super(name, aliases, value);
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public T getMinimum() {
        return minimum;
    }

    public T getMaximum() {
        return maximum;
    }
}
