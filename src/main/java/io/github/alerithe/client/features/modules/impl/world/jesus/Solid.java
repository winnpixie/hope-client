package io.github.alerithe.client.features.modules.impl.world.jesus;

import io.github.alerithe.client.events.game.EventBlockCollision;
import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.utilities.Wrapper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.AxisAlignedBB;

public class Solid extends JesusMode {
    private boolean isOnLiquid;

    public Solid() {
        super("Solid", new String[0]);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        if (!isOnLiquid) return;
        if (Wrapper.getPlayer().ticksExisted % 2 == 0) return;

        event.setY(event.getY() + 0.02);
        event.setOnGround(false);
        isOnLiquid = false;
    }

    @Override
    public void onBlockCollide(EventBlockCollision event) {
        if (!(event.getBlock() instanceof BlockLiquid)) return;

        event.setBoundingBox(new AxisAlignedBB(event.getPos(), event.getPos().add(1, 1, 1)));
        if (event.getPos().getY() < Wrapper.getPlayer().posY) {
            isOnLiquid = true;
        }
    }
}
