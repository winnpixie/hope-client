package io.github.alerithe.client.features.plugins;

import io.github.alerithe.client.features.Feature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Plugin extends Feature {
    private final Logger logger = LogManager.getLogger(this.getName());

    public Plugin(String name, String... aliases) {
        super(name, aliases);
    }

    public Logger getLogger() {
        return logger;
    }

    public void onLoad() {
    }

    public void onExit() {
    }
}
