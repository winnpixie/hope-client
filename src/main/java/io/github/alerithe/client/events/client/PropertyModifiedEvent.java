package io.github.alerithe.client.events.client;

import io.github.alerithe.client.events.bus.Event;
import io.github.alerithe.client.features.properties.Property;

public class PropertyModifiedEvent implements Event {
    private final Property<?> property;

    public PropertyModifiedEvent(Property<?> property) {
        this.property = property;
    }

    public Property<?> getProperty() {
        return property;
    }
}
