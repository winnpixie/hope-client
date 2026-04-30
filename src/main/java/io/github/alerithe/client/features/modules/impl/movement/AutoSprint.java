package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.bus.Subscribe;
import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.movement.sprint.Custom;
import io.github.alerithe.client.features.modules.impl.movement.sprint.Legit;
import io.github.alerithe.client.features.modules.impl.movement.sprint.SprintMode;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;
import io.github.alerithe.client.utilities.GameHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class AutoSprint extends Module {
    private final ObjectProperty<SprintMode> mode = new ObjectProperty<>("Mode", new String[0],
            new Legit(this), new Custom(this));
    public final BooleanProperty ignoreHunger = new BooleanProperty("IgnoreHunger", new String[]{"hunger"}, true);
    public final BooleanProperty ignoreWalls = new BooleanProperty("IgnoreWalls", new String[]{"walls"}, true);
    public final BooleanProperty ignoreBlindness = new BooleanProperty("IgnoreBlindness", new String[]{"blindness"}, true);

    public AutoSprint() {
        super("AutoSprint", new String[]{"sprint"}, Type.MOVEMENT);

        getPropertyManager().add(mode);
        getPropertyManager().add(ignoreHunger);
        getPropertyManager().add(ignoreWalls);
        getPropertyManager().add(ignoreBlindness);
    }

    @Override
    public void onDisable() {
        KeyBinding.setKeyBindState(GameHelper.getSettings().keyBindSprint.getKeyCode(),
                GameSettings.isKeyDown(GameHelper.getSettings().keyBindSprint));
    }

    @Subscribe
    public void onPreUpdate(EventUpdate.Pre event) {
        mode.getValue().onPreUpdate(event);
    }
}
