package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.events.impl.Subscribe;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class AutoSprint extends Module {
    public AutoSprint() {
        super("AutoSprint", new String[]{"sprint"}, Type.MOVEMENT);
    }

    @Override
    public void onDisable() {
        KeyBinding.setKeyBindState(GameHelper.getSettings().keyBindSprint,
                GameSettings.isKeyDown(GameHelper.getSettings().keyBindSprint));
    }

    @Subscribe
    private void onPreUpdate(EventUpdate.Pre event) {
        if (!EntityHelper.getUser().isUserMoving()) return; // This isn't really necessary, but it makes me feel better.

        KeyBinding.setKeyBindState(GameHelper.getSettings().keyBindSprint, true);
    }
}
