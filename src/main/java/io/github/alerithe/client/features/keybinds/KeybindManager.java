package io.github.alerithe.client.features.keybinds;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.EventInput;
import io.github.alerithe.client.features.FeatureManager;
import io.github.alerithe.events.EventBus;
import io.github.alerithe.events.EventHandler;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class KeybindManager extends FeatureManager<Keybind> {
    @Override
    public void load() {
        setConfigurationFile(new File(Client.DATA_DIR, "keybinds.txt"));

        try {
            Files.readAllLines(getConfigurationFile().toPath()).forEach(line -> {
                String[] data = line.split(":", 2);
                Keybind kb = find(data[0]);
                if (kb == null) return;

                kb.setKey(Keyboard.getKeyIndex(data[1].toUpperCase()));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        EventBus.register(new EventHandler<EventInput.KeyPress>() {
            @Override
            public void handle(EventInput.KeyPress event) {
                getElements().forEach(kb -> {
                    if (kb.getKey() == event.getKey()) kb.getAction().run();
                });
            }
        });
    }

    @Override
    public void save() {
        StringBuilder builder = new StringBuilder();
        getElements().forEach(kb -> builder.append(kb.getName()).append(':')
                .append(Keyboard.getKeyName(kb.getKey())).append('\n'));

        try {
            Files.write(getConfigurationFile().toPath(), builder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
