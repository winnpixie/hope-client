package io.github.alerithe.client.features.modules.impl.combat.aura;

import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.features.modules.impl.combat.KillAura;

public class Tick extends AuraMode {
    private int waitTicks;

    public Tick(KillAura module) {
        super("Tick", new String[0], module);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        module.target = null;

        if (module.near.isEmpty()) {
            waitTicks = 10;
            return;
        }

        if (waitTicks < 10) {
            waitTicks++;
        }

        module.target = module.near.get(0);
        if (module.target == null) return;
        if (waitTicks < 10) return;

        module.attacking = true;
        waitTicks = 0;
    }
}
