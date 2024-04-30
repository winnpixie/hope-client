package io.github.alerithe.client.features.modules.impl.player;

import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemShears;
import net.minecraft.network.play.client.C02PacketUseEntity;

public class AutoShear extends Module {
    public AutoShear() {
        super("AutoShear", new String[0], Type.PLAYER);
    }

    @Register
    private void onPreUpdate(EventUpdate.Pre event) {
        if (Wrapper.getPlayer().getHeldItem() == null) return;
        if (!(Wrapper.getPlayer().getHeldItem().getItem() instanceof ItemShears)) return;

        for (Entity entity : Wrapper.getWorld().loadedEntityList) {
            if (!(entity instanceof EntitySheep)) continue;
            if (entity.getDistanceSqToEntity(Wrapper.getPlayer()) > 36) continue;

            EntitySheep sheep = (EntitySheep) entity;
            if (sheep.getSheared()) continue;

            Wrapper.sendPacket(new C02PacketUseEntity(sheep, C02PacketUseEntity.Action.INTERACT));
        }
    }
}
