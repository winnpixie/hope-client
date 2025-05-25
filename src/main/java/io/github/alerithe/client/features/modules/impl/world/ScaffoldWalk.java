package io.github.alerithe.client.features.modules.impl.world;

import io.github.alerithe.client.events.game.EventBlockEdgeCheck;
import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.features.properties.impl.IntProperty;
import io.github.alerithe.client.utilities.MsTimer;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;
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
    private final IntProperty bps = new IntProperty("BPS", new String[]{"cps", "speed"}, 20, 1, 20);

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
    public void enable() {
        blocksPlaced = 0;
        this.data = null;

        super.enable();
    }

    @Register
    private void onPreUpdate(EventUpdate.Pre event) {
        event.setPitch(90);

        BlockPos pos = new BlockPos(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY - 1, Wrapper.getPlayer().posZ);
        Block block = Wrapper.getBlock(pos);
        data = null;

        if (!blacklist.contains(block)) return;
        if (!isHoldingBlock()) return;

        data = makeData(pos);
        if (data == null) return;

        float[] angles = Wrapper.getPlayer().getRotationToPosition(new Vec3(data.pos));
        event.setYaw(angles[0]);
        event.setPitch(angles[1]);
    }

    @Register
    private void onPostUpdate(EventUpdate.Post event) {
        if (data == null) return;
        if (!timer.hasPassed(1000 / bps.getValue())) return;
        if (!Wrapper.getPlayerController().onPlayerRightClick(Wrapper.getPlayer(), Wrapper.getWorld(),
                Wrapper.getPlayer().getHeldItem(), data.pos, data.facing, new Vec3(data.pos))) return;

        if (tower.getValue() && data.facing == EnumFacing.UP && Wrapper.getPlayer().movementInput.jump) {
            blocksPlaced++;
            Wrapper.getPlayer().motionY = 0.42;

            if (blocksPlaced > 8) {
                Wrapper.getPlayer().motionY = 0;
                blocksPlaced = 0;
            }

            Wrapper.getPlayer().setSpeed(0);
        }

        Wrapper.getPlayer().swingItem();
        timer.update();
    }

    @Register
    private void onBlockEdge(EventBlockEdgeCheck event) {
        event.setCancelled(true);
    }

    private Data makeData(BlockPos pos) {
        // now this part is simple
        if (!blacklist.contains(Wrapper.getBlock(pos.add(0, -1, 0)))) { // Down
            return new Data(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (!blacklist.contains(Wrapper.getBlock(pos.add(1, 0, 0)))) { // East
            return new Data(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!blacklist.contains(Wrapper.getBlock(pos.add(0, 0, 1)))) { // South
            return new Data(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!blacklist.contains(Wrapper.getBlock(pos.add(-1, 0, 0)))) { // West
            return new Data(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!blacklist.contains(Wrapper.getBlock(pos.add(0, 0, -1)))) { // North
            return new Data(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }

        return null;
    }

    private boolean isHoldingBlock() {
        return Wrapper.getPlayer().getHeldItem() != null
                && Wrapper.getPlayer().getHeldItem().getItem() instanceof ItemBlock;
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
