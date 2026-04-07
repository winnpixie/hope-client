package io.github.alerithe.client.features.modules.impl.world.waterwalk;

import io.github.alerithe.client.events.game.EventBlockCollision;
import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.utilities.EntityHelper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.AxisAlignedBB;

public class Solid extends WaterWalkMode {
    private boolean isOnLiquid;

    public Solid() {
        super("Solid");
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        if (!isOnLiquid) return;

        if (EntityHelper.getUser().ticksExisted % 2 == 0) {
            event.setY(event.getY() + 0.02);
            event.setOnGround(false);
        } else {
            event.setOnGround(true);
        }

        isOnLiquid = false;
    }

    @Override
    public void onBlockCollide(EventBlockCollision event) {
        if (!(event.getBlock() instanceof BlockLiquid)) return;
        if (event.getPos().getY() > EntityHelper.getUser().posY) return;
        if (EntityHelper.getUser().isSneaking()) return;

        event.setBoundingBox(new AxisAlignedBB(event.getPos(), event.getPos().add(1, 1, 1)));
        isOnLiquid = true;
    }
}
