package io.github.alerithe.client.features.modules.impl.combat;

import io.github.alerithe.client.events.EventTick;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.IntProperty;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.MsTimer;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;
import net.minecraft.client.settings.GameSettings;

public class AutoClicker extends Module {
    public final IntProperty minCps = new IntProperty("MinClicksPerSecond", new String[]{"mincps", "minspeed"}, 8, 1, 20);
    public final IntProperty maxCps = new IntProperty("MaxClicksPerSecond", new String[]{"maxcps", "maxspeed"}, 12, 1, 20);

    private final MsTimer timer = new MsTimer();

    public AutoClicker() {
        super("AutoClicker", new String[]{"ac"}, Type.COMBAT);

        getPropertyManager().add(minCps);
        getPropertyManager().add(maxCps);
    }

    @Register
    private void onTick(EventTick event) {
        if (!event.isInGame()) return;
        if (!GameSettings.isKeyDown(Wrapper.getSettings().keyBindAttack)) return;
        if (Wrapper.getGame().currentScreen != null) return;
        if (!timer.hasPassed(1000 / MathHelper.getRandomInt(minCps.getValue(), maxCps.getValue()))) return;

        timer.update();
        Wrapper.getGame().clickMouse();
        Wrapper.getSettings().keyBindAttack.unpressKey();
    }
}
