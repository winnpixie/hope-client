package io.github.alerithe.client.ui.tab;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.Property;

import java.util.List;

public class TabContainer {
    private final TabController controller = new TabController(this);
    private final TabRenderer renderer = new TabRenderer(this);

    private TabSection section = TabSection.TYPE;

    public TabController getController() {
        return controller;
    }

    public TabRenderer getRenderer() {
        return renderer;
    }

    public TabSection getSection() {
        return section;
    }

    public void setSection(TabSection section) {
        this.section = section;
    }

    public Module.Type getType() {
        return Module.Type.values()[controller.getTypeIndex()];
    }

    public Module getModule() {
        return getModules().get(controller.getModuleIndex());
    }

    public List<Module> getModules() {
        return Client.MODULE_MANAGER.allOfType(getType());
    }

    public Property<?> getProperty() {
        return getProperties().get(controller.getPropertyIndex());
    }

    public List<Property<?>> getProperties() {
        return getModule().getPropertyManager().getElements();
    }
}
