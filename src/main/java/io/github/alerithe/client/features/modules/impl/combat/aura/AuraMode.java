package io.github.alerithe.client.features.modules.impl.combat.aura;

import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.features.modules.impl.combat.KillAura;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;

public class AuraMode extends ObjectProperty.Value {
    protected KillAura module;

    public AuraMode(String name, String[] aliases, KillAura module) {
        super(name, aliases);
        this.module = module;
    }

    public void onPreUpdate(EventUpdate.Pre event) {
    }

    public void onPostUpdate(EventUpdate.Post event) {
    }
}
