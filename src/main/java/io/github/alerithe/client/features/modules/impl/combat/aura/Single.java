package io.github.alerithe.client.features.modules.impl.combat.aura;

import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.features.modules.impl.combat.KillAura;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.MsTimer;

public class Single extends AuraMode {
    private final MsTimer timer = new MsTimer();

    public Single(KillAura module) {
        super("Single", new String[0], module);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        module.target = null;
        if (module.near.isEmpty()) return;

        module.target = module.near.get(0);

        if (!timer.hasPassed(1000 / MathHelper.getRandomInt(module.minAps.getValue(), module.maxAps.getValue())))
            return;

        module.attacking = true;
        timer.update();
    }
}
