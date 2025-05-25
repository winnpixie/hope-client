package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.CallOrder;
import io.github.alerithe.events.Register;
import net.minecraft.potion.Potion;

public class Sprint extends Module {
    private final BooleanProperty omniDir = new BooleanProperty("OmniDir", new String[]{"omni"}, true);

    public Sprint() {
        super("Sprint", new String[]{"autosprint"}, Type.MOVEMENT);
        getPropertyManager().add(omniDir);
    }

    @Register(CallOrder.FIRST)
    private void onPreUpdate(EventUpdate.Pre event) {
        if (Wrapper.getPlayer().isCollidedHorizontally) return;
        if (Wrapper.getPlayer().getFoodStats().getFoodLevel() < 7) return;
        if (Wrapper.getPlayer().isSneaking()) return;
        if (Wrapper.getPlayer().isUsingItem()) return;
        if (Wrapper.getPlayer().getActivePotionEffect(Potion.blindness) != null) return;

        if ((omniDir.getValue() && Wrapper.getPlayer().isUserMoving())
                || Wrapper.getPlayer().movementInput.moveForward > 0) {
            Wrapper.getPlayer().setSprinting(true);
        }
    }
}
