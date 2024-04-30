package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class InventoryMove extends Module {
    public InventoryMove() {
        super("InventoryMove", new String[]{"invmove", "screenwalk"}, Type.MOVEMENT);
    }

    @Override
    public void disable() {
        super.disable();

        updateKeyStates();
    }

    @Register
    private void onPreUpdate(EventUpdate.Pre event) {
        if (Wrapper.getMC().currentScreen == null) return;
        if (Wrapper.getMC().ingameGUI.getChatGUI().getChatOpen()) return;
        if (Wrapper.getMC().currentScreen instanceof GuiEditSign) return;
        if (Wrapper.getMC().currentScreen instanceof GuiScreenBook) return;

        updateKeyStates();

        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            Wrapper.getPlayer().rotationPitch -= 5;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            Wrapper.getPlayer().rotationPitch += 5;
        }
        Wrapper.getPlayer().rotationPitch = MathHelper.clamp(Wrapper.getPlayer().rotationPitch, -90, 90);
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            Wrapper.getPlayer().rotationYaw -= 5;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            Wrapper.getPlayer().rotationYaw += 5;
        }
    }

    private void updateKeyStates() {
        // Forward
        KeyBinding.setKeyBindState(Wrapper.getGameSettings().keyBindForward,
                GameSettings.isKeyDown(Wrapper.getGameSettings().keyBindForward));
        // Back
        KeyBinding.setKeyBindState(Wrapper.getGameSettings().keyBindBack,
                GameSettings.isKeyDown(Wrapper.getGameSettings().keyBindBack));
        // Left
        KeyBinding.setKeyBindState(Wrapper.getGameSettings().keyBindLeft,
                GameSettings.isKeyDown(Wrapper.getGameSettings().keyBindLeft));
        // Right
        KeyBinding.setKeyBindState(Wrapper.getGameSettings().keyBindRight,
                GameSettings.isKeyDown(Wrapper.getGameSettings().keyBindRight));
        // Jump
        KeyBinding.setKeyBindState(Wrapper.getGameSettings().keyBindJump,
                GameSettings.isKeyDown(Wrapper.getGameSettings().keyBindJump));
        // Sneak
        KeyBinding.setKeyBindState(Wrapper.getGameSettings().keyBindSneak,
                GameSettings.isKeyDown(Wrapper.getGameSettings().keyBindSneak));
    }
}
