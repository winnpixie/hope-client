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
        setConfigFile(new File(Client.DATA_DIR, "plugins"));
        if (!getConfigFile().exists()) {
            if (!getConfigFile().mkdir()) {
                Client.LOGGER.warning("Could not create plugins directory (does it already exist?)!");
            }
        }

        loadJarFiles();

        Client.LOGGER.info(String.format("Registered %d Plugins", getElements().size()));

        getElements().forEach(plugin -> {
            Client.LOGGER.info(String.format("Loading plugin %s", plugin.getName()));
            plugin.onClientLoad();
        });
    }

    private void loadJarFiles() {
        File[] pluginFiles = getConfigFile().listFiles();
        if (pluginFiles == null) return;
        if (pluginFiles.length < 1) return;

        ClassLoader currentCl = getClass().getClassLoader();
        Thread.currentThread().setContextClassLoader(currentCl);

        for (File zipFile : pluginFiles) {
            String pathName = zipFile.getAbsolutePath();
            if (!pathName.toLowerCase().endsWith(".jar") && !pathName.toLowerCase().endsWith(".zip")) continue;

            try (ZipFile zip = new ZipFile((zipFile));
                 URLClassLoader urlCl = new URLClassLoader(new URL[]{zipFile.toURI().toURL()}, currentCl)) {
                Enumeration<? extends ZipEntry> entries = zip.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    if (entry.isDirectory()) continue;
                    if (entry.getName().toLowerCase().endsWith(".class")) continue;

                    String className = entry.getName().replaceAll("[/\\\\]", ".");
                    className = className.substring(0, className.lastIndexOf('.'));
                    Class<?> cls = urlCl.loadClass(className);
                    if (!Plugin.class.isAssignableFrom(cls)) continue;

                    Plugin plugin = (Plugin) cls.newInstance();
                    getElements().add(plugin);
                    Client.LOGGER.info(String.format("Detected and registered plugin %s", plugin.getName()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void save() {
        getElements().forEach(plugin -> {
            Client.LOGGER.info(String.format("Unloading plugin %s", plugin.getName()));
            plugin.onClientUnload();
        });
    }
}
