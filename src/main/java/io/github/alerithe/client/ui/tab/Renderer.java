package io.github.alerithe.client.ui.tab;

import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.Property;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.VisualHelper;
import io.github.alerithe.client.utilities.Wrapper;

import java.util.List;

// TODO: Why do I insist on using a tab UI.
public class Renderer extends Component {
    public Renderer(Container container) {
        super(container);
    }

    public void draw(float partialTicks) {
        switch (getContainer().getSection()) {
            case TYPE:
                drawCategories();
                break;
            case MODULE:
                drawModules();
                break;
            case PROPERTY:
                drawProperties();
                break;
        }
    }

    private void drawCategories() {
        int width = 0;
        for (Module.Type type : Module.Type.values()) {
            width = MathHelper.max(width, Wrapper.getTextRenderer().getStringWidth(type.getLabel().toUpperCase()) - 4);
        }

        int y = 11;
        for (Module.Type type : Module.Type.values()) {
            VisualHelper.drawRect(1, y, 1 + width, y + 12, getContainer().getType() == type ? 0xFF990000 : 0xFF222222);
            Wrapper.getTextRenderer().drawStringWithShadow(type.getLabel(), 3, y + 2, -1);
            y += 12;
        }
    }

    private void drawModules() {
        List<Module> modules = getContainer().getModules();
        int width = 0;
        for (Module module : modules) {
            width = MathHelper.max(width, Wrapper.getTextRenderer().getStringWidth(module.getName().toUpperCase()) + 4);
        }

        int y = 11;
        for (Module module : modules) {
            VisualHelper.drawRect(1, y, 1 + width, y + 12, getContainer().getModule() == module ? 0xFF990000 : 0xFF222222);
            Wrapper.getTextRenderer().drawStringWithShadow(module.getName(), 3, y + 2, module.isEnabled() ? -1 : 0xFFAAAAAA);
            y += 12;
        }
    }

    private void drawProperties() {
        List<Property<?>> properties = getContainer().getProperties();
        int width = 0;
        for (Property<?> property : properties) {
            width = MathHelper.max(width, Wrapper.getTextRenderer().getStringWidth(format(property).toUpperCase()) + 4);
        }

        int y = 11;
        for (Property<?> property : properties) {
            VisualHelper.drawRect(1, y, 1 + width, y + 12, getContainer().getProperty() == property ? 0xFF990000 : 0xFF222222);
            Wrapper.getTextRenderer().drawStringWithShadow(format(property), 3, y + 2, 0xFFAAAAAA);
            y += 12;
        }
    }

    private String format(Property<?> property) {
        return String.format("%s: \247f%s", property.getName(), property.getValue().toString());
    }
}
