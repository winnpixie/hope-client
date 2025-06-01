package io.github.alerithe.client.features.plugins;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.FeatureManager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginManager extends FeatureManager<Plugin> {
    private ClassLoader primaryClassLoader;

    @Override
    public void load() {
        setConfigurationFile(new File(Client.DATA_DIR, "plugins"));
        if (!getConfigurationFile().exists() && !getConfigurationFile().mkdir()) {
            Client.LOGGER.warn("Could not create plugins directory (does it already exist?)!");
        }

        this.primaryClassLoader = getClass().getClassLoader();
        loadPluginsFromFiles();

        Client.LOGGER.info(String.format("Registered %d Plugin(s)", getChildren().size()));

        getChildren().forEach(plugin -> {
            plugin.getLogger().info(String.format("Loading %s", plugin.getName()));
            plugin.onLoad();
        });
    }

    private void loadPluginsFromFiles() {
        File[] pluginFiles = getConfigurationFile().listFiles();
        if (pluginFiles == null) return;
        if (pluginFiles.length < 1) return;

        for (File file : pluginFiles) {
            Plugin plugin = loadPluginFromFile(file);
            if (plugin == null) continue;

            getChildren().add(plugin);
            Client.LOGGER.info(String.format("Registered plugin %s v%s",
                    plugin.getManifest().getName(),
                    plugin.getManifest().getVersion()));
        }
    }

    private Plugin loadPluginFromFile(File file) {
        String path = file.getPath();
        if (!path.toLowerCase().endsWith(".jar")) return null;

        PluginClassLoader loader = null;

        try (JarFile jar = new JarFile(file)) {
            JarEntry manifestEntry = jar.getJarEntry("plugin.properties");
            if (manifestEntry == null) return null;

            loader = new PluginClassLoader(file, primaryClassLoader);

            PluginManifest manifest = new PluginManifest(jar.getInputStream(manifestEntry));
            Class<?> entryPoint = loader.loadClass(manifest.getEntryPoint());
            if (!Plugin.class.isAssignableFrom(entryPoint)) return null;

            Plugin plugin = (Plugin) entryPoint.newInstance();
            plugin.setManifest(manifest);
            return plugin;
        } catch (Exception e) {
            e.printStackTrace();

            try {
                if (loader != null) loader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public void save() {

        getChildren().forEach(plugin -> {
            plugin.getLogger().info(String.format("Unloading %s", plugin.getManifest().getName()));
            plugin.onExit();

            try {
                ((PluginClassLoader) plugin.getClass().getClassLoader()).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
