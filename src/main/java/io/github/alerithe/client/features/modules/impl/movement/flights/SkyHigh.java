package io.github.alerithe.client.features.modules.impl.movement.flights;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.impl.movement.Flight;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.NetworkHelper;
import net.minecraft.network.play.client.C03PacketPlayer;

public class SkyHigh extends FlightMode {
    public SkyHigh(Flight module) {
        super("1.9", new String[]{"skyhigh"}, module);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        EntityHelper.getUser().motionY = 0;
        double x = EntityHelper.getUser().posX + EntityHelper.getUser().motionX * 11;
        double y = EntityHelper.getUser().posY
                + (EntityHelper.getUser().movementInput.jump ? 0.0625 : EntityHelper.getUser().movementInput.sneak ? -0.0625 : 0);
        double z = EntityHelper.getUser().posZ + EntityHelper.getUser().motionZ * 11;
        NetworkHelper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
        NetworkHelper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, EntityHelper.getUser().posY - 420, z, false));
    }
}
