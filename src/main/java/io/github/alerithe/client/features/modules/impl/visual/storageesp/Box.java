package io.github.alerithe.client.features.modules.impl.visual.storageesp;

import io.github.alerithe.client.events.game.EventDraw;
import io.github.alerithe.client.features.modules.impl.visual.StorageESP;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.WorldHelper;
import io.github.alerithe.client.utilities.graphics.VisualHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class Box extends StorageESPMode {
    private final Set<TileEntity> tiles = new TreeSet<>(Comparator.comparing(tile -> {
        BlockPos pos = tile.getPos();
        return -WorldHelper.distanceSq(pos.getX(), pos.getY(), pos.getZ());
    }));

    public Box(StorageESP module) {
        super("3D", new String[]{"box"}, module);
    }

    @Override
    public void onWorldDraw(EventDraw.World event) {
        tiles.clear();
        for (TileEntity entity : WorldHelper.getWorld().loadedTileEntityList) {
            if (module.qualifies(entity)) tiles.add(entity);
        }

        for (TileEntity tile : tiles) {
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

            GlStateManager.disableDepth();
            GlStateManager.disableLighting();

            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glLineWidth(2f);
            VisualHelper.MC_GFX.drawPrism(aabb.minX, aabb.minY, aabb.minZ,
                    aabb.maxX, aabb.maxY, aabb.maxZ,
                    module.getTileColor(tile));
            GL11.glDisable(GL11.GL_LINE_SMOOTH);

            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
        }
    }
}
