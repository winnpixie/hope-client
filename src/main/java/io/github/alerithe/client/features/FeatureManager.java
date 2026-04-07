package io.github.alerithe.client.features;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FeatureManager<T extends Feature> {
    private final List<T> children = new ArrayList<>();

    private Path dataPath;

    public List<T> getChildren() {
        return children;
    }

    public Path getDataPath() {
        return dataPath;
    }

    public void setDataPath(Path dataPath) {
        this.dataPath = dataPath;
    }

    public void add(T feature) {
        children.add(feature);
    }

    public void remove(T feature) {
        children.remove(feature);
    }

    public <V extends T> V find(Class<V> cls) {
        for (T feature : children) {
            if (cls.isInstance(feature)) {
                return cls.cast(feature);
            }
        }

        return null;
    }

    public T find(String id) {
        for (T feature : children) {
            if (feature.getName().equalsIgnoreCase(id)) {
                return feature;
            }

            for (String alias : feature.getAliases()) {
                if (alias.equalsIgnoreCase(id)) {
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
