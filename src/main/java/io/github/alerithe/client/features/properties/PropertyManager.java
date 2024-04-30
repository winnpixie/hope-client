package io.github.alerithe.client.features.properties;

import io.github.alerithe.client.features.FeatureManager;
import io.github.alerithe.client.features.properties.impl.NumberProperty;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;
import io.github.alerithe.client.utilities.MathHelper;

import java.io.IOException;
import java.nio.file.Files;

public class PropertyManager extends FeatureManager<Property<?>> {
    @Override
    public void load() {
        try {
            Files.readAllLines(getConfigFile().toPath()).forEach(line -> {
                String[] data = line.split(":", 2);
                Property<?> property = get(data[0]);
                if (property == null) return;

                if (property.getValue() instanceof Boolean) {
                    ((Property<Boolean>) property).setValue(data[1].equalsIgnoreCase("true"));
                } else if (property instanceof NumberProperty) {
                    if (property.getValue() instanceof Integer) {
                        if (!MathHelper.isInt(data[1])) return;

                        ((NumberProperty<Integer>) property).setValue(Integer.parseInt(data[1]));
                    } else if (property.getValue() instanceof Double) {
                        if (!MathHelper.isDouble(data[1])) return;

                        ((NumberProperty<Double>) property).setValue(Double.parseDouble(data[1]));
                    }
                } else if (property instanceof ObjectProperty) {
                    ObjectProperty prop = (ObjectProperty) property;
                    ObjectProperty.Value value = prop.get(data[1]);
                    if (value != null) prop.setValue(value);
                } else if (property.getValue() instanceof String) {
                    ((Property<String>) property).setValue(data[1]);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        StringBuilder builder = new StringBuilder();
        getElements().forEach(property -> builder.append(property.getName()).append(':')
                .append(property.getValue().toString()).append('\n'));

        try {
            Files.write(getConfigFile().toPath(), builder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
