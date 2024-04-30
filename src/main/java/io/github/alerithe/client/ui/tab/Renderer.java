package io.github.alerithe.client.ui.tab;

import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.Property;
import io.github.alerithe.client.utilities.VisualHelper;
import io.github.alerithe.client.utilities.Wrapper;

import java.util.Arrays;
import java.util.List;

public class Renderer extends Component {
    public Renderer(Container container) {
        super(container);
    }

    public void draw(float partialTicks) {
        int typeWidth = Arrays.stream(Module.Type.values())
                .map(type -> Wrapper.getFontRenderer().getStringWidth(type.getLabel().toUpperCase()))
                .max(Integer::compare).get() + 4;
        int typeY = 11;
        for (Module.Type type : Module.Type.values()) {
            VisualHelper.drawRect(1, typeY, 1 + typeWidth, typeY + 12, getContainer().getType() == type ? 0xFF990000
                    : 0xFF222222);
            Wrapper.getFontRenderer().drawStringWithShadow(type.getLabel(), 3, typeY + 2, -1);
            typeY += 12;
        }

        if (getContainer().getSection().equals(Section.MODULE) || getContainer().getSection().equals(Section.PROPERTY)) {
            int moduleX = typeWidth + 2;
            List<Module> modules = getContainer().getModules();
            int moduleWidth = modules.stream()
                    .map(type -> Wrapper.getFontRenderer().getStringWidth(type.getName().toUpperCase()))
                    .max(Integer::compare).get() + 4;
            int moduleY = 11;
            for (Module module : modules) {
                VisualHelper.drawRect(moduleX, moduleY, moduleX + moduleWidth, moduleY + 12,
                        getContainer().getModule() == module ? 0xFF990000 : 0xFF222222);
                Wrapper.getFontRenderer().drawStringWithShadow(module.getName(), moduleX + 2, moduleY + 2,
                        module.isEnabled() ? -1 : 0xFFAAAAAA);
                moduleY += 12;
            }

            if (getContainer().getSection().equals(Section.PROPERTY)) {
                int propertyX = moduleX + moduleWidth + 1;
                List<Property<?>> properties = getContainer().getProperties();
                int propertyWidth = properties.stream()
                        .map(property -> Wrapper.getFontRenderer().getStringWidth(format(property).toUpperCase()))
                        .max(Integer::compare).get() + 4;
                int propertyY = 11;
                for (Property<?> property : properties) {
                    VisualHelper.drawRect(propertyX, propertyY, propertyX + propertyWidth, propertyY + 12,
                            getContainer().getProperty() == property ? 0xFF990000 : 0xFF222222);
                    Wrapper.getFontRenderer().drawStringWithShadow(format(property), propertyX + 2, propertyY + 2,
                            0xFFAAAAAA);
                    propertyY += 12;
                }
            }
        }
    }

    private String format(Property<?> property) {
        return String.format("%s: \247f%s", property.getName(), property.getValue().toString());
    }
}
