package io.github.alerithe.client.features.modules.impl.miscellaneous;

import io.github.alerithe.client.events.game.EventPacket;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.miscellaneous.pingspoof.NullPing;
import io.github.alerithe.client.features.modules.impl.miscellaneous.pingspoof.SpoofMode;
import io.github.alerithe.client.features.modules.impl.miscellaneous.pingspoof.ZeroPing;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.events.impl.Subscribe;

public class PingSpoof extends Module {
    private final ObjectProperty<SpoofMode> mode = new ObjectProperty<>("Mode", new String[0], new ZeroPing(),
            new NullPing());

    public PingSpoof() {
        super("PingSpoof", new String[0], Type.MISCELLANEOUS);

        getPropertyManager().add(mode);
    }

    @Subscribe
    private void onPacketRead(EventPacket.Read event) {
        if (GameHelper.getGame().isSingleplayer()) return;

        mode.getValue().onPacketRead(event);
    }
}
