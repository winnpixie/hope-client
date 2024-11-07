package io.github.alerithe.client.features.properties.impl;

import io.github.alerithe.client.features.properties.Property;

public class StringProperty extends Property<String> {
    public StringProperty(String name, String[] aliases, String value) {
        super(name, aliases, value);
    }
}
