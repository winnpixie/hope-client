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
        if (Wrapper.getGame().currentScreen == null) return;
        if (Wrapper.getGame().ingameGUI.getChatGUI().getChatOpen()) return;
        if (Wrapper.getGame().currentScreen instanceof GuiEditSign) return;
        if (Wrapper.getGame().currentScreen instanceof GuiScreenBook) return;

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
        KeyBinding.setKeyBindState(Wrapper.getSettings().keyBindForward,
                GameSettings.isKeyDown(Wrapper.getSettings().keyBindForward));
        // Back
        KeyBinding.setKeyBindState(Wrapper.getSettings().keyBindBack,
                GameSettings.isKeyDown(Wrapper.getSettings().keyBindBack));
        // Left
        KeyBinding.setKeyBindState(Wrapper.getSettings().keyBindLeft,
                GameSettings.isKeyDown(Wrapper.getSettings().keyBindLeft));
        // Right
        KeyBinding.setKeyBindState(Wrapper.getSettings().keyBindRight,
                GameSettings.isKeyDown(Wrapper.getSettings().keyBindRight));
        // Jump
        KeyBinding.setKeyBindState(Wrapper.getSettings().keyBindJump,
                GameSettings.isKeyDown(Wrapper.getSettings().keyBindJump));
        // Sneak
        KeyBinding.setKeyBindState(Wrapper.getSettings().keyBindSneak,
                GameSettings.isKeyDown(Wrapper.getSettings().keyBindSneak));
    }
}
