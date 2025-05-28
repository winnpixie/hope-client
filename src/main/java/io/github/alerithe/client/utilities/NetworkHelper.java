package io.github.alerithe.client.utilities;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;

import java.util.UUID;

public class NetworkHelper {
    public static int getPing() {
        return getPing(Minecraft.getInstance().player);
    }

    public static int getPing(EntityPlayer player) {
        NetworkPlayerInfo info = getInfo(player);
        return info == null ? -1 : info.getResponseTime();
    }

    public static int getPing(UUID id) {
        NetworkPlayerInfo netInfo = getInfo(id);
        return netInfo == null ? -1 : netInfo.getResponseTime();
    }

    public static NetworkPlayerInfo getInfo() {
        return getInfo(Minecraft.getInstance().player);
    }

    public static NetworkPlayerInfo getInfo(EntityPlayer player) {
        GameProfile profile = player.getGameProfile();
        if (profile == null) return getInfo(player.getUniqueID());

        return getInfo(profile.getId());
    }

    public static NetworkPlayerInfo getInfo(UUID id) {
        return Minecraft.getInstance().player.sendQueue.getPlayerInfo(id);
    }

    public static void sendPacket(Packet<?> packet) {
        Minecraft.getInstance().player.sendQueue.addToSendQueue(packet);
    }
}
