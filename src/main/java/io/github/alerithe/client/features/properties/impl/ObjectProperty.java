package io.github.alerithe.client.features.properties.impl;

import io.github.alerithe.client.features.properties.Property;

import java.util.Arrays;
import java.util.List;

public class ObjectProperty<T extends ObjectProperty.Value> extends Property<T> {
    private final List<T> values;

    public ObjectProperty(String name, String[] aliases, T... values) {
        super(name, aliases, values[0]);
        this.values = Arrays.asList(values);
    }

    public List<T> getValues() {
        return values;
    }

    public T get(Class<T> cls) {
        for (T val : values) {
            if (val.getClass().equals(cls)) return val;
        }

        return null;
    }

    public T get(String handle) {
        for (T val : values) {
            if (val.getName().equalsIgnoreCase(handle)) return val;

            for (String alias : val.getAliases()) {
                if (alias.equalsIgnoreCase(handle)) return val;
            }
        }

        return null;
    }

    public static class Value {
        private final String name;
        private final String[] aliases;

        public Value(String name, String[] aliases) {
            this.name = name;
            this.aliases = aliases;
        }

        public String getName() {
            return name;
        }

        public String[] getAliases() {
            return aliases;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
