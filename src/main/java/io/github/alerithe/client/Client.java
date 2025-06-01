package io.github.alerithe.client;

import io.github.alerithe.client.events.game.EventProgramExit;
import io.github.alerithe.client.extensions.IngameGui;
import io.github.alerithe.client.features.commands.CommandManager;
import io.github.alerithe.client.features.friends.FriendManager;
import io.github.alerithe.client.features.keybinds.KeybindManager;
import io.github.alerithe.client.features.modules.ModuleManager;
import io.github.alerithe.client.features.plugins.PluginManager;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.IdentityHelper;
import io.github.alerithe.client.utilities.sessions.SessionHelper;
import io.github.alerithe.events.impl.EventBusImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class Client {
    public static final Logger LOGGER = LogManager.getLogger(Client.class);
    public static final String NAME = "Hope";
    public static final String BUILD = "0.1";

    public static final int ACCENT_COLOR = 0xFFB00B1E;

    public static final EventBusImpl EVENT_BUS = new EventBusImpl();
    public static final CommandManager COMMAND_MANAGER = new CommandManager();
    public static final ModuleManager MODULE_MANAGER = new ModuleManager();
    public static final KeybindManager KEYBIND_MANAGER = new KeybindManager();
    public static final FriendManager FRIEND_MANAGER = new FriendManager();
    public static final PluginManager PLUGIN_MANAGER = new PluginManager();

    public static File DATA_DIR;

    public static void load() {
        LOGGER.info(IdentityHelper.getId());

        DATA_DIR = new File(GameHelper.getGame().mcDataDir, NAME);
        if (!DATA_DIR.exists() && !DATA_DIR.mkdir()) {
            throw new RuntimeException("Could not create save directory!");
        }

        COMMAND_MANAGER.load();
        MODULE_MANAGER.load();
        KEYBIND_MANAGER.load();
        FRIEND_MANAGER.load();
        PLUGIN_MANAGER.load();

        GameHelper.getGame().ingameGUI = new IngameGui(GameHelper.getGame());

        EVENT_BUS.subscribe(EventProgramExit.class, event -> {
            MODULE_MANAGER.save();
            KEYBIND_MANAGER.save();
            FRIEND_MANAGER.save();
            PLUGIN_MANAGER.save();
        });

        // Debug log-in
        String username = System.getProperty("mc.email", "");
        String password = System.getProperty("mc.pass", "");
        if (!username.isEmpty() && !password.isEmpty()) {
            try {
                GameHelper.getGame().setSession(SessionHelper.MICROSOFT.createSession(username, password));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}