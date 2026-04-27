package io.github.alerithe.client.utilities;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;

import java.util.UUID;

public class NetworkHelper {
    private NetworkHelper() {
    }

    public static int getPing() {
        return getPing(EntityHelper.getUser());
    }

    public static int getPing(EntityPlayer player) {
        NetworkPlayerInfo info = getInfo(player);
        if (info == null) {
            return -1;
        }

        return info.getResponseTime();
    }

    public static int getPing(UUID id) {
        NetworkPlayerInfo info = getInfo(id);
        if (info == null) {
            return -1;
        }

        return info.getResponseTime();
    }

    public static NetworkPlayerInfo getInfo() {
        return getInfo(EntityHelper.getUser());
    }

    public static NetworkPlayerInfo getInfo(EntityPlayer player) {
        GameProfile profile = player.getGameProfile();
        if (profile == null) {
            return getInfo(player.getUniqueID());
        }

        return getInfo(profile.getId());
    }

    public static NetworkPlayerInfo getInfo(UUID id) {
        return EntityHelper.getUser().sendQueue.getPlayerInfo(id);
    }

    public static void sendPacket(Packet<?> packet) {
        EntityHelper.getUser().sendQueue.addToSendQueue(packet);
    }
}
