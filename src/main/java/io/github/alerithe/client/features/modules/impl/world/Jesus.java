package io.github.alerithe.client.features.modules.impl.world;

import io.github.alerithe.client.events.EventBlockCollision;
import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.world.jesus.Bounce;
import io.github.alerithe.client.features.modules.impl.world.jesus.JesusMode;
import io.github.alerithe.client.features.modules.impl.world.jesus.Solid;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;
import io.github.alerithe.events.Register;

public class Jesus extends Module {
    private final ObjectProperty<JesusMode> mode = new ObjectProperty<>("Mode", new String[0], new Solid(), new Bounce());

    public Jesus() {
        super("Jesus", new String[]{"waterwalk"}, Type.WORLD);

        getPropertyManager().add(mode);
    }

    @Register
    private void onPreUpdate(EventUpdate.Pre event) {
        mode.getValue().onPreUpdate(event);
    }

    @Register
    private void onBlockCollide(EventBlockCollision event) {
        mode.getValue().onBlockCollide(event);
    }
}
