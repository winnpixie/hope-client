package io.github.alerithe.client.features.modules;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.client.ModuleToggleEvent;
import io.github.alerithe.client.features.Feature;
import io.github.alerithe.client.features.properties.PropertyManager;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;

public class Module extends Feature {
    private final Type type;
    private final PropertyManager propertyManager;
    private final BooleanProperty visibility = new BooleanProperty("Hidden", new String[]{"hide"}, false);

    private boolean enabled;

    public Module(String name, String[] aliases, Type type) {
        super(name, aliases);

        this.type = type;
        this.propertyManager = new PropertyManager();

        propertyManager.setDataPath(Client.MODULE_MANAGER.getDataPath().resolve(String.format("%s.properties", getName())));

        visibility.setValue(type.equals(Type.VISUAL));
        propertyManager.add(visibility);
    }

    public Type getType() {
        return type;
    }

    public PropertyManager getPropertyManager() {
        return propertyManager;
    }

    public BooleanProperty getVisibility() {
        return visibility;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (enabled) {
            onEnable();

            Client.EVENT_BUS.subscribeAll(this);
        } else {
            Client.EVENT_BUS.unsubscribeAll(this);

            onDisable();
        }

        // EVENT
        Client.EVENT_BUS.post(new ModuleToggleEvent(this));
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
