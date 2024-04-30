package io.github.alerithe.client.features.modules.impl.combat;

import io.github.alerithe.client.events.EventTick;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.NumberProperty;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.Timer;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;
import net.minecraft.client.settings.GameSettings;

public class AutoClicker extends Module {
    public final NumberProperty<Integer> minCps = new NumberProperty<>("MinClicksPerSecond", new String[]{"mincps", "minspeed"}, 8, 1, 20);
    public final NumberProperty<Integer> maxCps = new NumberProperty<>("MaxClicksPerSecond", new String[]{"maxcps", "maxspeed"}, 12, 1, 20);

    private final Timer timer = new Timer();

    public AutoClicker() {
        super("AutoClicker", new String[]{"ac"}, Type.COMBAT);

        getPropertyManager().add(minCps);
        getPropertyManager().add(maxCps);
    }

    @Register
    private void onTick(EventTick event) {
        if (!event.isInGame()) return;
        if (!GameSettings.isKeyDown(Wrapper.getGameSettings().keyBindAttack)) return;
        if (Wrapper.getMC().currentScreen != null) return;
        if (!timer.hasPassed(1000 / (long) MathHelper.randomd(minCps.getValue(), maxCps.getValue()))) return;

        timer.update();
        Wrapper.getMC().clickMouse();
        Wrapper.getGameSettings().keyBindAttack.unpressKey();
    }
}
