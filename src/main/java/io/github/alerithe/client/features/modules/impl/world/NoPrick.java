package io.github.alerithe.client.features.modules.impl.world;

import io.github.alerithe.client.events.bus.Subscribe;
import io.github.alerithe.client.events.game.EventBlockCollision;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import net.minecraft.block.BlockCactus;
import net.minecraft.util.AxisAlignedBB;

public class NoPrick extends Module {
    private final BooleanProperty fullCoverage = new BooleanProperty("FullCoverage", new String[0], false);

    public NoPrick() {
        super("NoPrick", new String[]{"anticactus", "antiprick"}, Type.WORLD);

        getPropertyManager().add(fullCoverage);
    }

    @Subscribe
    public void onCollision(EventBlockCollision event) {
        if (!(event.getBlock() instanceof BlockCactus)) return;

        event.setBoundingBox(new AxisAlignedBB(
                event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(),
                event.getPos().getX() + 1.0,
                event.getPos().getY() + (fullCoverage.getValue() ? 1.0 : 0.9375),
                event.getPos().getZ() + 1.0
        ));
    }
}
