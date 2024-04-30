package io.github.alerithe.client.features.plugins;

import io.github.alerithe.client.features.Feature;

public class Plugin extends Feature {
    public Plugin(String name, String... aliases) {
        super(name, aliases);
    }

    public void onClientLoad() {
    }

    public void onClientUnload() {
    }
}
