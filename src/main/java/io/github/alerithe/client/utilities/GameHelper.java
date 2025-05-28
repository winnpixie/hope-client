package io.github.alerithe.client.utilities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class GameHelper {
    public static Minecraft getGame() {
        return Minecraft.getInstance();
    }

    public static PlayerControllerMP getController() {
        return getGame().playerController;
    }

    public static GameSettings getSettings() {
        return getGame().gameSettings;
    }

    public static void printChatMessage(String message) {
        printChatMessage(new ChatComponentText(message));
    }

    public static void printChatMessage(IChatComponent component) {
        getGame().ingameGUI.getChatGUI().printChatMessage(component);
    }
}
