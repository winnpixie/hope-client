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
        // This is the maximum move you can make in one tick to not trigger Bukkit's PlayerMoveEvent
        double maxMoveDelta = 1.0 / 16.1;
        float[] moveVec = EntityHelper.getUser().getLookVector();

        EntityHelper.getUser().motionY = 0.0;
        EntityHelper.getUser().setSpeed(0.0);
        double x = EntityHelper.getUser().posX + (moveVec[0] * maxMoveDelta);
        double y = EntityHelper.getUser().posY;
        double z = EntityHelper.getUser().posZ + (moveVec[1] * maxMoveDelta);
        NetworkHelper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
        EntityHelper.getUser().setPosition(x, y, z);
        NetworkHelper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, -301, z, false));
        event.cancel();
    }
}
