package io.github.alerithe.client.utilities;

import io.github.alerithe.client.extensions.LocalPlayer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.Packet;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.util.UUID;

/**
 * Using a wrapper just like its 2015 again
 */
public class Wrapper {
    public static Minecraft getGame() {
        return Minecraft.getMinecraft();
    }

    public static PlayerControllerMP getPlayerController() {
        return getGame().playerController;
    }

    public static GameSettings getSettings() {
        return getGame().gameSettings;
    }

    public static LocalPlayer getPlayer() {
        return getGame().player;
    }

    public static WorldClient getWorld() {
        return getGame().world;
    }

    public static NetworkPlayerInfo getNetInfo(UUID uuid) {
        return getPlayer().sendQueue.getPlayerInfo(uuid);
    }

    public static Block getBlock(BlockPos pos) {
        return getWorld().getBlockState(pos).getBlock();
    }

    public static void sendPacket(Packet<?> packet) {
        getPlayer().sendQueue.addToSendQueue(packet);
    }

    public static void printMessage(String message) {
        printMessage(new ChatComponentText(message));
    }

    public static void printMessage(IChatComponent component) {
        getGame().ingameGUI.getChatGUI().printChatMessage(component);
    }
}
