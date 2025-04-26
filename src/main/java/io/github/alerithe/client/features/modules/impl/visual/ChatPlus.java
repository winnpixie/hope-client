package io.github.alerithe.client.features.modules.impl.visual;

import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.features.properties.impl.IntProperty;
import io.github.alerithe.client.utilities.Wrapper;

public class ChatPlus extends Module {
    public final BooleanProperty background = new BooleanProperty("Background", new String[]{"fastchat"}, true);
    public final IntProperty historyLines = new IntProperty("MaxLines", new String[]{"maxhistory", "history", "lines"},
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
        Wrapper.printMessage("\2474Chat+ can not be turned off.");
    }
}
