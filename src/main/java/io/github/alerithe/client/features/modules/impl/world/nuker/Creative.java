package io.github.alerithe.client.features.modules.impl.world.nuker;

import io.github.alerithe.client.events.game.EventMoveUpdate;
import io.github.alerithe.client.features.modules.impl.world.Nuker;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.NetworkHelper;
import io.github.alerithe.client.utilities.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class Creative extends NukerMode {
    public Creative(Nuker module) {
        super("Creative", new String[0], module);
    }

    @Override
    public void onPreUpdate(EventMoveUpdate.Pre event) {
        if (module.currentBlock != null) {
            return;
        }

        int reach = module.reach.getValue();
        for (int y = -reach; y <= reach; y++) {
            for (int x = -reach; x <= reach; x++) {
                for (int z = -reach; z <= reach; z++) {
                    BlockPos pos = EntityHelper.getUser().getPosition().add(x, y + 1, z);
                    Block block = WorldHelper.getBlock(pos);
                    if (block instanceof BlockAir) {
                        continue;
                    }

                    module.currentBlock = pos;
                    breakBlockInstant(pos);
                    module.currentBlock = null;
                }
            }
        }
    }

    @Override
    public void onPostUpdate(EventMoveUpdate.Post event) {
        if (module.currentBlock == null) {
            return;
        }

        breakBlockInstant(module.currentBlock);
        module.currentBlock = null;
    }

    private void breakBlockInstant(BlockPos pos) {
        EnumFacing facing = EntityHelper.getUser().getHorizontalFacing();
        NetworkHelper.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK,
                pos, facing));
        NetworkHelper.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                pos, facing));

        if (module.swing.getValue()) {
            EntityHelper.getUser().swingItem();
        }
    }
}
