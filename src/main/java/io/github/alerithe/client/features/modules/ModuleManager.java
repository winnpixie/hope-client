package io.github.alerithe.client.features.modules;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.EventTick;
import io.github.alerithe.client.features.FeatureManager;
import io.github.alerithe.client.features.keybinds.Keybind;
import io.github.alerithe.client.features.modules.impl.combat.*;
import io.github.alerithe.client.features.modules.impl.miscellaneous.*;
import io.github.alerithe.client.features.modules.impl.movement.*;
import io.github.alerithe.client.features.modules.impl.player.*;
import io.github.alerithe.client.features.modules.impl.visual.*;
import io.github.alerithe.client.features.modules.impl.world.*;
import io.github.alerithe.events.EventBus;
import io.github.alerithe.events.EventHandler;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager extends FeatureManager<Module> {
    public File moduleDataDir;
    private boolean toggledModules;

    @Override
    public void load() {
        moduleDataDir = new File(Client.DATA_DIR, "modules");
        if (!moduleDataDir.exists()) {
            if (!moduleDataDir.mkdir()) {
                Client.LOGGER.warn("Could not create modules directory (does it already exist?)!");
            }
        }

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
        add(new FastLadder());
        add(new Flight());
        add(new InventoryMove());
        add(new LongJump());
        add(new NoSlowdown()); // TODO: Water
        add(new SafeWalk());
        add(new Speed());
        add(new Spider()); // TODO: Finish/Fix?
        add(new Step());
        add(new Sprint());

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
        add(new ChatPlus());
        add(new ClickUI());
        add(new EntityESP());
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

        Client.LOGGER.info(String.format("Registered %d Modules", getElements().size()));

        getElements().forEach(module -> {
            Client.KEYBIND_MANAGER.add(new Keybind(module.getName(), module.getAliases(), Keyboard.KEY_NONE, module::toggle));

            module.getPropertyManager().load();
        });

        setConfigFile(new File(Client.DATA_DIR, "modules.txt"));
        List<Module> toEnable = new ArrayList<>();

        try {
            Files.readAllLines(getConfigFile().toPath()).forEach(line -> {
                String[] data = line.split(":", 2);
                Module module = get(data[0]);
                if (module == null) return;
                if (data[1].equalsIgnoreCase("false")) return;

                toEnable.add(module);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        EventBus.register(new EventHandler<EventTick>() {
            @Override
            public void handle(EventTick event) {
                if (!event.isInGame()) return;

                toEnable.forEach(Module::toggle);
                toggledModules = true;
                EventBus.unregister(this);
            }
        });
    }

    public List<Module> getAllInType(Module.Type type) {
        return getElements().stream().filter(module -> module.getType().equals(type)).collect(Collectors.toList());
    }

    @Override
    public void save() {
        getElements().forEach(module -> module.getPropertyManager().save());
        if (!toggledModules) return;

        StringBuilder builder = new StringBuilder();
        getElements().forEach(module -> builder.append(module.getName()).append(':')
                .append(module.isEnabled()).append('\n'));

        try {
            Files.write(getConfigFile().toPath(), builder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
