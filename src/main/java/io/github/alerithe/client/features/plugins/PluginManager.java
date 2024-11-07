package io.github.alerithe.client.features.plugins;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.FeatureManager;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PluginManager extends FeatureManager<Plugin> {
    @Override
    public void load() {
        setConfigurationFile(new File(Client.DATA_DIR, "plugins"));
        if (!getConfigurationFile().exists() && !getConfigurationFile().mkdir()) {
            Client.LOGGER.warn("Could not create plugins directory (does it already exist?)!");
        }

        loadPluginsFromFiles();

        Client.LOGGER.info(String.format("Registered %d Plugin(s)", getElements().size()));

        getElements().forEach(plugin -> {
            plugin.getLogger().info(String.format("Loading %s", plugin.getName()));
            plugin.onLoad();
        });
    }

    private void loadPluginsFromFiles() {
        File[] pluginFiles = getConfigurationFile().listFiles();
        if (pluginFiles == null) return;
        if (pluginFiles.length < 1) return;

        ClassLoader clsLoader = getClass().getClassLoader();
        Thread.currentThread().setContextClassLoader(clsLoader);

        for (File zipFile : pluginFiles) {
            String pathName = zipFile.getAbsolutePath();
            if (!pathName.toLowerCase().endsWith(".jar") && !pathName.toLowerCase().endsWith(".zip")) continue;

            try (ZipFile zip = new ZipFile((zipFile));
                 URLClassLoader urlClsLoader = new URLClassLoader(new URL[]{zipFile.toURI().toURL()}, clsLoader)) {
                Enumeration<? extends ZipEntry> entries = zip.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    if (entry.isDirectory()) continue;
                    if (!entry.getName().toLowerCase().endsWith(".class")) continue;

                    String className = entry.getName().replaceAll("[/\\\\]", ".");
                    className = className.substring(0, className.lastIndexOf('.'));
                    Class<?> cls = urlClsLoader.loadClass(className);
                    if (!Plugin.class.isAssignableFrom(cls)) continue;

                    Plugin plugin = (Plugin) cls.newInstance();
                    getElements().add(plugin);
                    Client.LOGGER.info(String.format("Registered plugin %s", plugin.getName()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void save() {
        getElements().forEach(plugin -> {
            plugin.getLogger().info(String.format("Unloading %s", plugin.getName()));
            plugin.onExit();
        });
    }
}
