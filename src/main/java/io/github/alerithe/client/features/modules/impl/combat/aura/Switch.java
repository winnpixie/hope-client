package io.github.alerithe.client.features.modules.impl.combat.aura;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.impl.combat.KillAura;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.Stopwatch;

public class Switch extends AuraMode {
    private final Stopwatch timer = new Stopwatch();
    private final Stopwatch switchTimer = new Stopwatch();
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

        if (!timer.hasPassed(1000 / MathHelper.getRandomInt(module.minAps.getValue(), module.maxAps.getValue())))
            return;

        module.attacking = true;
        timer.update();
    }
}
