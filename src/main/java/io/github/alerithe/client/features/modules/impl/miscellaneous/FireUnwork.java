package io.github.alerithe.client.features.modules.impl.miscellaneous;

import io.github.alerithe.client.events.game.EventPacket;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.events.Register;
import net.minecraft.item.ItemFirework;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

public class FireUnwork extends Module {
    public FireUnwork() {
        super("FireUnwork", new String[]{"firesploit", "fireworkdisplace"}, Type.MISCELLANEOUS);
    }

    @Register
    private void onPacketWrite(EventPacket.Write event) {
        if (!(event.getPacket() instanceof C08PacketPlayerBlockPlacement)) return;

        C08PacketPlayerBlockPlacement packet = (C08PacketPlayerBlockPlacement) event.getPacket();
        if (packet.getStack() == null) return;
        if (!(packet.getStack().getItem() instanceof ItemFirework)) return;

        event.setPacket(new C08PacketPlayerBlockPlacement(packet.getPosition(), packet.getPlacedBlockDirection(),
                packet.getStack(), packet.getPlacedBlockOffsetX() + MathHelper.getRandomInt(-16, 16),
                packet.getPlacedBlockOffsetY(), packet.getPlacedBlockOffsetZ() + MathHelper.getRandomInt(-16, 16)));
    }
}