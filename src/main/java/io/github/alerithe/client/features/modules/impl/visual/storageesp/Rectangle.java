package io.github.alerithe.client.features.modules.impl.visual.storageesp;

import io.github.alerithe.client.events.game.EventDraw;
import io.github.alerithe.client.features.modules.impl.visual.StorageESP;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.WorldHelper;
import io.github.alerithe.client.utilities.graphics.VisualHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class Rectangle extends StorageESPMode {
    private final Map<TileEntity, float[]> projections = new TreeMap<>(Comparator.comparing(tile -> {
        BlockPos pos = tile.getPos();
        return -WorldHelper.distanceSq(pos.getX(), pos.getY(), pos.getZ());
    }));

    public Rectangle(StorageESP module) {
        super("2D", new String[]{"rectangle", "rect"}, module);
    }

    @Override
    public void onWorldDraw(EventDraw.World event) {
        projections.clear();

        for (TileEntity entity : WorldHelper.getWorld().loadedTileEntityList) {
            if (!module.qualifies(entity)) {
                continue;
            }

            double[][] boundingBox = getBoundingBox(entity);
            float[] projection = projectBoundingBox(boundingBox);

            projections.put(entity, projection);
        }
    }

    @Override
    public void onOverlayDraw(EventDraw.Overlay event) {
        for (Map.Entry<TileEntity, float[]> projection : projections.entrySet()) {
            TileEntity tile = projection.getKey();
            float[] position = projection.getValue();

            float x = position[0];
            float y = position[1];
            float width = position[2] - position[0];
            float height = position[3] - position[1];

            VisualHelper.MC_GFX.drawBorderedSquare(x + 0.5f, y + 0.5f, width - 1f, height - 1f,
                    1.5f, 0x00000000, 0xFF000000);
            VisualHelper.MC_GFX.drawBorderedSquare(x, y, width, height,
                    0.5f, 0x00000000, module.getTileColor(tile));
        }
    }

    private double[][] getBoundingBox(TileEntity tile) {
        AxisAlignedBB aabb = tile.getBlockType().getSelectedBoundingBox(tile.getWorld(), tile.getPos());

        if (tile instanceof TileEntityChest) {
            TileEntityChest chest = (TileEntityChest) tile;
            TileEntityChest adjacent = null;

            if (chest.adjacentChestXPos != null) {
                adjacent = chest.adjacentChestXPos;
            } else if (chest.adjacentChestXNeg != null) {
                adjacent = chest.adjacentChestXNeg;
            } else if (chest.adjacentChestZPos != null) {
                adjacent = chest.adjacentChestZPos;
            } else if (chest.adjacentChestZNeg != null) {
                adjacent = chest.adjacentChestZNeg;
            }

            if (adjacent != null) {
                AxisAlignedBB aabbAdjacent = tile.getBlockType().getSelectedBoundingBox(adjacent.getWorld(), adjacent.getPos());
                aabb = aabb.union(aabbAdjacent);
            }
        }

        aabb = aabb.offset(-GameHelper.getGame().getRenderManager().viewerPosX,
                -GameHelper.getGame().getRenderManager().viewerPosY,
                -GameHelper.getGame().getRenderManager().viewerPosZ);

        return new double[][]{
                new double[]{aabb.minX, aabb.minY, aabb.minZ},
                new double[]{aabb.minX, aabb.maxY, aabb.minZ},
                new double[]{aabb.minX, aabb.maxY, aabb.maxZ},
                new double[]{aabb.minX, aabb.minY, aabb.maxZ},
                new double[]{aabb.maxX, aabb.minY, aabb.minZ},
                new double[]{aabb.maxX, aabb.maxY, aabb.minZ},
                new double[]{aabb.maxX, aabb.maxY, aabb.maxZ},
                new double[]{aabb.maxX, aabb.minY, aabb.maxZ}
        };
    }

    private float[] projectBoundingBox(double[][] boundingBox) {
        float[] position = new float[]{Float.MAX_VALUE, Float.MAX_VALUE, -1, -1};

        for (double[] vector : boundingBox) {
            float[] projection = VisualHelper.project((float) vector[0], (float) vector[1], (float) vector[2]);
            if (projection.length == 0
                    || projection[2] < 0f
                    || projection[2] >= 1f) {
                continue;
            }

            position[0] = MathHelper.min(position[0], projection[0]);
            position[1] = MathHelper.min(position[1], projection[1]);
            position[2] = MathHelper.max(position[2], projection[0]);
            position[3] = MathHelper.max(position[3], projection[1]);
        }

        return position;
    }
}
