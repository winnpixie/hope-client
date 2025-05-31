package io.github.alerithe.client.features.modules;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.game.EventTick;
import io.github.alerithe.client.features.FeatureManager;
import io.github.alerithe.client.features.keybinds.Keybind;
import io.github.alerithe.client.features.modules.impl.combat.*;
import io.github.alerithe.client.features.modules.impl.miscellaneous.*;
import io.github.alerithe.client.features.modules.impl.movement.*;
import io.github.alerithe.client.features.modules.impl.player.*;
import io.github.alerithe.client.features.modules.impl.visual.*;
import io.github.alerithe.client.features.modules.impl.world.*;
import io.github.alerithe.events.Subscriber;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager extends FeatureManager<Module> {
    private File moduleStatesFile;
    private boolean restoredStates;

    @Override
    public void load() {
        setConfigurationFile(new File(Client.DATA_DIR, "modules"));
        if (!getConfigurationFile().exists() && !getConfigurationFile().mkdir()) {
            Client.LOGGER.warn("Could not create modules directory (does it already exist?)!");
        }

        moduleStatesFile = new File(getConfigurationFile(), "enabled.txt");

        // Combat
        add(new AntiBot());
        add(new AutoArmor()); // TODO: Finish
        add(new AutoClicker());
        add(new AutoHeal()); // TODO: Finish
        add(new Criticals());
        add(new KillAura());
        add(new SuperKnockback());
        add(new Triggerbot()); // TODO: Finish
        add(new Velocity());

        // Movement
        add(new AntiPlate());
        add(new AutoJump());
        add(new AutoSprint());
        add(new AutoWalk());
        add(new FastLadder());
        add(new Flight());
        add(new InventoryMove());
        add(new LongJump());
        add(new NoSlowdown()); // TODO: Water
        add(new SafeWalk());
        add(new Speed());
        add(new Spider()); // TODO: Finish/Fix?
        add(new Step());

        // Player
        add(new AntiAim());
        add(new AutoRespawn());
        add(new AutoShear());
        add(new Blink());
        add(new FastUse());
        add(new Freecam());
        add(new InventoryCleaner()); // TODO: Finish
        add(new NoFall());
        add(new NoRotate());
        add(new Regen());

        // Visual
        add(new Breadcrumbs()); // TODO: Finish
        add(new ChatPlus());
        add(new ClickUI());
        add(new EntityESP());
        add(new F5360());
        add(new FullBright());
        add(new HUD());
        add(new NameTags());
        add(new Radar());
        add(new StorageESP()); // TODO: Finish, also maybe just add this to ESP instead?
        add(new TabUI());
        add(new Tracers());

        // World
        add(new ChestStealer());
        add(new FastPlace());
        add(new Jesus());
        add(new NoPrick());
        add(new Nuker()); // TODO: Finish
        add(new Phase());
        add(new ScaffoldWalk());

        // Misc
        add(new AntiExploit());
        add(new AutoCaptcha());
        add(new Crasher());
        add(new CreativeDrop());
        add(new FireUnwork());
        add(new GameSpeed());
        add(new LogSpammer());
        add(new MiddleClickFriend());
        add(new NoHunger());
        add(new PingSpoof());
        add(new TextSpammer()); // TODO: Finish

        Client.LOGGER.info(String.format("Registered %d Module(s)", getChildren().size()));

        getChildren().forEach(module -> {
            Client.KEYBIND_MANAGER.add(new Keybind(module.getName(), module.getAliases(), Keyboard.KEY_NONE, module::toggle));

            module.getPropertyManager().load();
        });

        List<Module> toEnable = new ArrayList<>();

        try {
            Files.readAllLines(moduleStatesFile.toPath()).forEach(line -> {
                String[] data = line.split(":", 2);
                Module module = find(data[0]);
                if (module == null) return;
                if (data[1].equalsIgnoreCase("false")) return;

                toEnable.add(module);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        Client.EVENT_BUS.subscribe(EventTick.End.class, new Subscriber<EventTick.End>() {
            @Override
            public void handle(EventTick.End event) {
                if (!event.isInGame()) return;
                if (restoredStates) return; // Just in case.

                toEnable.forEach(Module::toggle);
                restoredStates = true;
                Client.EVENT_BUS.unsubscribe(this);
            }
        });
    }

    public List<Module> getAllInType(Module.Type type) {
        return getChildren().stream().filter(module -> module.getType().equals(type)).collect(Collectors.toList());
    }

    @Override
    public void save() {
        getChildren().forEach(module -> module.getPropertyManager().save());
        if (!restoredStates) return;

        StringBuilder builder = new StringBuilder();
        getChildren().forEach(module -> builder.append(module.getName()).append(':')
                .append(module.isEnabled()).append('\n'));

        try {
            Files.write(moduleStatesFile.toPath(), builder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
