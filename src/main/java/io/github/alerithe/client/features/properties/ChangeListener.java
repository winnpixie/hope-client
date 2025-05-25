package io.github.alerithe.client.features.properties;

public interface ChangeListener<T> {
    void onValueChanged(Property<T> property);
}
