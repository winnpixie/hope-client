package io.github.alerithe.client.features.modules.impl.world;

import io.github.alerithe.client.events.bus.Subscribe;
import io.github.alerithe.client.events.game.EventMoveUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.world.nuker.Creative;
import io.github.alerithe.client.features.modules.impl.world.nuker.NukerMode;
import io.github.alerithe.client.features.modules.impl.world.nuker.Survival;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.features.properties.impl.IntProperty;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;
import net.minecraft.util.BlockPos;

public class Nuker extends Module {
    private final ObjectProperty<NukerMode> mode = new ObjectProperty<>("Mode", new String[0], new Creative(this), new Survival(this));
    public final IntProperty reach = new IntProperty("Reach", new String[0], 3, 1, 6);
    public final BooleanProperty swing = new BooleanProperty("Swing", new String[0], true);

    public BlockPos currentBlock;

    public Nuker() {
        super("Nuker", new String[0], Type.WORLD);

        getPropertyManager().add(mode);
        getPropertyManager().add(reach);
        getPropertyManager().add(swing);
    }

    @Override
    public void onDisable() {
        this.currentBlock = null;
    }

    @Subscribe
    public void onPreUpdate(EventMoveUpdate.Pre event) {
        mode.getValue().onPreUpdate(event);
    }

    @Subscribe
    public void onPostUpdate(EventMoveUpdate.Post event) {
        mode.getValue().onPostUpdate(event);
    }
}
