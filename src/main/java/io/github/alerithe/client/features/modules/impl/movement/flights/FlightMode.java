package io.github.alerithe.client.features.modules.impl.movement.flights;

import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.features.modules.impl.movement.Flight;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;

public class FlightMode extends ObjectProperty.Value {
    protected Flight module;

    public FlightMode(String name, String[] aliases, Flight module) {
        super(name, aliases);
        this.module = module;
    }

    public void onPreUpdate(EventUpdate.Pre event) {
    }
}
