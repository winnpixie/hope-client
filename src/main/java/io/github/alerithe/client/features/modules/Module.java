package io.github.alerithe.client.features.modules;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.Feature;
import io.github.alerithe.client.features.properties.Property;
import io.github.alerithe.client.features.properties.PropertyManager;
import io.github.alerithe.events.EventBus;

import java.io.File;

public class Module extends Feature {
    private final Type type;
    private final PropertyManager propertyManager;
    private boolean enabled;
    public Property<Boolean> hidden = new Property<>("Hidden", new String[]{"hide"}, false);

    public Module(String name, String[] aliases, Type type) {
        super(name, aliases);
        this.type = type;
        this.propertyManager = new PropertyManager();
        this.propertyManager.setConfigFile(new File(Client.MODULE_MANAGER.moduleDataDir, getName() + ".txt"));

        hidden.setValue(type.equals(Type.VISUAL));
        propertyManager.add(hidden);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (enabled) {
            enable();
        } else {
            disable();
        }
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

    public void enable() {
        EventBus.register(this);
    }

    public void disable() {
        EventBus.unregister(this);
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
