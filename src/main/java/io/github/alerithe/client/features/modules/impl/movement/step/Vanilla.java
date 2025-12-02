package io.github.alerithe.client.features.modules.impl.movement.step;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.impl.movement.Step;
import io.github.alerithe.client.utilities.EntityHelper;

public class Vanilla extends StepMode {
    public Vanilla(Step module) {
        super("Basic", new String[0], module);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        EntityHelper.getUser().stepHeight = 10f;
    }
}
