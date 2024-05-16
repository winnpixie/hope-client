package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.EventSlowdown;
import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.Property;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.CallOrder;
import io.github.alerithe.events.Register;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.BlockWeb;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoSlowdown extends Module {
    private final Property<Boolean> packets = new Property<>("Packets", new String[0], true);

    private boolean block;

    public NoSlowdown() {
        super("NoSlowdown", new String[0], Type.MOVEMENT);

        getPropertyManager().add(packets);
    }

    @Register
    private void onItemSlowdown(EventSlowdown.Item event) {
        event.setCancelled(true);
    }

    @Register
    private void onEnvironmentSlowdown(EventSlowdown.Environment event) {
        if (event.getBlock() instanceof BlockWeb) event.setCancelled(true);
        if (event.getBlock() instanceof BlockSoulSand) event.setCancelled(true);
    }

    @Register(CallOrder.LAST)
    private void onPreUpdate(EventUpdate.Pre event) {
        if (!packets.getValue()) return;
        if (!Wrapper.getPlayer().onGround) return;
        if (!Wrapper.getPlayer().isUserMoving()) return;
        if (!Wrapper.getPlayer().isBlocking()) return;

        Wrapper.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                BlockPos.ORIGIN, EnumFacing.DOWN));
        block = true;
    }

    @Register(CallOrder.LAST)
    private void onPostUpdate(EventUpdate.Post event) {
        if (!block) return;
        if (!Wrapper.getPlayer().isBlocking()) return;

        Wrapper.sendPacket(new C08PacketPlayerBlockPlacement(Wrapper.getPlayer().getHeldItem()));
        block = false;
    }
}
