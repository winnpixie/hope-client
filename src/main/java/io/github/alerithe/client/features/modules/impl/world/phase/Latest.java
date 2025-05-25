package io.github.alerithe.client.features.modules.impl.world.phase;

import io.github.alerithe.client.events.game.EventBlockCollision;
import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.utilities.Wrapper;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Latest extends PhaseMode {
    public Latest() {
        super("Latest", new String[0]);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        if (Wrapper.getPlayer().isUserMoving()) {
            Wrapper.getPlayer().setSprinting(false);
            float[] vector = Wrapper.getPlayer().getMoveVector();
            if (Wrapper.getPlayer().isCollidedHorizontally) { // THIS pushes you into the block
                double x = vector[0] * 0.006;
                double z = vector[1] * 0.006;
                Wrapper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.getPlayer().posX + x,
                        Wrapper.getPlayer().posY, Wrapper.getPlayer().posZ + z, false));
                Wrapper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.getPlayer().posX + x * 11,
                        Wrapper.getPlayer().posY - 11, Wrapper.getPlayer().posZ + z * 11, false));
                Wrapper.getPlayer().setPosition(Wrapper.getPlayer().posX + x, Wrapper.getPlayer().posY,
                        Wrapper.getPlayer().posZ + z);
            } else if (Wrapper.getPlayer().hurtTime == 9 || Wrapper.getPlayer().isSneaking()) { // THIS sends you through the block
                double x = vector[0] * 0.3;
                double z = vector[1] * 0.3;
                Wrapper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.getPlayer().posX + x,
                        Wrapper.getPlayer().posY, Wrapper.getPlayer().posZ + z, false));
                Wrapper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.getPlayer().posX + x * 11,
                        Wrapper.getPlayer().posY - 11, Wrapper.getPlayer().posZ + z * 11, false));
                Wrapper.getPlayer().setPosition(Wrapper.getPlayer().posX + x, Wrapper.getPlayer().posY,
                        Wrapper.getPlayer().posZ + z);
            }
        } else if (Wrapper.getPlayer().isSneaking()) { // DownClip because NCP is broken, lol.
            if (Wrapper.getBlock(Wrapper.getPlayer().getPosition().up()) instanceof BlockAir) {
                Wrapper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.getPlayer().posX,
                        Wrapper.getPlayer().posY - 1, Wrapper.getPlayer().posZ, true));
            } else if (Wrapper.getPlayer().ticksExisted % 4 == 0) {
                Wrapper.getPlayer().setPosition(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY - 0.7, Wrapper.getPlayer().posZ);
            }
        }
    }

    @Override
    public void onCollision(EventBlockCollision event) {
        if (Wrapper.getPlayer().isCollidedHorizontally) return;
        if (event.getBoundingBox() == null) return;
        if (event.getBoundingBox().minY < Wrapper.getPlayer().posY) return;

        event.setCancelled(true);
    }
}
