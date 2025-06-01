package io.github.alerithe.client.features.plugins;

import io.github.alerithe.client.features.Feature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Plugin extends Feature {
    private final Logger logger = LogManager.getLogger(this.getName());

    private PluginManifest manifest;

    public Plugin() {
        super("");
    }

    public Logger getLogger() {
        return logger;
    }

    public PluginManifest getManifest() {
        return manifest;
    }

    protected final void setManifest(PluginManifest manifest) {
        this.manifest = manifest;
    }

    public void onLoad() {
    }

    public void onExit() {
    }
}
