package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.game.EventPacket;
import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.NetworkHelper;
import io.github.alerithe.events.impl.Subscribe;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Step extends Module {
    private final BooleanProperty oneAndAHalf = new BooleanProperty("1.5", new String[0], false);
    private final BooleanProperty motion = new BooleanProperty("Motion", new String[0], false);

    private final double[] oneAndAHalfOffsets = {0.42, 0.75, 1.0000001, 1};
    private final double[] oneBlockOffsets = {0.42, 0.75};

    private float oldStepHeight;
    private int packetSkips;

    public Step() {
        super("Step", new String[0], Type.MOVEMENT);
        getPropertyManager().add(oneAndAHalf);
        getPropertyManager().add(motion);
    }

    @Override
    public void onEnable() {
        oldStepHeight = EntityHelper.getUser().stepHeight;
    }

    @Override
    public void onDisable() {
        EntityHelper.getUser().stepHeight = oldStepHeight;
        packetSkips = 0;
    }

    @Subscribe
    private void onPreUpdate(EventUpdate.Pre event) {
        EntityHelper.getUser().stepHeight = oldStepHeight;

        if (!EntityHelper.getUser().isCollidedHorizontally) return;
        if (EntityHelper.getUser().movementInput.jump) return;
        if (EntityHelper.getUser().isInLiquid()) return;

        if (motion.getValue()) {
            if (EntityHelper.getUser().onGround) {
                EntityHelper.getUser().motionY = 0.369;
            } else if (oneAndAHalf.getValue() && EntityHelper.getUser().fallDistance > 0) {
                EntityHelper.getUser().motionY = 0.26;
                EntityHelper.getUser().fallDistance = 0;
            }

            return;
        }

        if (EntityHelper.getUser().onGround) {
            EntityHelper.getUser().setSprinting(false);
            event.cancel();

            if (oneAndAHalf.getValue()) {
                for (double offset : oneAndAHalfOffsets) {
                    NetworkHelper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(EntityHelper.getUser().posX,
                            EntityHelper.getUser().posY + offset, EntityHelper.getUser().posZ, false));
                }

                EntityHelper.getUser().stepHeight = 1.5f;
                packetSkips = 8;
            } else {
                for (double offset : oneBlockOffsets) {
                    NetworkHelper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(EntityHelper.getUser().posX,
                            EntityHelper.getUser().posY + offset, EntityHelper.getUser().posZ, false));
                }

                EntityHelper.getUser().stepHeight = 1;
                packetSkips = 4;
            }
        }
    }

    private void onPacketWrite(EventPacket.Write event) {
        if (!(event.getPacket() instanceof C03PacketPlayer)) return;
        if (packetSkips < 1) return;

        if (packetSkips < 5) event.cancel();
        packetSkips--;
    }
}
