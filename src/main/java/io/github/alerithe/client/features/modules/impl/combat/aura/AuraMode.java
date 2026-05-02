package io.github.alerithe.client.features.modules.impl.combat.aura;

import io.github.alerithe.client.events.game.EventMoveUpdate;
import io.github.alerithe.client.features.modules.impl.combat.KillAura;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;

public class AuraMode extends ObjectProperty.Value {
    protected final KillAura module;

    public AuraMode(String name, String[] aliases, KillAura module) {
        super(name, aliases);
        this.module = module;
    }

    public void onPreUpdate(EventMoveUpdate.Pre event) {
    }

    public void onPostUpdate(EventMoveUpdate.Post event) {
    }
}
