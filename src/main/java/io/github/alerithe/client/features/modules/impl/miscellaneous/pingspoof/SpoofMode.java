package io.github.alerithe.client.features.modules.impl.miscellaneous.pingspoof;

import io.github.alerithe.client.events.EventPacket;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;

public class SpoofMode extends ObjectProperty.Value {
    public SpoofMode(String name, String[] aliases) {
        super(name, aliases);
    }

    public void onPacketRead(EventPacket.Read event) {
    }
}
