package io.github.alerithe.client.features.plugins;

import io.github.alerithe.client.features.Feature;

import java.util.logging.Logger;

public class Plugin extends Feature {
    private Logger logger;

    private PluginManifest manifest;

    public Plugin() {
        super("ERR_MISSING_MANIFEST");
    }

    public Logger getLogger() {
        return logger;
    }

    public PluginManifest getManifest() {
        return manifest;
    }

    @Override
    public String getName() {
        return manifest.getName();
    }

    final void configure(PluginManifest manifest) {
        this.manifest = manifest;

        this.logger = Logger.getLogger(manifest.getName());
    }

    public void onLoad() {
    }

    public void onExit() {
    }
}
