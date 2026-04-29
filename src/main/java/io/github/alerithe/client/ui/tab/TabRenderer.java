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
    private static final int ACTIVE_COLOR = 0xFFDEDEDE;
    private static final int TEXT_COLOR = 0xFF999999;

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
            width = MathHelper.max(width, VisualHelper.TXT.getStringWidth(type.getLabel()) + 9);
        }

        float x = xOffset + 1;
        float y = VisualHelper.TXT.getFontHeight() + 1f;
        for (Module.Type type : Module.Type.values()) {
            boolean active = getContainer().getType() == type;
            VisualHelper.GFX.drawSquare(x, y, width, VisualHelper.TXT.getFontHeight() + 3f,
                    active ? Client.ACCENT_COLOR : BACKGROUND_COLOR);
            VisualHelper.TXT.drawString(type.getLabel(), x + 2, y + 2,
                    active ? ACTIVE_COLOR : TEXT_COLOR);
            y += VisualHelper.TXT.getFontHeight() + 3f;
        }

        return width;
    }

    private float drawModules(float xOffset) {
        List<Module> modules = getContainer().getModules();
        float width = 0;
        for (Module module : modules) {
            width = MathHelper.max(width, VisualHelper.TXT.getStringWidth(module.getName()) + 4);
        }

        float x = xOffset + 1;
        float y = VisualHelper.TXT.getFontHeight() + 1f;
        for (Module module : modules) {
            VisualHelper.GFX.drawSquare(x, y, width, VisualHelper.TXT.getFontHeight() + 3f,
                    getContainer().getModule() == module ? Client.ACCENT_COLOR : BACKGROUND_COLOR);
            VisualHelper.TXT.drawString(module.getName(), x + 2, y + 2,
                    module.isEnabled() ? ACTIVE_COLOR : TEXT_COLOR);
            y += VisualHelper.TXT.getFontHeight() + 3f;
        }

        return width;
    }

    private float drawProperties(float xOffset) {
        List<Property<?>> properties = getContainer().getProperties();
        float width = 0;
        for (Property<?> property : properties) {
            width = MathHelper.max(width, VisualHelper.TXT.getStringWidth(format(property)) + 4);
        }

        float x = xOffset + 1;
        float y = VisualHelper.TXT.getFontHeight() + 1f;
        for (Property<?> property : properties) {
            VisualHelper.GFX.drawSquare(x, y, width, VisualHelper.TXT.getFontHeight() + 3f,
                    getContainer().getProperty() == property ? Client.ACCENT_COLOR : BACKGROUND_COLOR);
            VisualHelper.TXT.drawString(format(property), x + 2, y + 2,
                    TEXT_COLOR);
            y += VisualHelper.TXT.getFontHeight() + 3f;
        }

        return width;
    }

    private String format(Property<?> property) {
        return String.format("%s: \247f%s", property.getName(), property.getValue());
    }
}
