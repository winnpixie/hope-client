package io.github.alerithe.client.features.modules.impl.player;

import io.github.alerithe.client.events.EventPacket;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;
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
    public void enable() {
        clone = new EntityOtherPlayerMP(Wrapper.getWorld(), Wrapper.getGame().getSession().getProfile());
        clone.setEntityId(-1337);
        clone.copyDataFromOld(Wrapper.getPlayer());
        clone.copyLocationAndAnglesFrom(Wrapper.getPlayer());
        clone.rotationYawHead = Wrapper.getPlayer().rotationYawHead;
        Wrapper.getWorld().addEntityToWorld(clone.getEntityId(), clone);

        startTime = System.currentTimeMillis();
        super.enable();
    }

    @Override
    public void disable() {
        super.disable();
        Wrapper.getWorld().removeEntityFromWorld(clone.getEntityId());
        clone = null;
        packetQueue.forEach(Wrapper::sendPacket);
        Wrapper.printMessage(String.format("%dms elapsed, %d packets captured.",
                System.currentTimeMillis() - startTime, packetQueue.size()));
        packetQueue.clear();
    }

    @Register
    private void onPacketWrite(EventPacket.Write event) {
        if (event.getPacket() instanceof C03PacketPlayer
                || event.getPacket() instanceof C02PacketUseEntity || event.getPacket() instanceof C0BPacketEntityAction
                || event.getPacket() instanceof C07PacketPlayerDigging
                || event.getPacket() instanceof C08PacketPlayerBlockPlacement) {
            packetQueue.add(event.getPacket());
            event.setCancelled(true);
        }
    }
}