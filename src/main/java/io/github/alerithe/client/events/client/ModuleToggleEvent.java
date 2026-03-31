package io.github.alerithe.client.events.client;

import io.github.alerithe.client.events.bus.Event;
import io.github.alerithe.client.features.modules.Module;

public class ModuleToggleEvent implements Event {
    private final Module module;

    public ModuleToggleEvent(Module module) {
        this.module = module;
    }

    public Module getModule() {
        return module;
    }
}
