package io.github.alerithe.client.features.modules.impl.visual.storageesp;

import io.github.alerithe.client.events.game.EventDraw;
import io.github.alerithe.client.features.modules.impl.visual.StorageESP;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;

public class StorageESPMode extends ObjectProperty.Value {
    protected final StorageESP module;

    public StorageESPMode(String name, String[] aliases, StorageESP module) {
        super(name, aliases);

        this.module = module;
    }

    public void onWorldDraw(EventDraw.World event) {
    }

    public void onOverlayDraw(EventDraw.Overlay event) {
    }
}
