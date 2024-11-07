package io.github.alerithe.client.features.properties.impl;

public class DoubleProperty extends NumberProperty<Double> {
    public DoubleProperty(String name, String[] aliases, double value, double minimum, double maximum) {
        super(name, aliases, value, minimum, maximum);
    }

    @Override
    public void setValue(Double value) {
        super.setValue(value < getMinimum() ? getMinimum() : value > getMaximum() ? getMaximum() : value);
    }
}
