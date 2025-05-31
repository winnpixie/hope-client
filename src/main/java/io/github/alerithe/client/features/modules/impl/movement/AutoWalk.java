package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.events.impl.Subscribe;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class AutoWalk extends Module {
    private final BooleanProperty forwards = new BooleanProperty("Forwards", new String[0], true);
    private final BooleanProperty backwards = new BooleanProperty("Backwards", new String[0], false);
    private final BooleanProperty left = new BooleanProperty("Left", new String[0], false);
    private final BooleanProperty right = new BooleanProperty("Right", new String[0], false);

    public AutoWalk() {
        super("AutoWalk", new String[]{"automove"}, Type.MOVEMENT);

        getPropertyManager().add(forwards);
        getPropertyManager().add(backwards);
        getPropertyManager().add(left);
        getPropertyManager().add(right);
    }

    @Override
    public void onDisable() {
        updateKeyStates();
    }

    @Subscribe
    private void onPreUpdate(EventUpdate.Pre event) {
        if (forwards.getValue()) KeyBinding.setKeyBindState(GameHelper.getSettings().keyBindForward, true);

        if (backwards.getValue()) KeyBinding.setKeyBindState(GameHelper.getSettings().keyBindBack, true);

        if (left.getValue()) KeyBinding.setKeyBindState(GameHelper.getSettings().keyBindLeft, true);

        if (right.getValue()) KeyBinding.setKeyBindState(GameHelper.getSettings().keyBindRight, true);
    }

    private void updateKeyStates() {
        // Forward
        KeyBinding.setKeyBindState(GameHelper.getSettings().keyBindForward,
                GameSettings.isKeyDown(GameHelper.getSettings().keyBindForward));
        // Back
        KeyBinding.setKeyBindState(GameHelper.getSettings().keyBindBack,
                GameSettings.isKeyDown(GameHelper.getSettings().keyBindBack));
        // Left
        KeyBinding.setKeyBindState(GameHelper.getSettings().keyBindLeft,
                GameSettings.isKeyDown(GameHelper.getSettings().keyBindLeft));
        // Right
        KeyBinding.setKeyBindState(GameHelper.getSettings().keyBindRight,
                GameSettings.isKeyDown(GameHelper.getSettings().keyBindRight));
    }
}
