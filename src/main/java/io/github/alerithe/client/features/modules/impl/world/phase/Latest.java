package io.github.alerithe.client.features.modules.impl.world.phase;

import io.github.alerithe.client.events.game.EventBlockCollision;
import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.NetworkHelper;
import io.github.alerithe.client.utilities.WorldHelper;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Latest extends PhaseMode {
    public Latest() {
        super("Latest", new String[0]);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        if (EntityHelper.getUser().isUserMoving()) {
            EntityHelper.getUser().setSprinting(false);
            float[] vector = EntityHelper.getUser().getMoveVector();
            if (EntityHelper.getUser().isCollidedHorizontally) { // THIS pushes you into the block
                double x = vector[0] * 0.006;
                double z = vector[1] * 0.006;
                NetworkHelper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(EntityHelper.getUser().posX + x,
                        EntityHelper.getUser().posY, EntityHelper.getUser().posZ + z, false));
                NetworkHelper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(EntityHelper.getUser().posX + x * 11,
                        EntityHelper.getUser().posY - 11, EntityHelper.getUser().posZ + z * 11, false));
                EntityHelper.getUser().setPosition(EntityHelper.getUser().posX + x, EntityHelper.getUser().posY,
                        EntityHelper.getUser().posZ + z);
            } else if (EntityHelper.getUser().hurtTime == 9 || EntityHelper.getUser().isSneaking()) { // THIS sends you through the block
                double x = vector[0] * 0.3;
                double z = vector[1] * 0.3;
                NetworkHelper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(EntityHelper.getUser().posX + x,
                        EntityHelper.getUser().posY, EntityHelper.getUser().posZ + z, false));
                NetworkHelper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(EntityHelper.getUser().posX + x * 11,
                        EntityHelper.getUser().posY - 11, EntityHelper.getUser().posZ + z * 11, false));
                EntityHelper.getUser().setPosition(EntityHelper.getUser().posX + x, EntityHelper.getUser().posY,
                        EntityHelper.getUser().posZ + z);
            }
        } else if (EntityHelper.getUser().isSneaking()) { // DownClip because NCP is broken, lol.
            if (WorldHelper.getBlock(EntityHelper.getUser().getPosition().up()) instanceof BlockAir) {
                NetworkHelper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(EntityHelper.getUser().posX,
                        EntityHelper.getUser().posY - 1, EntityHelper.getUser().posZ, true));
            } else if (EntityHelper.getUser().ticksExisted % 4 == 0) {
                EntityHelper.getUser().setPosition(EntityHelper.getUser().posX, EntityHelper.getUser().posY - 0.7, EntityHelper.getUser().posZ);
            }
        }
    }

    @Override
    public void onCollision(EventBlockCollision event) {
        if (EntityHelper.getUser().isCollidedHorizontally) return;
        if (event.getBoundingBox() == null) return;
        if (event.getBoundingBox().minY < EntityHelper.getUser().posY) return;

        event.setCancelled(true);
    }
}
