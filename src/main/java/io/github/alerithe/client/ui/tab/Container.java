package io.github.alerithe.client.ui.tab;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.Property;

import java.util.List;

public class Container {
    private final Controller controller;
    private final Renderer renderer;
    
    private Section section;

    public Container() {
        this.controller = new Controller(this);
        this.renderer = new Renderer(this);
        section = Section.TYPE;
    }

    public Controller getController() {
        return controller;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Module.Type getType() {
        return Module.Type.values()[controller.getTypeIndex()];
    }

    public Module getModule() {
        return getModules().get(controller.getModuleIndex());
    }

    public List<Module> getModules() {
        return Client.MODULE_MANAGER.getAllInType(getType());
    }

    public Property<?> getProperty() {
        return getProperties().get(controller.getPropertyIndex());
    }

    public List<Property<?>> getProperties() {
        return getModule().getPropertyManager().getElements();
    }
}
