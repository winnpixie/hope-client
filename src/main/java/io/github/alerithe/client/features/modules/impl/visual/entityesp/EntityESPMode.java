package io.github.alerithe.client.features.modules.impl.visual.entityesp;

import io.github.alerithe.client.events.game.EventDraw;
import io.github.alerithe.client.features.modules.impl.visual.EntityESP;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;

public class EntityESPMode extends ObjectProperty.Value {
    protected EntityESP module;

    public EntityESPMode(String name, String[] aliases, EntityESP module) {
        super(name, aliases);

        this.module = module;
    }

    public void onOverlayDraw(EventDraw.Overlay event) {
    }

    public void onWorldDraw(EventDraw.World event) {
    }
}
