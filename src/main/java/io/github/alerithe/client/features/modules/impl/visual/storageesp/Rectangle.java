package io.github.alerithe.client.features.modules.impl.visual.storageesp;

import io.github.alerithe.client.events.game.EventDraw;
import io.github.alerithe.client.features.modules.impl.visual.StorageESP;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.WorldHelper;
import io.github.alerithe.client.utilities.graphics.VisualHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

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

            double[][] vertices = getProjectionVertices(entity);
            float[] projection = projectBoundingBox(vertices);

            projections.put(entity, projection);
        }
    }

    @Override
    public void onOverlayDraw(EventDraw.Overlay event) {
        VisualHelper.GFX_BUFFERED.begin(4);

        for (Map.Entry<TileEntity, float[]> projection : projections.entrySet()) {
            TileEntity tile = projection.getKey();
            float[] bounds = projection.getValue();

            float x = bounds[0];
            float y = bounds[1];
            float width = bounds[2] - bounds[0];
            float height = bounds[3] - bounds[1];

            VisualHelper.GFX_BUFFERED.drawBorderedSquare(x + 0.5f, y + 0.5f, width - 1f, height - 1f,
                    1.5f, 0x00000000, 0xFF000000);
            VisualHelper.GFX_BUFFERED.drawBorderedSquare(x, y, width, height,
                    0.5f, 0x00000000, module.getTileColor(tile));
        }

        VisualHelper.GFX_BUFFERED.end(GL11.GL_TRIANGLE_FAN);
    }

    private double[][] getProjectionVertices(TileEntity tile) {
        AxisAlignedBB aabb = module.getTileBoundingBox(tile);

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

    private float[] projectBoundingBox(double[][] vertices) {
        float[] bounds = new float[]{Float.MAX_VALUE, Float.MAX_VALUE, -1, -1};

        for (double[] vertex : vertices) {
            float[] projection = VisualHelper.project((float) vertex[0], (float) vertex[1], (float) vertex[2]);
            if (projection.length == 0
                    || projection[2] < 0f
                    || projection[2] >= 1f) {
                continue;
            }

            bounds[0] = MathHelper.min(bounds[0], projection[0]);
            bounds[1] = MathHelper.min(bounds[1], projection[1]);
            bounds[2] = MathHelper.max(bounds[2], projection[0]);
            bounds[3] = MathHelper.max(bounds[3], projection[1]);
        }

        return bounds;
    }
}
