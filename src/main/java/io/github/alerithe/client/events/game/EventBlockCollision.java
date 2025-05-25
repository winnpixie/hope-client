package io.github.alerithe.client.events.game;

import io.github.alerithe.events.CancellableEvent;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class EventBlockCollision extends CancellableEvent {
    private Block block;
    private BlockPos pos;
    private AxisAlignedBB boundingBox;

    public EventBlockCollision(Block block, BlockPos pos, AxisAlignedBB boundingBox) {
        this.block = block;
        this.pos = pos;
        this.boundingBox = boundingBox;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    public AxisAlignedBB getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }
}
