package io.github.alerithe.client.features.properties.impl;

public class DoubleProperty extends NumberProperty<Double> {
    public DoubleProperty(String name, String[] aliases, double value, double minimum, double maximum) {
        super(name, aliases, value, minimum, maximum);
    }

    @Override
    public void setValue(Double value) {
        super.setValue(value < getMinimum() ? getMinimum() : value > getMaximum() ? getMaximum() : value);
    }

    @Override
    public Double add(Double addend) {
        return getValue() + addend;
    }

    @Override
    public Double subtract(Double subtrahend) {
        return getValue() - subtrahend;
    }

    @Override
    public Double multiply(Double factor) {
        return getValue() * factor;
    }

    @Override
    public Double divide(Double denominator) {
        return getValue() / denominator;
    }
}
