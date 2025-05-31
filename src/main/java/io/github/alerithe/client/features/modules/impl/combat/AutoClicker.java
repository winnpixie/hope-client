package io.github.alerithe.client.features.modules.impl.combat;

import io.github.alerithe.client.events.game.EventTick;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.IntProperty;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.Stopwatch;
import io.github.alerithe.events.impl.Subscribe;
import net.minecraft.client.settings.GameSettings;

public class AutoClicker extends Module {
    public final IntProperty minCps = new IntProperty("MinClicksPerSecond", new String[]{"mincps", "minspeed"},
            8, 1, 20);
    public final IntProperty maxCps = new IntProperty("MaxClicksPerSecond", new String[]{"maxcps", "maxspeed"},
            12, 1, 20);

    private final Stopwatch timer = new Stopwatch();

    public AutoClicker() {
        super("AutoClicker", new String[]{"ac"}, Type.COMBAT);

        getPropertyManager().add(minCps);
        getPropertyManager().add(maxCps);
    }

    @Subscribe
    private void onEndTick(EventTick.End event) {
        if (!event.isInGame()) return;
        if (!GameSettings.isKeyDown(GameHelper.getSettings().keyBindAttack)) return;
        if (GameHelper.getGame().currentScreen != null) return;
        if (!timer.hasPassed(1000 / MathHelper.getRandomInt(minCps.getValue(), maxCps.getValue()))) return;

        timer.update();
        GameHelper.getGame().clickMouse();
        GameHelper.getSettings().keyBindAttack.unpressKey();
    }
}
