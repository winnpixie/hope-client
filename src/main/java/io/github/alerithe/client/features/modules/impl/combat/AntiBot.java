package io.github.alerithe.client.features.modules.impl.combat;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.IntProperty;
import io.github.alerithe.client.utilities.NetworkHelper;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;

public class AntiBot extends Module {
    private final IntProperty ticksExisted = new IntProperty("TicksExisted", new String[]{"ticks"},
            10, 0, 1200);

    public AntiBot() {
        super("AntiBot", new String[0], Type.COMBAT);

        getPropertyManager().add(ticksExisted);
    }

    public static boolean isBot(EntityPlayer player) {
        AntiBot antiBot = Client.MODULE_MANAGER.find(AntiBot.class);
        if (!antiBot.isEnabled()) return false;
        if (player.getGameProfile() == null) return true;

        NetworkPlayerInfo netInfo = NetworkHelper.getInfo(player.getGameProfile().getId());
        return netInfo == null
                || netInfo.getGameProfile() == null
                || player.ticksExisted < antiBot.ticksExisted.getValue();
    }
}
