package io.github.alerithe.client.features.modules.impl.combat.aura;

import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.features.modules.impl.combat.KillAura;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.Timer;

public class LockOn extends AuraMode {
    private Timer timer = new Timer();

    public LockOn(KillAura module) {
        super("LockOn", new String[0], module);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        if (module.target != null && !module.qualifies(module.target)) module.target = null;

        if (!module.near.isEmpty() && module.target == null) module.target = module.near.get(0);

        if (module.target == null) return;
        if (!timer.hasPassed(1000 / (long) MathHelper.randomd(module.minAps.getValue(), module.maxAps.getValue())))
            return;

        module.attacking = true;
        timer.update();
    }
}
