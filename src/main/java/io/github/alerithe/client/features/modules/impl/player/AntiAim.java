package io.github.alerithe.client.features.modules.impl.player;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.player.antiaim.RotationMode;
import io.github.alerithe.client.features.modules.impl.player.antiaim.pitch.Headless;
import io.github.alerithe.client.features.modules.impl.player.antiaim.pitch.NormalPitch;
import io.github.alerithe.client.features.modules.impl.player.antiaim.pitch.SpinPitch;
import io.github.alerithe.client.features.modules.impl.player.antiaim.yaw.Backwards;
import io.github.alerithe.client.features.modules.impl.player.antiaim.yaw.LBYBreaker;
import io.github.alerithe.client.features.modules.impl.player.antiaim.yaw.NormalYaw;
import io.github.alerithe.client.features.modules.impl.player.antiaim.yaw.SpinYaw;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;
import io.github.alerithe.events.impl.Subscribe;

public class AntiAim extends Module {
    private final ObjectProperty<RotationMode> yawMode = new ObjectProperty<>("YawMode", new String[0], new SpinYaw(),
            new Backwards(), new LBYBreaker(), new NormalYaw());
    private final ObjectProperty<RotationMode> pitchMode = new ObjectProperty<>("PitchMode", new String[0], new Headless(),
            new SpinPitch(), new NormalPitch());

    public AntiAim() {
        super("AntiAim", new String[]{"derp"}, Type.PLAYER);

        getPropertyManager().add(yawMode);
        getPropertyManager().add(pitchMode);
    }

    @Subscribe
    private void onPreUpdate(EventUpdate.Pre event) {
        yawMode.getValue().onPreUpdate(event);
        pitchMode.getValue().onPreUpdate(event);
    }
}
