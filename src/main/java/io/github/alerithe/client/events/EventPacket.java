package io.github.alerithe.client.events;

import io.github.alerithe.events.CancellableEvent;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;

public class EventPacket extends CancellableEvent {
    private Packet<?> packet;
    private final NetworkManager networkManager;

    public EventPacket(Packet<?> packet, NetworkManager networkManager) {
        this.packet = packet;
        this.networkManager = networkManager;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    public static class Read extends EventPacket {
        private final INetHandler netHandler;

        public Read(Packet<?> packet, NetworkManager networkManager, INetHandler netHandler) {
            super(packet, networkManager);
            this.netHandler = netHandler;
        }

        public INetHandler getNetHandler() {
            return netHandler;
        }
    }

    public static class Write extends EventPacket {
        public Write(Packet<?> packet, NetworkManager networkManager) {
            super(packet, networkManager);
        }
    }
}
