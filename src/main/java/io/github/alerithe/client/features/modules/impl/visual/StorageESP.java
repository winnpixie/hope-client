package io.github.alerithe.client.features.modules.impl.visual;

import io.github.alerithe.client.events.bus.Subscribe;
import io.github.alerithe.client.events.game.EventDraw;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.visual.storageesp.Box;
import io.github.alerithe.client.features.modules.impl.visual.storageesp.Rectangle;
import io.github.alerithe.client.features.modules.impl.visual.storageesp.StorageESPMode;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.graphics.VisualHelper;
import net.minecraft.block.BlockJukebox;
import net.minecraft.tileentity.*;
import net.minecraft.util.AxisAlignedBB;

public class StorageESP extends Module {
    private final ObjectProperty<StorageESPMode> mode = new ObjectProperty<>("Mode", new String[0],
            new Rectangle(this), new Box(this));
    private final BooleanProperty normalChests = new BooleanProperty("NormalChests", new String[]{"chests"}, true);
    private final BooleanProperty trappedChests = new BooleanProperty("TrappedChests", new String[]{"traps"}, true);
    private final BooleanProperty enderChests = new BooleanProperty("EnderChests", new String[]{"ender"}, true);
    private final BooleanProperty furnaces = new BooleanProperty("Furnaces", new String[0], true);
    private final BooleanProperty droppers = new BooleanProperty("Droppers", new String[0], true);
    private final BooleanProperty dispensers = new BooleanProperty("Dispensers", new String[0], true);
    private final BooleanProperty jukeBoxes = new BooleanProperty("JukeBoxes", new String[0], true);

    public StorageESP() {
        super("StorageESP", new String[]{"chestesp"}, Type.VISUAL);

        getPropertyManager().add(mode);
        getPropertyManager().add(normalChests);
        getPropertyManager().add(trappedChests);
        getPropertyManager().add(enderChests);
        getPropertyManager().add(furnaces);
        getPropertyManager().add(droppers);
        getPropertyManager().add(dispensers);
        getPropertyManager().add(jukeBoxes);
    }

    @Subscribe
    public void onWorldDraw(EventDraw.World event) {
        mode.getValue().onWorldDraw(event);
    }

    @Subscribe
    public void onOverlayDraw(EventDraw.Overlay event) {
        mode.getValue().onOverlayDraw(event);
    }

    public boolean qualifies(TileEntity tile) {
        boolean included = false;

        if (tile instanceof TileEntityChest) {
            TileEntityChest chest = (TileEntityChest) tile;

            included = (chest.getChestType() == 0 && normalChests.getValue())
                    || (chest.getChestType() == 1 && trappedChests.getValue());
        }

        included |= (tile instanceof TileEntityEnderChest && enderChests.getValue())
                || (tile instanceof TileEntityFurnace && furnaces.getValue())
                || (tile instanceof TileEntityDropper && droppers.getValue())
                || (tile instanceof TileEntityDispenser && dispensers.getValue())
                || (tile instanceof BlockJukebox.TileEntityJukebox && jukeBoxes.getValue());

        if (included) {
            AxisAlignedBB aabb = tile.getBlockType().getSelectedBoundingBox(tile.getWorld(), tile.getPos());
            if (!VisualHelper.isInView(aabb)) {
                return false;
            }
        }

        return included;
    }

    public AxisAlignedBB getTileBoundingBox(TileEntity tile) {
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
                AxisAlignedBB aabbAdjacent = adjacent.getBlockType().getSelectedBoundingBox(adjacent.getWorld(), adjacent.getPos());
                aabb = aabb.union(aabbAdjacent);
            }
        }

        return aabb.offset(-GameHelper.getGame().getRenderManager().viewerPosX,
                -GameHelper.getGame().getRenderManager().viewerPosY,
                -GameHelper.getGame().getRenderManager().viewerPosZ);
    }

    public int getTileColor(TileEntity tile) {
        if (tile instanceof TileEntityChest) {
            TileEntityChest chest = (TileEntityChest) tile;

            return chest.getChestType() == 0 ? 0xFFFFFF00 : 0xFFF46607;
        }

        if (tile instanceof TileEntityEnderChest) return 0xFFFF00FF;
        if (tile instanceof TileEntityFurnace) return 0xFF424242;
        if (tile instanceof TileEntityDropper) return 0xFFAAAAAA;
        if (tile instanceof TileEntityDispenser) return 0xFF696969;
        if (tile instanceof BlockJukebox.TileEntityJukebox) return 0xFF0099FF;

        return 0xFFFFFFFF;
    }
}
