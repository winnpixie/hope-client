package io.github.alerithe.client.features.modules.impl.world.jesus;

import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.utilities.Wrapper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.BlockPos;

public class Bounce extends JesusMode {
    public Bounce() {
        super("Bounce", new String[0]);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        if (Wrapper.getPlayer().isSneaking()) return;

        BlockPos playerLoc = new BlockPos(Wrapper.getPlayer());
        if (!(Wrapper.getBlock(playerLoc) instanceof BlockLiquid)) return;
        if (Wrapper.getWorld().getBlockState(playerLoc).getValue(BlockLiquid.LEVEL) > 4) return;

        Wrapper.getPlayer().motionY = Wrapper.getPlayer().movementInput.jump ? 0.5 : 0.13;
    }
}
