package io.github.alerithe.client.features.modules.impl.movement.step;

import io.github.alerithe.client.events.game.EventMoveUpdate;
import io.github.alerithe.client.features.modules.impl.movement.Step;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.WorldHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;

public class PredictJump extends StepMode {
    public PredictJump(Step module) {
        super("PredictJump", new String[0], module);
    }

    @Override
    public void onPreUpdate(EventMoveUpdate.Pre event) {
        KeyBinding.setKeyBindState(GameHelper.getSettings().keyBindJump.getKeyCode(),
                GameSettings.isKeyDown(GameHelper.getSettings().keyBindJump));

        if (!EntityHelper.getUser().isUserMoving()) return;
        if (!EntityHelper.getUser().onGround) return;
        if (EntityHelper.getUser().isInLiquid()) return;

        float[] heading = EntityHelper.getUser().getMoveVector();

        for (int i = 1; i < 23; i++) {
            if (tryJump(heading, i / 10.0)) {
                break;
            }
        }
    }

    private boolean tryJump(float[] heading, double offset) {
        double x = EntityHelper.getUser().posX;
        double y = EntityHelper.getUser().posY;
        double z = EntityHelper.getUser().posZ;

        double ox = x + (heading[0] * offset);
        double oz = z + (heading[1] * offset);

        if (!WorldHelper.isFullBlock(WorldHelper.getBlockState(new BlockPos(ox, y, oz)))) return false;
        if (WorldHelper.isFullBlock(WorldHelper.getBlockState(new BlockPos(ox, y + 2, oz)))) return false;
        if (WorldHelper.isFullBlock(WorldHelper.getBlockState(new BlockPos(ox, y + 1, oz)))) return false;

        ox = x - (heading[0] * 0.125);
        oz = z - (heading[1] * 0.125);
        if (WorldHelper.isFullBlock(WorldHelper.getBlockState(new BlockPos(ox, y + 2, oz)))) return false;

        KeyBinding.setKeyBindState(GameHelper.getSettings().keyBindJump.getKeyCode(), true);
        return true;
    }
}
