package io.github.alerithe.client.ui.tab;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.Property;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.graphics.VisualHelper;

import java.util.List;

// TODO: Why do I insist on using a tab UI.
public class Renderer extends Component {
    private static final int BACKGROUND_COLOR = 0xEE111111;

    public Renderer(Container container) {
        super(container);
    }

    // FIXME: Autism.
    public void draw(float partialTicks) {
        float xOffset = 0;
        xOffset = drawCategories(xOffset) + 1;

        if (getContainer().getSection() == Section.MODULE) {
            drawModules(xOffset);
        }

        if (getContainer().getSection() == Section.PROPERTY) {
            xOffset += drawModules(xOffset) + 1;
            drawProperties(xOffset);
        }
    }

    private float drawCategories(float xOffset) {
        float width = 0;
        for (Module.Type type : Module.Type.values()) {
            width = MathHelper.max(width, VisualHelper.MC_FONT.getStringWidth(type.getLabel()) + 4);
        }

        float x = xOffset + 1;
        float y = 11;
        for (Module.Type type : Module.Type.values()) {
            VisualHelper.drawSquare(x, y, width, 12, getContainer().getType() == type ? Client.ACCENT_COLOR : BACKGROUND_COLOR);
            VisualHelper.MC_FONT.drawStringWithShadow(type.getLabel(), x + 2, y + 2, -1);
            y += 12;
        }

        return width;
    }

    private float drawModules(float xOffset) {
        List<Module> modules = getContainer().getModules();
        float width = 0;
        for (Module module : modules) {
            width = MathHelper.max(width, VisualHelper.MC_FONT.getStringWidth(module.getName()) + 4);
        }

        float x = xOffset + 1;
        float y = 11;
        for (Module module : modules) {
            VisualHelper.drawSquare(x, y, width, 12, getContainer().getModule() == module ? Client.ACCENT_COLOR : BACKGROUND_COLOR);
            VisualHelper.MC_FONT.drawStringWithShadow(module.getName(), x + 2, y + 2, module.isEnabled() ? -1 : 0xFFAAAAAA);
            y += 12;
        }

        return width;
    }

    private float drawProperties(float xOffset) {
        List<Property<?>> properties = getContainer().getProperties();
        float width = 0;
        for (Property<?> property : properties) {
            width = MathHelper.max(width, VisualHelper.MC_FONT.getStringWidth(format(property)) + 4);
        }

        float x = xOffset + 1;
        float y = 11;
        for (Property<?> property : properties) {
            VisualHelper.drawSquare(x, y, width, 12, getContainer().getProperty() == property ? Client.ACCENT_COLOR : BACKGROUND_COLOR);
            VisualHelper.MC_FONT.drawStringWithShadow(format(property), x + 2, y + 2, 0xFFAAAAAA);
            y += 12;
        }

        return width;
    }

    private String format(Property<?> property) {
        return String.format("%s: \247f%s", property.getName(), property.getValue());
    }
}
