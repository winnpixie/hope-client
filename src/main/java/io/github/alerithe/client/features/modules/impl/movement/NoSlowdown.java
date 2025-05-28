package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.game.EventSlowdown;
import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.NetworkHelper;
import io.github.alerithe.events.impl.Subscribe;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.BlockWeb;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoSlowdown extends Module {
    private final BooleanProperty packets = new BooleanProperty("Packets", new String[0], true);

    private boolean block;

    public NoSlowdown() {
        super("NoSlowdown", new String[0], Type.MOVEMENT);

        getPropertyManager().add(packets);
    }

    @Subscribe
    private void onItemSlowdown(EventSlowdown.Item event) {
        event.setCancelled(true);
    }

    @Subscribe
    private void onEnvironmentSlowdown(EventSlowdown.Environment event) {
        if (event.getBlock() instanceof BlockWeb) event.setCancelled(true);
        if (event.getBlock() instanceof BlockSoulSand) event.setCancelled(true);
    }

    @Subscribe
    private void onPreUpdate(EventUpdate.Pre event) {
        if (!packets.getValue()) return;
        if (!EntityHelper.getUser().onGround) return;
        if (!EntityHelper.getUser().isUserMoving()) return;
        if (!EntityHelper.getUser().isBlocking()) return;

        NetworkHelper.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                BlockPos.ORIGIN, EnumFacing.DOWN));
        block = true;
    }

    @Subscribe
    private void onPostUpdate(EventUpdate.Post event) {
        if (!block) return;
        if (!EntityHelper.getUser().isBlocking()) return;

        NetworkHelper.sendPacket(new C08PacketPlayerBlockPlacement(EntityHelper.getUser().getHeldItem()));
        block = false;
    }
}
