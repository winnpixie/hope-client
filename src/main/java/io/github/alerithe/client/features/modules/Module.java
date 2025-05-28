package io.github.alerithe.client.features.modules;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.client.ModuleToggleEvent;
import io.github.alerithe.client.features.Feature;
import io.github.alerithe.client.features.properties.PropertyManager;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;

import java.io.File;

public class Module extends Feature {
    private final Type type;
    private final PropertyManager propertyManager;
    private boolean enabled;
    public BooleanProperty hidden = new BooleanProperty("Hidden", new String[]{"hide"}, false);

    public Module(String name, String[] aliases, Type type) {
        super(name, aliases);
        this.type = type;
        this.propertyManager = new PropertyManager();
        this.propertyManager.setConfigurationFile(new File(Client.MODULE_MANAGER.getConfigurationFile(), getName() + ".txt"));

        hidden.setValue(type.equals(Type.VISUAL));
        propertyManager.add(hidden);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (enabled) {
            onEnable();

            Client.EVENT_BUS.subscribe(this);
        } else {
            Client.EVENT_BUS.unsubscribe(this);

            onDisable();
        }

        // EVENT
        Client.EVENT_BUS.post(new ModuleToggleEvent(this));
    }

    public Type getType() {
        return type;
    }

    public PropertyManager getPropertyManager() {
        return propertyManager;
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public enum Type {
        COMBAT("Combat"),
        MOVEMENT("Movement"),
        PLAYER("Player"),
        VISUAL("Visual"),
        WORLD("World"),
        MISCELLANEOUS("Miscellaneous");

        private final String label;

        Type(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }
}
