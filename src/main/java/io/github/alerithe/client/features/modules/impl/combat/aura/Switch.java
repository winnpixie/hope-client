package io.github.alerithe.client.features.modules.impl.combat.aura;

import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.features.modules.impl.combat.KillAura;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.Timer;

public class Switch extends AuraMode {
    private Timer timer = new Timer();
    private Timer switchTimer = new Timer();
    private int index;

    public Switch(KillAura module) {
        super("Switch", new String[0], module);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        module.target = null;
        if (module.near.isEmpty()) return;

        if (switchTimer.hasPassed(500)) {
            index++;
            switchTimer.update();
        }
        index %= module.near.size();
        module.target = module.near.get(index);

        if (!timer.hasPassed(1000 / (long) MathHelper.randomd(module.minAps.getValue(), module.maxAps.getValue())))
            return;

        module.attacking = true;
        timer.update();
    }
}
