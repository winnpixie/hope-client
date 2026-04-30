package io.github.alerithe.client.features.modules;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.bus.Subscriber;
import io.github.alerithe.client.events.game.EventTick;
import io.github.alerithe.client.features.FeatureManager;
import io.github.alerithe.client.features.keybinds.Keybind;
import io.github.alerithe.client.features.modules.impl.combat.*;
import io.github.alerithe.client.features.modules.impl.miscellaneous.*;
import io.github.alerithe.client.features.modules.impl.movement.*;
import io.github.alerithe.client.features.modules.impl.player.*;
import io.github.alerithe.client.features.modules.impl.visual.*;
import io.github.alerithe.client.features.modules.impl.world.*;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ModuleManager extends FeatureManager<Module> {
    private Path toggleRestorePath;
    private boolean restoredToggles = false;

    private final Map<Module.Type, List<Module>> modulesInType = new EnumMap<>(Module.Type.class);

    @Override
    public void load() {
        setDataPath(Client.DATA_PATH.resolve("modules"));
        if (Files.notExists(getDataPath())) {
            try {
                Files.createDirectory(getDataPath());
            } catch (IOException ioe) {
                Client.LOGGER.warn("Could not create modules directory (does it already exist?)!");
            }
        }

        this.toggleRestorePath = getDataPath().resolve("RESTORE_ON_PLAY");

        for (Module.Type type : Module.Type.values()) {
            modulesInType.put(type, new ArrayList<>());
        }

        // Combat
        add(new AntiBot());
        add(new AutoArmor()); // TODO: Finish
        add(new AutoAttack());
        add(new AutoClicker());
        add(new AutoHeal()); // TODO: Finish
        add(new Criticals());
        add(new KillAura());
        add(new QuickDraw()); // TODO: Finish
        add(new SuperKnockback());
        add(new Velocity());

        // Movement
        add(new AntiPlate());
        add(new AutoJump());
        add(new AutoSprint());
        add(new AutoWalk());
        add(new Flight());
        add(new InventoryMove());
        add(new LadderBoost());
        add(new LongJump());
        add(new NoSlowdown());
        add(new SafeWalk());
        add(new Speed());
        add(new Spider());
        add(new Step());

        // Player
        add(new AntiAim());
        add(new AutoRespawn());
        add(new Blink());
        add(new FastHeal());
        add(new Freecam());
        add(new InventoryCleaner()); // TODO: Finish
        add(new NoFall());
        add(new NoHunger());
        add(new NoRotate());
        add(new QuickConsume());

        // Visual
        add(new BreadCrumbs());
        add(new ChatPlus());
        add(new ClickUI());
        add(new EntityESP());
        add(new F5360());
        add(new FullBright());
        add(new HUD());
        add(new NameTags());
        add(new Radar());
        add(new StorageESP());
        add(new TabUI());
        add(new Tracers());
        add(new WhatAmILookingAt()); // TODO: Finish

        // World
        add(new AutoBridge());
        add(new AutoLoot());
        add(new AutoShear());
        add(new FastPlace());
        add(new NoPrick());
        add(new Nuker()); // TODO: Finish
        add(new Phase());
        add(new WaterWalk());

        // Misc
        add(new AntiExploit());
        add(new AutoCaptcha());
        add(new ConsoleSpam());
        add(new Crasher());
        add(new CreativeDrop());
        add(new FireUnwork());
        add(new GameSpeed());
        add(new MiddleClickFriend());
        add(new MurderMystery()); // TODO: Finish
        add(new PingSpoof());
        add(new TextSpammer()); // TODO: Finish

        Client.LOGGER.info("Registered {} Module(s)", getElements().size());

        getElements().forEach(module -> {
            Client.KEYBIND_MANAGER.add(
                    new Keybind(module.getName(), module.getAliases(), Keyboard.KEY_NONE, module::toggle));

            module.getPropertyManager().load();
        });

        List<Module> toBeToggled = new ArrayList<>(getElements().size());
        try {
            Files.readAllLines(toggleRestorePath).forEach(line -> {
                Module module = find(line);
                if (module == null) return;

                toBeToggled.add(module);
            });
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        Client.EVENT_BUS.subscribe(EventTick.End.class, new Subscriber<EventTick.End>() {
            @Override
            public void handle(EventTick.End event) {
                if (!event.isInGame()) return;
                if (restoredToggles) return; // just in case?

                toBeToggled.forEach(Module::toggle);
                restoredToggles = true;
                Client.EVENT_BUS.unsubscribe(this);
            }
        });
    }

    @Override
    public void add(Module feature) {
        modulesInType.get(feature.getType())
                .add(feature);

        super.add(feature);
    }

    public List<Module> allOfType(Module.Type type) {
        return modulesInType.get(type);
    }

    @Override
    public void save() {
        getElements().forEach(module -> module.getPropertyManager().save());
        if (!restoredToggles) return;

        StringBuilder builder = new StringBuilder();

        for (Module module : getElements()) {
            if (!module.isEnabled()) {
                continue;
            }

            builder.append(module.getName()).append('\n');
        }

        try {
            Files.write(toggleRestorePath, builder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
