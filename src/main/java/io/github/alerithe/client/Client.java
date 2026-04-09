package io.github.alerithe.client;

import io.github.alerithe.client.events.bus.EventBus;
import io.github.alerithe.client.events.game.EventProgramExit;
import io.github.alerithe.client.features.commands.CommandManager;
import io.github.alerithe.client.features.friends.FriendManager;
import io.github.alerithe.client.features.keybinds.KeybindManager;
import io.github.alerithe.client.features.modules.ModuleManager;
import io.github.alerithe.client.features.plugins.PluginManager;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.IdentityHelper;
import io.github.alerithe.client.utilities.SessionHelper;
import io.github.alerithe.client.utilities.SpeechEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Client {
    public static final Logger LOGGER = LogManager.getLogger(Client.class);

    public static final String NAME = "Hope";
    public static final String BUILD = "0.1";
    public static final int ACCENT_COLOR = 0xFFF0111E;

    public static final SpeechEngine SPEECH_ENGINE = new SpeechEngine();

    public static final EventBus EVENT_BUS = new EventBus();

    public static final CommandManager COMMAND_MANAGER = new CommandManager();
    public static final ModuleManager MODULE_MANAGER = new ModuleManager();
    public static final KeybindManager KEYBIND_MANAGER = new KeybindManager();
    public static final FriendManager FRIEND_MANAGER = new FriendManager();
    public static final PluginManager PLUGIN_MANAGER = new PluginManager();

    public static final Path DATA_PATH = GameHelper.getGame().mcDataDir.toPath().resolve(NAME);

    private Client() {
    }

    public static void load() {
        LOGGER.info(IdentityHelper.getId());

        if (Files.notExists(DATA_PATH)) {
            try {
                Files.createDirectory(DATA_PATH);
            } catch (IOException ioe) {
                throw new IllegalStateException(ioe);
            }
        }

        SPEECH_ENGINE.init();

        COMMAND_MANAGER.load();
        MODULE_MANAGER.load();
        KEYBIND_MANAGER.load();
        FRIEND_MANAGER.load();
        PLUGIN_MANAGER.load();

        EVENT_BUS.subscribe(EventProgramExit.class, event -> {
            MODULE_MANAGER.save();
            KEYBIND_MANAGER.save();
            FRIEND_MANAGER.save();
            PLUGIN_MANAGER.save();

            SPEECH_ENGINE.cleanUp();
        });

        if (System.getProperty("client.silentStart", "").isEmpty()) {
            SPEECH_ENGINE.queue(String.format("Hello, %s. Welcome to %s client, built for Minecraft 1 point 8 point 8",
                    GameHelper.getGame().getSession().getUsername(), Client.NAME));
        }

        // Debug log-in
        authenticateFromProperties();
    }

    private static void authenticateFromProperties() {
        String username = System.getProperty("mc.email", "");
        String password = System.getProperty("mc.pass", "");
        if (username.isEmpty()) return;
        if (password.isEmpty()) return;

        try {
            GameHelper.getGame().setSession(SessionHelper.logInWithMicrosoft(username, password));
        } catch (Exception e) {
            LOGGER.warn("Error logging in from system properties", e);
        }
    }
}