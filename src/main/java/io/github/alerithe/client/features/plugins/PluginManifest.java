package io.github.alerithe.client.features.plugins;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PluginManifest {
    private final String name;
    private final String version;
    private final String entryPoint;

    public PluginManifest(InputStream dataStream) throws IOException {
        Properties properties = new Properties();
        properties.load(dataStream);

        this.name = properties.getProperty("name");
        this.version = properties.getProperty("version");
        this.entryPoint = properties.getProperty("entrypoint");
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getEntryPoint() {
        return entryPoint;
    }
}
