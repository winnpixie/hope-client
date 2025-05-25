package io.github.alerithe.client.features.properties.impl;

public class IntProperty extends NumberProperty<Integer> {
    public IntProperty(String name, String[] aliases, int value, int minimum, int maximum) {
        super(name, aliases, value, minimum, maximum);
    }

    @Override
    public void setValue(Integer value) {
        super.setValue(value < getMinimum() ? getMinimum() : value > getMaximum() ? getMaximum() : value);
    }
}
