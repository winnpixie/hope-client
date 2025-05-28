package io.github.alerithe.client.features.modules.impl.player;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.NetworkHelper;
import io.github.alerithe.client.utilities.WorldHelper;
import io.github.alerithe.events.impl.Subscribe;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemShears;
import net.minecraft.network.play.client.C02PacketUseEntity;

public class AutoShear extends Module {
    public AutoShear() {
        super("AutoShear", new String[0], Type.PLAYER);
    }

    @Subscribe
    private void onPreUpdate(EventUpdate.Pre event) {
        if (EntityHelper.getUser().getHeldItem() == null) return;
        if (!(EntityHelper.getUser().getHeldItem().getItem() instanceof ItemShears)) return;

        for (Entity entity : WorldHelper.getWorld().loadedEntityList) {
            if (!(entity instanceof EntitySheep)) continue;
            if (entity.getDistanceSqToEntity(EntityHelper.getUser()) > 36) continue;

            EntitySheep sheep = (EntitySheep) entity;
            if (sheep.getSheared()) continue;

            NetworkHelper.sendPacket(new C02PacketUseEntity(sheep, C02PacketUseEntity.Action.INTERACT));
        }
    }
}
