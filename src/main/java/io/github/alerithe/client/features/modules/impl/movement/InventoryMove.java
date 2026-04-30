package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.bus.Subscribe;
import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.DoubleProperty;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.MathHelper;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class InventoryMove extends Module {
    private final DoubleProperty yawSpeed = new DoubleProperty("YawSpeed", new String[]{"yaw"},
            5.0, 0.1, 1.0);
    private final DoubleProperty pitchSpeed = new DoubleProperty("PitchSpeed", new String[]{"pitch"},
            5.0, 0.1, 1.0);

    public InventoryMove() {
        super("InventoryMove", new String[]{"invmove", "screenwalk"}, Type.MOVEMENT);

        getPropertyManager().add(yawSpeed);
        getPropertyManager().add(pitchSpeed);
    }

    @Override
    public void onDisable() {
        updateKeyStates();
    }

    @Subscribe
    public void onPreUpdate(EventUpdate.Pre event) {
        if (GameHelper.getGame().currentScreen == null) return;
        if (GameHelper.getGame().ingameGUI.getChatGUI().getChatOpen()) return;
        if (GameHelper.getGame().currentScreen instanceof GuiEditSign) return;
        if (GameHelper.getGame().currentScreen instanceof GuiScreenBook) return;

        updateKeyStates();

        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            EntityHelper.getUser().rotationPitch -= pitchSpeed.getValue().floatValue();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            EntityHelper.getUser().rotationPitch += pitchSpeed.getValue().floatValue();
        }
        EntityHelper.getUser().rotationPitch = MathHelper.clamp(EntityHelper.getUser().rotationPitch, -90, 90);
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            EntityHelper.getUser().rotationYaw -= yawSpeed.getValue().floatValue();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            EntityHelper.getUser().rotationYaw += yawSpeed.getValue().floatValue();
        }
    }

    private void updateKeyStates() {
        // Forward
        KeyBinding.setKeyBindState(GameHelper.getSettings().keyBindForward.getKeyCode(),
                GameSettings.isKeyDown(GameHelper.getSettings().keyBindForward));
        // Back
        KeyBinding.setKeyBindState(GameHelper.getSettings().keyBindBack.getKeyCode(),
                GameSettings.isKeyDown(GameHelper.getSettings().keyBindBack));
        // Left
        KeyBinding.setKeyBindState(GameHelper.getSettings().keyBindLeft.getKeyCode(),
                GameSettings.isKeyDown(GameHelper.getSettings().keyBindLeft));
        // Right
        KeyBinding.setKeyBindState(GameHelper.getSettings().keyBindRight.getKeyCode(),
                GameSettings.isKeyDown(GameHelper.getSettings().keyBindRight));
        // Jump
        KeyBinding.setKeyBindState(GameHelper.getSettings().keyBindJump.getKeyCode(),
                GameSettings.isKeyDown(GameHelper.getSettings().keyBindJump));
        // Sneak
        KeyBinding.setKeyBindState(GameHelper.getSettings().keyBindSneak.getKeyCode(),
                GameSettings.isKeyDown(GameHelper.getSettings().keyBindSneak));
    }
}
