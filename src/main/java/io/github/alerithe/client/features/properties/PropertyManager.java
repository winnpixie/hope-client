package io.github.alerithe.client.features.properties;

import io.github.alerithe.client.features.FeatureManager;
import io.github.alerithe.client.features.properties.impl.*;
import io.github.alerithe.client.utilities.MathHelper;

import java.io.IOException;
import java.nio.file.Files;

public class PropertyManager extends FeatureManager<Property<?>> {
    @Override
    public void load() {
        try {
            Files.readAllLines(getConfigurationFile().toPath()).forEach(line -> {
                String[] data = line.split(":", 2);
                Property<?> property = find(data[0]);
                if (property == null) return;

                if (property instanceof BooleanProperty) {
                    ((BooleanProperty) property).setValue(Boolean.parseBoolean(data[1]));
                } else if (property instanceof IntProperty) {
                    if (MathHelper.isInt(data[1])) ((IntProperty) property).setValue(Integer.parseInt(data[1]));
                } else if (property instanceof DoubleProperty) {
                    if (MathHelper.isDouble(data[1])) ((DoubleProperty) property).setValue(Double.parseDouble(data[1]));
                } else if (property instanceof ObjectProperty) {
                    ObjectProperty objProp = (ObjectProperty) property;
                    ObjectProperty.Value value = objProp.get(data[1]);
                    if (value != null) objProp.setValue(value);
                } else if (property instanceof StringProperty) {
                    ((StringProperty) property).setValue(data[1]);
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
            Files.write(getConfigurationFile().toPath(), builder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
