package io.github.alerithe.client.features.modules.impl.combat;

import io.github.alerithe.client.events.game.EventPacket;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.IntProperty;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.WorldHelper;
import io.github.alerithe.events.impl.Subscribe;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

public class SuperKnockback extends Module {
    private final IntProperty packets = new IntProperty("PacketsPerAttack", new String[]{"packets", "ppa"},
            100, 1, 1000);

    public SuperKnockback() {
        super("SuperKnockback", new String[]{"superkb"}, Type.COMBAT);

        getPropertyManager().add(packets);
    }

    @Subscribe
    private void onPacketWrite(EventPacket.Write event) {
        if (!(event.getPacket() instanceof C02PacketUseEntity)) return;

        C02PacketUseEntity packet = (C02PacketUseEntity) event.getPacket();
        if (packet.getAction() != C02PacketUseEntity.Action.ATTACK) return;
        if (!EntityHelper.getUser().onGround) return;
        if (!WorldHelper.getWorld()
                .getEntitiesWithinAABBExcludingEntity(EntityHelper.getUser(), EntityHelper.getUser().getEntityBoundingBox())
                .contains(packet.getEntityFromWorld(WorldHelper.getWorld()))) return;

        for (int i = 0; i < packets.getValue(); i++) {
            event.getNetworkManager().sendPacket(new C03PacketPlayer(true));
        }
    }
}