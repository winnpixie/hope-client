package io.github.alerithe.client.events.client;

import io.github.alerithe.client.features.properties.Property;

public class PropertyModifiedEvent {
    private final Property<?> property;

    public PropertyModifiedEvent(Property<?> property) {
        this.property = property;
    }

    public Property<?> getProperty() {
        return property;
    }
}
