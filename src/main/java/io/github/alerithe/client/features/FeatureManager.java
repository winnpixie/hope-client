package io.github.alerithe.client.features;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FeatureManager<T extends Feature> {
    private final List<T> elements = new ArrayList<>();

    private Path dataPath;

    public List<T> getElements() {
        return elements;
    }

    public Path getDataPath() {
        return dataPath;
    }

    public void setDataPath(Path dataPath) {
        this.dataPath = dataPath;
    }

    public void add(T feature) {
        elements.add(feature);
    }

    public void remove(T feature) {
        elements.remove(feature);
    }

    public <V extends T> V find(Class<V> cls) {
        for (T feature : elements) {
            if (cls.isInstance(feature)) {
                return cls.cast(feature);
            }
        }

        return null;
    }

    public T find(String name) {
        for (T feature : elements) {
            if (feature.getName().equalsIgnoreCase(name)) {
                return feature;
            }

            for (String alias : feature.getAliases()) {
                if (alias.equalsIgnoreCase(name)) {
                    return feature;
                }
            }
        }

        return null;
    }

    public void load() {
    }

    public void save() {
    }
}
