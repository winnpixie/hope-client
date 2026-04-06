package io.github.alerithe.client.features.modules.impl.world.waterwalk;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.WorldHelper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.BlockPos;

public class Bounce extends WaterWalkMode {
    public Bounce() {
        super("Bounce");
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        if (EntityHelper.getUser().isSneaking()) return;

        BlockPos playerLoc = new BlockPos(EntityHelper.getUser());
        if (!(WorldHelper.getBlock(playerLoc) instanceof BlockLiquid)) return;
        if (WorldHelper.getBlockState(playerLoc).getValue(BlockLiquid.LEVEL) > 4) return;

        EntityHelper.getUser().motionY = EntityHelper.getUser().movementInput.jump ? 0.5 : 0.13;
    }
}
