package io.github.alerithe.client;

import com.github.creeper123123321.viafabric.ViaFabric;
import io.github.alerithe.client.events.game.EventProgramExit;
import io.github.alerithe.client.extensions.IngameGui;
import io.github.alerithe.client.features.commands.CommandManager;
import io.github.alerithe.client.features.friends.FriendManager;
import io.github.alerithe.client.features.keybinds.KeybindManager;
import io.github.alerithe.client.features.modules.ModuleManager;
import io.github.alerithe.client.features.plugins.PluginManager;
import io.github.alerithe.client.utilities.IdentityHelper;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.client.utilities.sessions.SessionHelper;
import io.github.alerithe.client.utilities.speech.SpeechRecognition;
import io.github.alerithe.client.utilities.speech.TextToSpeech;
import io.github.alerithe.events.EventBus;
import io.github.alerithe.events.EventHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class Client {
    public static final Logger LOGGER = LogManager.getLogger(Client.class);
    public static final String NAME = "Hope";
    public static final String BUILD = "0.1-dev";

    public static final int ACCENT_COLOR = 0xFFB00B1E;

    public static final CommandManager COMMAND_MANAGER = new CommandManager();
    public static final ModuleManager MODULE_MANAGER = new ModuleManager();
    public static final KeybindManager KEYBIND_MANAGER = new KeybindManager();
    public static final FriendManager FRIEND_MANAGER = new FriendManager();
    public static final PluginManager PLUGIN_MANAGER = new PluginManager();
    public static final TextToSpeech TEXT_TO_SPEECH = new TextToSpeech();
    public static final SpeechRecognition SPEECH_RECOGNIZER = new SpeechRecognition();

    public static File DATA_DIR;

    public static void load() {
        LOGGER.info(IdentityHelper.getId());

        DATA_DIR = new File(Wrapper.getGame().mcDataDir, NAME);
        if (!DATA_DIR.exists() && !DATA_DIR.mkdir()) {
            throw new RuntimeException("Could not create save directory!");
        }

        TEXT_TO_SPEECH.load(); // TTS is very important, don't ask.
        // FIXME: HIGH memory usage (mem-leak?) when you speak, plus it just kind of sucks.
        // SPEECH_RECOGNIZER.load(); // STT is also very important.

        COMMAND_MANAGER.load();
        MODULE_MANAGER.load();
        KEYBIND_MANAGER.load();
        FRIEND_MANAGER.load();
        PLUGIN_MANAGER.load();

        Wrapper.getGame().ingameGUI = new IngameGui(Wrapper.getGame());

        EventBus.register(new EventHandler<EventProgramExit>() {
            public void handle(EventProgramExit event) {
                MODULE_MANAGER.save();
                KEYBIND_MANAGER.save();
                FRIEND_MANAGER.save();
                PLUGIN_MANAGER.save();

                TEXT_TO_SPEECH.unload();
                SPEECH_RECOGNIZER.unload();
            }
        });

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
                Wrapper.getGame().setSession(SessionHelper.MICROSOFT.createSession(username, password));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        boolean silentBoot = System.getProperty("hope.silentboot", "false").equalsIgnoreCase("true");
        if (silentBoot) return;

        TEXT_TO_SPEECH.narrate(String.format("Hello, %s.", Wrapper.getGame().getSession().getUsername()));
    }
}