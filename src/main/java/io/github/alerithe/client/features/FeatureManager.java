package io.github.alerithe.client.features;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FeatureManager<T extends Feature> {
    private final List<T> children = new ArrayList<>();

    private File dataFile;

    public List<T> getChildren() {
        return children;
    }

    public File getDataFile() {
        return dataFile;
    }

    public void setDataFile(File dataFile) {
        this.dataFile = dataFile;
    }

    public void add(T feature) {
        children.add(feature);
    }

    public void remove(T feature) {
        children.remove(feature);
    }

    public <V extends T> V find(Class<V> cls) {
        for (T feature : children) {
            if (cls.isInstance(feature)) return cls.cast(feature);
        }

        return null;
    }

    public T find(String handle) {
        for (T feature : children) {
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
