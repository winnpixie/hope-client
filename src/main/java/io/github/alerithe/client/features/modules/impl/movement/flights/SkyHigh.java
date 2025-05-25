package io.github.alerithe.client.features.modules.impl.movement.flights;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.impl.movement.Flight;
import io.github.alerithe.client.utilities.Wrapper;
import net.minecraft.network.play.client.C03PacketPlayer;

public class SkyHigh extends FlightMode {
    public SkyHigh(Flight module) {
        super("1.9", new String[]{"skyhigh"}, module);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        Wrapper.getPlayer().motionY = 0;
        double x = Wrapper.getPlayer().posX + Wrapper.getPlayer().motionX * 11;
        double y = Wrapper.getPlayer().posY
                + (Wrapper.getPlayer().movementInput.jump ? 0.0625 : Wrapper.getPlayer().movementInput.sneak ? -0.0625 : 0);
        double z = Wrapper.getPlayer().posZ + Wrapper.getPlayer().motionZ * 11;
        Wrapper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
        Wrapper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, Wrapper.getPlayer().posY - 420, z, false));
    }
}
