package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Step extends Module {
    private final BooleanProperty oneAndAHalf = new BooleanProperty("1.5", new String[0], false);
    private final BooleanProperty motion = new BooleanProperty("Motion", new String[0], false);

    private final double[] oneAndAHalfOffsets = {0.42, 0.75, 1.0000001, 1};
    private final double[] oneBlockOffsets = {0.42, 0.75};

    private float oldStepHeight;

    public Step() {
        super("Step", new String[0], Type.MOVEMENT);
        getPropertyManager().add(oneAndAHalf);
        getPropertyManager().add(motion);
    }

    @Override
    public void onEnable() {
        oldStepHeight = Wrapper.getPlayer().stepHeight;
    }

    @Override
    public void onDisable() {
        Wrapper.getPlayer().stepHeight = oldStepHeight;
    }

    @Register
    private void onPreUpdate(EventUpdate.Pre event) {
        Wrapper.getPlayer().stepHeight = oldStepHeight;

        if (!Wrapper.getPlayer().isCollidedHorizontally) return;
        if (Wrapper.getPlayer().movementInput.jump) return;
        if (Wrapper.getPlayer().isInLiquid()) return;

        if (motion.getValue()) {
            if (Wrapper.getPlayer().onGround) {
                Wrapper.getPlayer().motionY = 0.369;
            } else if (oneAndAHalf.getValue() && Wrapper.getPlayer().fallDistance > 0) {
                Wrapper.getPlayer().motionY = 0.26;
                Wrapper.getPlayer().fallDistance = 0;
            }

            return;
        }

        if (Wrapper.getPlayer().onGround) {
            Wrapper.getPlayer().setSprinting(false);
            event.setCancelled(true);

            if (oneAndAHalf.getValue()) {
                for (double offset : oneAndAHalfOffsets) {
                    Wrapper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.getPlayer().posX,
                            Wrapper.getPlayer().posY + offset, Wrapper.getPlayer().posZ, false));
                }

                Wrapper.getPlayer().stepHeight = 1.5f;

                new Thread(() -> {
                    Wrapper.getGame().timer.timerSpeed = 0f;

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Wrapper.getGame().timer.timerSpeed = 1f;
                }).start();
            } else {
                for (double offset : oneBlockOffsets) {
                    Wrapper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.getPlayer().posX,
                            Wrapper.getPlayer().posY + offset, Wrapper.getPlayer().posZ, false));
                }

                Wrapper.getPlayer().stepHeight = 1;

                new Thread(() -> {
                    Wrapper.getGame().timer.timerSpeed = 0f;

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Wrapper.getGame().timer.timerSpeed = 1f;
                }).start();
            }
        }
    }
}
