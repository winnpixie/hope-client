package io.github.alerithe.client.features.modules.impl.movement.sprint;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.impl.movement.AutoSprint;
import io.github.alerithe.client.utilities.EntityHelper;
import net.minecraft.potion.Potion;

public class Custom extends SprintMode {
    public Custom(AutoSprint module) {
        super("Custom", new String[0], module);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        if (EntityHelper.getUser().isCollidedHorizontally && !module.ignoreWalls.getValue()) return;
        if (EntityHelper.getUser().getFoodStats().getFoodLevel() < 7 && !module.ignoreHunger.getValue()) return;
        if (EntityHelper.getUser().isPotionActive(Potion.blindness) && !module.ignoreBlindness.getValue()) return;

        if (EntityHelper.getUser().moveForward > 0f) {
            EntityHelper.getUser().setSprinting(true);
        }
    }
}
