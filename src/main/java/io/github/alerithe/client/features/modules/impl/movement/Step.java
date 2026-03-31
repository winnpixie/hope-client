package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.game.EventPacket;
import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.movement.step.*;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.events.bus.Subscribe;

public class Step extends Module {
    private final ObjectProperty<StepMode> mode = new ObjectProperty<>("Mode", new String[0],
            new PredictJump(this), new Packets(this), new Motion(this), new Vanilla(this));
    public final BooleanProperty oneAndAHalf = new BooleanProperty("1.5", new String[0], false);
    public final BooleanProperty motion = new BooleanProperty("Motion", new String[0], false);

    private float oldStepHeight;

    public Step() {
        super("Step", new String[0], Type.MOVEMENT);

        getPropertyManager().add(mode);
        getPropertyManager().add(oneAndAHalf);
    }

    @Override
    public void onEnable() {
        oldStepHeight = EntityHelper.getUser().stepHeight;
    }

    @Override
    public void onDisable() {
        EntityHelper.getUser().stepHeight = oldStepHeight;
    }

    @Subscribe
    private void onPreUpdate(EventUpdate.Pre event) {
        EntityHelper.getUser().stepHeight = oldStepHeight;

        mode.getValue().onPreUpdate(event);
    }

    private void onPacketWrite(EventPacket.Write event) {
        mode.getValue().onPacketWrite(event);
    }
}
