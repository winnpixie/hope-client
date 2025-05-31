package io.github.alerithe.client.features.modules.impl.player;

import io.github.alerithe.client.events.game.EventPacket;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.NetworkHelper;
import io.github.alerithe.client.utilities.WorldHelper;
import io.github.alerithe.events.impl.Subscribe;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;

import java.util.ArrayList;
import java.util.List;

public class Blink extends Module {
    private EntityOtherPlayerMP clone;
    private final List<Packet<?>> packetQueue = new ArrayList<>();
    private long startTime;

    public Blink() {
        super("Blink", new String[0], Type.PLAYER);
    }

    @Override
    public void onEnable() {
        clone = new EntityOtherPlayerMP(WorldHelper.getWorld(), GameHelper.getGame().getSession().getProfile());
        clone.setEntityId(-1337);
        clone.copyDataFromOld(EntityHelper.getUser());
        clone.copyLocationAndAnglesFrom(EntityHelper.getUser());
        clone.rotationYawHead = EntityHelper.getUser().rotationYawHead;
        WorldHelper.getWorld().addEntityToWorld(clone.getEntityId(), clone);

        startTime = System.currentTimeMillis();
    }

    @Override
    public void onDisable() {
        WorldHelper.getWorld().removeEntityFromWorld(clone.getEntityId());
        clone = null;
        packetQueue.forEach(NetworkHelper::sendPacket);
        GameHelper.printChatMessage(String.format("%dms elapsed, %d packets captured.",
                System.currentTimeMillis() - startTime, packetQueue.size()));
        packetQueue.clear();
    }

    @Subscribe
    private void onPacketWrite(EventPacket.Write event) {
        if (event.getPacket() instanceof C03PacketPlayer
                || event.getPacket() instanceof C02PacketUseEntity || event.getPacket() instanceof C0BPacketEntityAction
                || event.getPacket() instanceof C07PacketPlayerDigging
                || event.getPacket() instanceof C08PacketPlayerBlockPlacement) {
            packetQueue.add(event.getPacket());
            event.cancel();
        }
    }
}