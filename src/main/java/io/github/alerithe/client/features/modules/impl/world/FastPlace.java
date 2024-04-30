package io.github.alerithe.client.features.modules.impl.world;

import io.github.alerithe.client.events.EventInput;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.NumberProperty;
import io.github.alerithe.events.Register;

public class FastPlace extends Module {
    private final NumberProperty<Integer> speed = new NumberProperty<>("Speed", new String[0], 4, 0, 4);

    public FastPlace() {
        super("FastPlace", new String[0], Type.WORLD);

        getPropertyManager().add(speed);
    }

    @Register
    private void onRightClick(EventInput.RightClick event) {
        event.setDelay(event.getDelay() - speed.getValue());
    }
}
