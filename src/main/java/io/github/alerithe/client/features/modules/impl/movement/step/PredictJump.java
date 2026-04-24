package io.github.alerithe.client.features.modules.impl.movement.step;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.impl.movement.Step;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.WorldHelper;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;

public class PredictJump extends StepMode {
    public PredictJump(Step module) {
        super("PredictJump", new String[0], module);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        KeyBinding.setKeyBindState(GameHelper.getSettings().keyBindJump,
                GameSettings.isKeyDown(GameHelper.getSettings().keyBindJump));

        if (!EntityHelper.getUser().isUserMoving()) return;
        if (!EntityHelper.getUser().onGround) return;

        float[] heading = EntityHelper.getUser().getMoveVector();

        for (int i = 1; i < 23; i++) {
            if (tryJump(heading, i / 10.0)) {
                break;
            }
        }
    }

    private boolean isFullBlock(IBlockState state) {
        if (state == null) return false;

        Block block = state.getBlock();
        if (block.isFullBlock()) return true;

        if (block instanceof BlockSlab) {
            return state.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP;
        }

        if (block instanceof BlockSnow) {
            return state.getValue(BlockSnow.LAYERS) > 5;
        }

        if (block instanceof BlockStairs) return true;
        if (block instanceof BlockCactus) return true;
        if (block instanceof BlockChest) return true;
        if (block instanceof BlockEnderChest) return true;
        if (block instanceof BlockEnchantmentTable) return true;

        return false;
    }

    private boolean isNotFullBlock(IBlockState state) {
        if (state == null) return true; // probably? lol

        Block block = state.getBlock();
        if (block.isFullBlock()) return false;
        if (block instanceof BlockSlab) return false;
        if (block instanceof BlockStairs) return false;

        return true;
    }

    private boolean tryJump(float[] heading, double offset) {
        double x = EntityHelper.getUser().posX;
        double y = EntityHelper.getUser().posY;
        double z = EntityHelper.getUser().posZ;

        double ox = x + (heading[0] * offset);
        double oz = z + (heading[1] * offset);

        if (!isFullBlock(WorldHelper.getBlockState(new BlockPos(ox, y, oz)))) return false;
        if (!isNotFullBlock(WorldHelper.getBlockState(new BlockPos(ox, y + 2, oz)))) return false;
        if (!isNotFullBlock(WorldHelper.getBlockState(new BlockPos(ox, y + 1, oz)))) return false;

        ox = x - (heading[0] * 0.125);
        oz = z - (heading[1] * 0.125);
        if (!isNotFullBlock(WorldHelper.getBlockState(new BlockPos(ox, y + 2, oz)))) return false;

        KeyBinding.setKeyBindState(GameHelper.getSettings().keyBindJump, true);
        return true;
    }
}
