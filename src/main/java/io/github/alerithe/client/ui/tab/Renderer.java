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

    // FIXME: Autism.
    public void draw(float partialTicks) {
        int xOffset = 0;
        xOffset = drawCategories(xOffset) + 1;

        if (getContainer().getSection() == Section.MODULE) {
            drawModules(xOffset);
        }

        if (getContainer().getSection() == Section.PROPERTY) {
            xOffset += drawModules(xOffset) + 1;
            drawProperties(xOffset);
        }
    }

    private int drawCategories(int xOffset) {
        int width = 0;
        for (Module.Type type : Module.Type.values()) {
            width = MathHelper.max(width, Wrapper.getTextRenderer().getStringWidth(type.getLabel()) + 4);
        }

        int x = xOffset + 1;
        int y = 11;
        for (Module.Type type : Module.Type.values()) {
            VisualHelper.drawSquare(x, y, width, 12, getContainer().getType() == type ? 0xFF990000 : 0xFF222222);
            Wrapper.getTextRenderer().drawStringWithShadow(type.getLabel(), x + 2, y + 2, -1);
            y += 12;
        }

        return width;
    }

    private int drawModules(int xOffset) {
        List<Module> modules = getContainer().getModules();
        int width = 0;
        for (Module module : modules) {
            width = MathHelper.max(width, Wrapper.getTextRenderer().getStringWidth(module.getName()) + 4);
        }

        int x = xOffset + 1;
        int y = 11;
        for (Module module : modules) {
            VisualHelper.drawSquare(x, y, width, 12, getContainer().getModule() == module ? 0xFF990000 : 0xFF222222);
            Wrapper.getTextRenderer().drawStringWithShadow(module.getName(), x + 2, y + 2, module.isEnabled() ? -1 : 0xFFAAAAAA);
            y += 12;
        }

        return width;
    }

    private int drawProperties(int xOffset) {
        List<Property<?>> properties = getContainer().getProperties();
        int width = 0;
        for (Property<?> property : properties) {
            width = MathHelper.max(width, Wrapper.getTextRenderer().getStringWidth(format(property)) + 4);
        }

        int x = xOffset + 1;
        int y = 11;
        for (Property<?> property : properties) {
            VisualHelper.drawSquare(x, y, width, 12, getContainer().getProperty() == property ? 0xFF990000 : 0xFF222222);
            Wrapper.getTextRenderer().drawStringWithShadow(format(property), x + 2, y + 2, 0xFFAAAAAA);
            y += 12;
        }

        return width;
    }

    private String format(Property<?> property) {
        return String.format("%s: \247f%s", property.getName(), property.getValue().toString());
    }
}
