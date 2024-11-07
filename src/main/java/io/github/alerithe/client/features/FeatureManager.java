package io.github.alerithe.client.features;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FeatureManager<T extends Feature> {
    private final List<T> elements = new ArrayList<>();

    private File configurationFile;

    public List<T> getElements() {
        return elements;
    }

    public File getConfigurationFile() {
        return configurationFile;
    }

    public void setConfigurationFile(File configurationFile) {
        this.configurationFile = configurationFile;
    }

    public void add(T feature) {
        elements.add(feature);
    }

    public void remove(T feature) {
        elements.remove(feature);
    }

    public <V extends T> V find(Class<V> cls) {
        for (T feature : elements) {
            if (cls.isInstance(feature)) return cls.cast(feature);
        }

        return null;
    }

    public T find(String handle) {
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
