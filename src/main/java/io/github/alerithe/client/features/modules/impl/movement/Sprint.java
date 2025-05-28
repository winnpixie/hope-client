package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.events.impl.Subscribe;
import net.minecraft.potion.Potion;

public class Sprint extends Module {
    private final BooleanProperty omniDir = new BooleanProperty("OmniDir", new String[]{"omni"}, true);

    public Sprint() {
        super("Sprint", new String[]{"autosprint"}, Type.MOVEMENT);
        getPropertyManager().add(omniDir);
    }

    @Subscribe
    private void onPreUpdate(EventUpdate.Pre event) {
        if (EntityHelper.getUser().isCollidedHorizontally) return;
        if (EntityHelper.getUser().getFoodStats().getFoodLevel() < 7) return;
        if (EntityHelper.getUser().isSneaking()) return;
        if (EntityHelper.getUser().isUsingItem()) return;
        if (EntityHelper.getUser().getActivePotionEffect(Potion.blindness) != null) return;

        if ((omniDir.getValue() && EntityHelper.getUser().isUserMoving())
                || EntityHelper.getUser().movementInput.moveForward > 0) {
            EntityHelper.getUser().setSprinting(true);
        }
    }
}
