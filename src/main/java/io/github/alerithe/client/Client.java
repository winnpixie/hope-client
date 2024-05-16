package io.github.alerithe.client;

import com.github.creeper123123321.viafabric.ViaFabric;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import io.github.alerithe.client.extensions.IngameGui;
import io.github.alerithe.client.features.commands.CommandManager;
import io.github.alerithe.client.features.friends.FriendManager;
import io.github.alerithe.client.features.keybinds.KeybindManager;
import io.github.alerithe.client.features.modules.ModuleManager;
import io.github.alerithe.client.features.plugins.PluginManager;
import io.github.alerithe.client.utilities.TTSManager;
import io.github.alerithe.client.utilities.Wrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class Client {
    public static final Logger LOGGER = LogManager.getLogger(Client.class);
    public static final String NAME = "Hope";
    public static final String BUILD = "0.1-dev";
    public static final CommandManager COMMAND_MANAGER = new CommandManager();
    public static final ModuleManager MODULE_MANAGER = new ModuleManager();
    public static final KeybindManager KEYBIND_MANAGER = new KeybindManager();
    public static final FriendManager FRIEND_MANAGER = new FriendManager();
    public static final PluginManager PLUGIN_MANAGER = new PluginManager();
    public static final TTSManager TTS_MANAGER = new TTSManager();
    public static File DATA_DIR;

    public static void load() {
        DATA_DIR = new File(Wrapper.getMC().mcDataDir, NAME);
        if (!DATA_DIR.exists() && !DATA_DIR.mkdir()) {
            throw new RuntimeException("Could not create save directory!");
        }

        TTS_MANAGER.load(); // TTS is very important, don't ask.
        COMMAND_MANAGER.load();
        MODULE_MANAGER.load();
        KEYBIND_MANAGER.load();
        FRIEND_MANAGER.load();
        PLUGIN_MANAGER.load();

        Wrapper.getMC().ingameGUI = new IngameGui(Wrapper.getMC());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            MODULE_MANAGER.save();
            KEYBIND_MANAGER.save();
            FRIEND_MANAGER.save();
            PLUGIN_MANAGER.save();

            TTS_MANAGER.unload();
        }));

        // ViaVersion / ViaMCP
        try {
            new ViaFabric().onInitialize();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Debug log-in
        String username = System.getProperty("mc.email", "");
        String password = System.getProperty("mc.pass", "");
        if (!username.isEmpty() && !password.isEmpty()) {
            try {
                Wrapper.getMC().setSession(Wrapper.createMicrosoftSession(username, password));
            } catch (MicrosoftAuthenticationException e) {
                e.printStackTrace();
            }
        }

        TTS_MANAGER.queueText("Hello, " + System.getProperty("user.name", Wrapper.getMC().getSession().getUsername()) + ".");
    }
}