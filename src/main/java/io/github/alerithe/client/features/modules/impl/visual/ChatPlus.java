package io.github.alerithe.client.features.modules.impl.visual;

import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.Property;
import io.github.alerithe.client.features.properties.impl.NumberProperty;
import io.github.alerithe.client.utilities.Wrapper;

public class ChatPlus extends Module {
    public final Property<Boolean> background = new Property<>("Background", new String[]{"fastchat"}, true);
    public final NumberProperty<Integer> historyLines = new NumberProperty<>("MaxLines", new String[]{"maxhistory", "history", "lines"},
            100, 0, Integer.MAX_VALUE);

    public ChatPlus() {
        super("Chat+", new String[]{"chatplus"}, Type.VISUAL);

        getPropertyManager().add(background);
        getPropertyManager().add(historyLines);

        setEnabled(true);
    }

    @Override
    public void disable() {
        super.disable();

        setEnabled(true);
        Wrapper.printChat("\2474Chat+ can not be turned off.");
    }
}
