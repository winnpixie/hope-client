package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.events.impl.Subscribe;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class AutoJump extends Module {
    private final BooleanProperty withInput = new BooleanProperty("WithInput", new String[]{"input"}, true);

    public AutoJump() {
        super("AutoJump", new String[0], Type.MOVEMENT);

        getPropertyManager().add(withInput);
    }

    @Override
    public void onDisable() {
        KeyBinding.setKeyBindState(GameHelper.getSettings().keyBindJump,
                GameSettings.isKeyDown(GameHelper.getSettings().keyBindJump));
    }

    @Subscribe
    private void onPreUpdate(EventUpdate.Pre event) {
        KeyBinding.setKeyBindState(GameHelper.getSettings().keyBindJump,
                !withInput.getValue() || EntityHelper.getUser().isUserMoving());
    }
}
