package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.events.impl.Subscribe;
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
    public void onDisable() {
        updateKeyStates();
    }

    @Subscribe
    private void onPreUpdate(EventUpdate.Pre event) {
        if (GameHelper.getGame().currentScreen == null) return;
        if (GameHelper.getGame().ingameGUI.getChatGUI().getChatOpen()) return;
        if (GameHelper.getGame().currentScreen instanceof GuiEditSign) return;
        if (GameHelper.getGame().currentScreen instanceof GuiScreenBook) return;

        updateKeyStates();

        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            EntityHelper.getUser().rotationPitch -= 5;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            EntityHelper.getUser().rotationPitch += 5;
        }
        EntityHelper.getUser().rotationPitch = MathHelper.clamp(EntityHelper.getUser().rotationPitch, -90, 90);
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            EntityHelper.getUser().rotationYaw -= 5;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            EntityHelper.getUser().rotationYaw += 5;
        }
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
        // Jump
        KeyBinding.setKeyBindState(GameHelper.getSettings().keyBindJump,
                GameSettings.isKeyDown(GameHelper.getSettings().keyBindJump));
        // Sneak
        KeyBinding.setKeyBindState(GameHelper.getSettings().keyBindSneak,
                GameSettings.isKeyDown(GameHelper.getSettings().keyBindSneak));
    }
}
