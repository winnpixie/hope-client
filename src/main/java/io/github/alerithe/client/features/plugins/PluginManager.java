package io.github.alerithe.client.features.plugins;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.FeatureManager;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginManager extends FeatureManager<Plugin> {
    private ClassLoader parentClassLoader;

    @Override
    public void load() {
        setDataPath(Client.DATA_PATH.resolve("plugins"));
        if (Files.notExists(getDataPath())) {
            try {
                Files.createDirectory(getDataPath());
            } catch (IOException ioe) {
                Client.LOGGER.warn("Could not create plugins directory (does it already exist?)!");
            }
        }

        this.parentClassLoader = getClass().getClassLoader();
        loadPluginsFromFiles();

        Client.LOGGER.info("Registered {} plugin(s)", getChildren().size());

        getChildren().forEach(plugin -> {
            plugin.getLogger().info(() -> String.format("Loading %s v%s", plugin.getName(), plugin.getManifest().getVersion()));
            plugin.onLoad();
        });
    }

    private void loadPluginsFromFiles() {
        try (DirectoryStream<Path> directory = Files.newDirectoryStream(getDataPath())) {
            directory.forEach(path -> {
                Plugin plugin = loadPluginFromFile(path);
                if (plugin == null) return;

                getChildren().add(plugin);
                Client.LOGGER.info("Registered plugin {}", plugin.getName());
            });
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private Plugin loadPluginFromFile(Path path) {
        String fullPath = path.toString();
        if (!fullPath.toLowerCase().endsWith(".jar")) return null;

        Plugin plugin = null;
        PluginClassLoader loader = null;

        try {
            JarFile jar = new JarFile(path.toFile());
            loader = new PluginClassLoader(path, jar, parentClassLoader);
            JarEntry manifestEntry = jar.getJarEntry("plugin.properties");
            if (manifestEntry == null) return null;

            PluginManifest manifest = new PluginManifest(jar.getInputStream(manifestEntry));
            Class<?> entryPoint = loader.loadClass(manifest.getEntryPoint());
            if (!Plugin.class.isAssignableFrom(entryPoint)) return null;

            plugin = (Plugin) entryPoint.newInstance();
            plugin.configure(manifest);
            return plugin;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        } finally {
            if (plugin == null && loader != null) {
                try {
                    loader.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }

    @Override
    public void save() {
        getChildren().forEach(plugin -> {
            plugin.getLogger().info(() -> String.format("Unloading %s", plugin.getName()));
            plugin.onExit();

            try {
                ((PluginClassLoader) plugin.getClass().getClassLoader()).close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        });
    }
}
