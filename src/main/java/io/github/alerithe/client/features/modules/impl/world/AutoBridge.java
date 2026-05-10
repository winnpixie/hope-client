package io.github.alerithe.client.features.modules.impl.world;

import io.github.alerithe.client.events.bus.Subscribe;
import io.github.alerithe.client.events.game.EventBlockEdgeTest;
import io.github.alerithe.client.events.game.EventMoveUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.features.properties.impl.IntProperty;
import io.github.alerithe.client.utilities.*;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class AutoBridge extends Module {
    private final BooleanProperty tower = new BooleanProperty("Tower", new String[0], true);
    private final IntProperty bps = new IntProperty("BPS", new String[]{"cps", "speed"},
            9, 1, 20);

    private final Stopwatch timer = new Stopwatch();

    private Data data;
    private int blocksPlaced;

    public AutoBridge() {
        super("AutoBridge", new String[]{"scaffoldwalk", "scaffold"}, Type.WORLD);

        getPropertyManager().add(tower);
        getPropertyManager().add(bps);
    }

    @Override
    public void onEnable() {
        blocksPlaced = 0;
        this.data = null;
    }

    @Subscribe
    public void onPreUpdate(EventMoveUpdate.Pre event) {
        data = null;
        if (!isHoldingBlock()) return;

        event.setPitch(90f);

        double px = EntityHelper.getUser().posX;
        double py = EntityHelper.getUser().posY - 0.5;
        double pz = EntityHelper.getUser().posZ;

        BlockPos below = new BlockPos(px, py, pz);
        if (WorldHelper.isFullBlock(below)) return;

        data = makeData(below);
        if (data == null) return;

        float[] angles = RotationHelper.getRotationToBlock(data.pos);
        event.setYaw(event.getYaw() + RotationHelper.getAngleDelta(angles[0], event.getYaw()));
        event.setPitch(angles[1]);
    }

    @Subscribe
    public void onPostUpdate(EventMoveUpdate.Post event) {
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
        timer.reset();
    }

    @Subscribe
    public void onBlockEdge(EventBlockEdgeTest event) {
        event.cancel();
    }

    // now this part is simple
    private Data makeData(BlockPos pos) {
        // Down
        if (WorldHelper.isFullBlock(pos.add(0, -1, 0))) {
            return new Data(pos.add(0, -1, 0), EnumFacing.UP);
        }

        // East
        if (WorldHelper.isFullBlock(pos.add(1, 0, 0))) {
            return new Data(pos.add(1, 0, 0), EnumFacing.WEST);
        }

        // South
        if (WorldHelper.isFullBlock(pos.add(0, 0, 1))) {
            return new Data(pos.add(0, 0, 1), EnumFacing.NORTH);
        }

        // West
        if (WorldHelper.isFullBlock(pos.add(-1, 0, 0))) {
            return new Data(pos.add(-1, 0, 0), EnumFacing.EAST);
        }

        // North
        if (WorldHelper.isFullBlock(pos.add(0, 0, -1))) {
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
