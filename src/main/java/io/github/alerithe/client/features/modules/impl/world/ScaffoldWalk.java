package io.github.alerithe.client.features.modules.impl.world;

import io.github.alerithe.client.events.game.EventBlockEdgeCheck;
import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.features.properties.impl.IntProperty;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.MsTimer;
import io.github.alerithe.client.utilities.WorldHelper;
import io.github.alerithe.events.impl.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

import java.util.Arrays;
import java.util.List;

public class ScaffoldWalk extends Module {
    private final BooleanProperty tower = new BooleanProperty("Tower", new String[0], true);
    private final IntProperty bps = new IntProperty("BPS", new String[]{"cps", "speed"},
            20, 1, 20);

    private final List<Block> blacklist = Arrays.asList(Blocks.cocoa, Blocks.water, Blocks.flowing_water, Blocks.lava,
            Blocks.flowing_lava, Blocks.air, Blocks.flower_pot, Blocks.red_flower, Blocks.yellow_flower,
            Blocks.tallgrass);
    private final MsTimer timer = new MsTimer();

    private Data data;
    private int blocksPlaced;

    public ScaffoldWalk() {
        super("ScaffoldWalk", new String[]{"scaffold"}, Type.WORLD);

        getPropertyManager().add(tower);
        getPropertyManager().add(bps);
    }

    @Override
    public void onEnable() {
        blocksPlaced = 0;
        this.data = null;
    }

    @Subscribe
    private void onPreUpdate(EventUpdate.Pre event) {
        event.setPitch(90);

        BlockPos pos = new BlockPos(EntityHelper.getUser().posX, EntityHelper.getUser().posY - 1, EntityHelper.getUser().posZ);
        Block block = WorldHelper.getBlock(pos);
        data = null;

        if (!blacklist.contains(block)) return;
        if (!isHoldingBlock()) return;

        data = makeData(pos);
        if (data == null) return;

        float[] angles = EntityHelper.getRotationToBlock(data.pos);
        event.setYaw(angles[0]);
        event.setPitch(angles[1]);
    }

    @Subscribe
    private void onPostUpdate(EventUpdate.Post event) {
        if (data == null) return;
        if (!timer.hasPassed(1000 / bps.getValue())) return;
        if (!GameHelper.getController().onPlayerRightClick(EntityHelper.getUser(), WorldHelper.getWorld(),
                EntityHelper.getUser().getHeldItem(), data.pos, data.facing, new Vec3(data.pos))) return;

        if (tower.getValue() && data.facing == EnumFacing.UP && EntityHelper.getUser().movementInput.jump) {
            blocksPlaced++;
            EntityHelper.getUser().motionY = 0.42;

            if (blocksPlaced > 8) {
                EntityHelper.getUser().motionY = 0;
                blocksPlaced = 0;
            }

            EntityHelper.getUser().setSpeed(0);
        }

        EntityHelper.getUser().swingItem();
        timer.update();
    }

    @Subscribe
    private void onBlockEdge(EventBlockEdgeCheck event) {
        event.setCancelled(true);
    }

    private Data makeData(BlockPos pos) {
        // now this part is simple
        if (!blacklist.contains(WorldHelper.getBlock(pos.add(0, -1, 0)))) { // Down
            return new Data(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (!blacklist.contains(WorldHelper.getBlock(pos.add(1, 0, 0)))) { // East
            return new Data(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!blacklist.contains(WorldHelper.getBlock(pos.add(0, 0, 1)))) { // South
            return new Data(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!blacklist.contains(WorldHelper.getBlock(pos.add(-1, 0, 0)))) { // West
            return new Data(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!blacklist.contains(WorldHelper.getBlock(pos.add(0, 0, -1)))) { // North
            return new Data(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }

        return null;
    }

    private boolean isHoldingBlock() {
        return EntityHelper.getUser().getHeldItem() != null
                && EntityHelper.getUser().getHeldItem().getItem() instanceof ItemBlock;
    }

    private static class Data {
        private final BlockPos pos;
        private final EnumFacing facing;

        public Data(BlockPos pos, EnumFacing facing) {
            this.pos = pos;
            this.facing = facing;
        }
    }
}
