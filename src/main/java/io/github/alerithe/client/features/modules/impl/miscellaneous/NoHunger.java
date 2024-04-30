package io.github.alerithe.client.features.modules.impl.miscellaneous;

import io.github.alerithe.events.Register;
import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.features.modules.Module;

public class NoHunger extends Module {
    public NoHunger() {
        super("NoHunger", new String[0], Type.MISCELLANEOUS);
    }

    @Register
    private void onPreUpdate(EventUpdate.Pre event) {
        event.setOnGround(false);
    }
}
