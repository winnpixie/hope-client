package io.github.alerithe.client.features.properties.impl;

import io.github.alerithe.client.features.properties.Property;

public class BooleanProperty extends Property<Boolean> {
    public BooleanProperty(String name, String[] aliases, boolean value) {
        super(name, aliases, value);
    }
}
