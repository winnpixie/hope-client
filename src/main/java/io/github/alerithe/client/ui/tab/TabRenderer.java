package io.github.alerithe.client.ui.tab;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.Property;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.graphics.VisualHelper;

import java.util.List;

// TODO: Why do I insist on using a tab UI.
public class TabRenderer extends TabComponent {
    private static final int BACKGROUND_COLOR = 0x69000000;

    private static final int TEXT_COLOR = 0xFFAAAAAA;

    public TabRenderer(TabContainer container) {
        super(container);
    }

    // FIXME: Autism.
    public void draw(float partialTicks) {
        float xOffset = 0;
        xOffset = drawCategories(xOffset) + 1;

        if (getContainer().getSection() == TabSection.MODULE) {
            drawModules(xOffset);
        }

        if (getContainer().getSection() == TabSection.PROPERTY) {
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
            boolean active = getContainer().getType() == type;
            VisualHelper.MC_GFX.drawSquare(x, y, width, 12,
                    active ? Client.ACCENT_COLOR : BACKGROUND_COLOR);
            VisualHelper.MC_FONT.drawStringWithShadow(type.getLabel(), x + 2, y + 2,
                    active ? 0xFFFFFFFF : TEXT_COLOR);
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
            VisualHelper.MC_GFX.drawSquare(x, y, width, 12,
                    getContainer().getModule() == module ? Client.ACCENT_COLOR : BACKGROUND_COLOR);
            VisualHelper.MC_FONT.drawStringWithShadow(module.getName(), x + 2, y + 2,
                    module.isEnabled() ? 0xFFFFFFFF : TEXT_COLOR);
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
            VisualHelper.MC_GFX.drawSquare(x, y, width, 12,
                    getContainer().getProperty() == property ? Client.ACCENT_COLOR : BACKGROUND_COLOR);
            VisualHelper.MC_FONT.drawStringWithShadow(format(property), x + 2, y + 2,
                    TEXT_COLOR);
            y += 12;
        }

        return width;
    }

    private String format(Property<?> property) {
        return String.format("%s: \247f%s", property.getName(), property.getValue());
    }
}
