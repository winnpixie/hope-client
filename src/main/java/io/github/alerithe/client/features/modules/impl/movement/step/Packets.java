package io.github.alerithe.client.features.modules.impl.movement.step;

import io.github.alerithe.client.events.game.EventPacket;
import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.impl.movement.Step;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.NetworkHelper;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Packets extends StepMode {
    private final double[] oneAndAHalfOffsets = {0.42, 0.75, 1.0000001, 1};
    private final double[] oneBlockOffsets = {0.42, 0.75};

    private int updateSkips;

    public Packets(Step module) {
        super("Packets", new String[0], module);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        if (!EntityHelper.getUser().isCollidedHorizontally) return;
        if (EntityHelper.getUser().movementInput.jump) return;
        if (EntityHelper.getUser().isInLiquid()) return;
        if (!EntityHelper.getUser().onGround) return;

        EntityHelper.getUser().setSprinting(false);
        event.cancel();

        if (module.oneAndAHalf.getValue()) {
            for (double offset : oneAndAHalfOffsets) {
                NetworkHelper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(EntityHelper.getUser().posX,
                        EntityHelper.getUser().posY + offset, EntityHelper.getUser().posZ, false));
            }

            EntityHelper.getUser().stepHeight = 1.5f;
            updateSkips = 8;
        } else {
            for (double offset : oneBlockOffsets) {
                NetworkHelper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(EntityHelper.getUser().posX,
                        EntityHelper.getUser().posY + offset, EntityHelper.getUser().posZ, false));
            }

            EntityHelper.getUser().stepHeight = 1;
            updateSkips = 4;
        }
    }

    @Override
    public void onPacketWrite(EventPacket.Write event) {
        if (!(event.getPacket() instanceof C03PacketPlayer)) return;
        if (updateSkips < 1) return;

        if (updateSkips < 5) event.cancel();
        updateSkips--;
    }
}
