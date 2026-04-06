package io.github.alerithe.client.features.modules.impl.movement.sprint;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.impl.movement.AutoSprint;
import io.github.alerithe.client.utilities.GameHelper;
import net.minecraft.client.settings.KeyBinding;

public class Legit extends SprintMode {
    public Legit(AutoSprint module) {
        super("Legit", new String[0], module);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        KeyBinding.setKeyBindState(GameHelper.getSettings().keyBindSprint, true);
    }
}
