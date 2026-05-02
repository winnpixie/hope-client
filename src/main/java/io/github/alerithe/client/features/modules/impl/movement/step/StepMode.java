package io.github.alerithe.client.features.modules.impl.movement.step;

import io.github.alerithe.client.events.game.EventPacket;
import io.github.alerithe.client.events.game.EventMoveUpdate;
import io.github.alerithe.client.features.modules.impl.movement.Step;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;

public class StepMode extends ObjectProperty.Value {
    protected final Step module;

    public StepMode(String name, String[] aliases, Step module) {
        super(name, aliases);

        this.module = module;
    }

    public void onPreUpdate(EventMoveUpdate.Pre event) {
    }

    public void onPacketWrite(EventPacket.Write event) {
    }
}
