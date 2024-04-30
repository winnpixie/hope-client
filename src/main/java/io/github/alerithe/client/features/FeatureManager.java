package io.github.alerithe.client.features;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FeatureManager<T extends Feature> {
    private File configFile;
    private final List<T> elements;

    public FeatureManager() {
        this.elements = new ArrayList<>();
    }

    public File getConfigFile() {
        return configFile;
    }

    public void setConfigFile(File configFile) {
        this.configFile = configFile;
    }

    public List<T> getElements() {
        return elements;
    }

    public void add(T feature) {
        elements.add(feature);
    }

    public void remove(T feature) {
        elements.remove(feature);
    }

    public <V extends T> V get(Class<V> cls) {
        for (T feature : elements) {
            if (feature.getClass().equals(cls)) return (V) feature;
        }

        return null;
    }

    public T get(String handle) {
        for (T feature : elements) {
            if (feature.getName().equalsIgnoreCase(handle)) return feature;

            for (String alias : feature.getAliases()) {
                if (alias.equalsIgnoreCase(handle)) return feature;
            }
        }

        return null;
    }

    public void load() {
    }

    public void save() {
    }
}
