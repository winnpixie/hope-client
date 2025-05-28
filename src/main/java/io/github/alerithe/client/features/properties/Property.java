package io.github.alerithe.client.features.properties;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.client.PropertyModifiedEvent;
import io.github.alerithe.client.features.Feature;

import java.util.ArrayList;
import java.util.List;

public class Property<T> extends Feature {
    private T value;
    private final List<ChangeListener<T>> changeListeners = new ArrayList<>();

    public Property(String name, String[] aliases, T value) {
        super(name, aliases);
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;

        for (ChangeListener<T> changeListener : changeListeners) changeListener.onValueChanged(this);

        // EVENT
        Client.EVENT_BUS.post(new PropertyModifiedEvent(this));
    }

    public void addChangeListener(ChangeListener<T> changeListener) {
        changeListeners.add(changeListener);
    }

    public void removeChangeListener(ChangeListener<T> changeListener) {
        changeListeners.remove(changeListener);
    }

    public List<ChangeListener<T>> getChangeListeners() {
        return changeListeners;
    }
}
