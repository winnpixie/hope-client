package io.github.alerithe.client.features.modules.impl.world;

import io.github.alerithe.client.events.EventBlockCollision;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.events.Register;
import net.minecraft.block.BlockCactus;
import net.minecraft.util.AxisAlignedBB;

public class NoPrick extends Module {
    private final BooleanProperty full = new BooleanProperty("Full", new String[0], false);

    public NoPrick() {
        super("NoPrick", new String[]{"anticactus", "antiprick"}, Type.WORLD);

        getPropertyManager().add(full);
    }

    @Register
    private void onCollision(EventBlockCollision event) {
        if (!(event.getBlock() instanceof BlockCactus)) return;

        event.setBoundingBox(new AxisAlignedBB(
                event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(),
                event.getPos().getX() + 1, event.getPos().getY() + (full.getValue() ? 1 : 0.9375),
                event.getPos().getZ() + 1
        ));
    }
}
