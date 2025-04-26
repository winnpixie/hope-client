package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.events.EventPacket;
import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.MsTimer;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.EventBus;
import io.github.alerithe.events.EventHandler;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;

import java.util.HashSet;
import java.util.Set;

public class CommandServerInfo extends Command {
    public CommandServerInfo() {
        super("serverinfo", new String[]{"si"}, "[text]");
    }

    @Override
    public void execute(String[] args) {
        if (Wrapper.getGame().getCurrentServerData() == null || Wrapper.getGame().isSingleplayer()) {
            Wrapper.printMessage(ErrorMessages.format("No server detected, are you in single-player?"));
            return;
        }

        Wrapper.printMessage("\247eServer Information:");
        Wrapper.printMessage(String.format("Name in List: \247e%s", Wrapper.getGame().getCurrentServerData().serverName));

        String[] ipAndPort = Wrapper.getGame().getCurrentServerData().serverIP.split(":");
        String addr = ipAndPort[0];
        int port = ipAndPort.length > 1 ? Integer.parseInt(ipAndPort[1]) : 25565;
        Wrapper.printMessage(String.format("IP: \247e%s", addr));
        Wrapper.printMessage(String.format("Port: \247e%s", port));
        Wrapper.printMessage(String.format("Brand: \247e%s", Wrapper.getPlayer().getClientBrand()));
        if (args.length > 0) {
            Wrapper.printMessage(String.format("Searching for plugins that begin with '%s'...", args[0]));
            Wrapper.sendPacket(new C14PacketTabComplete("/" + args[0]));
        } else {
            Wrapper.printMessage("Searching for plugins...");
            Wrapper.sendPacket(new C14PacketTabComplete("/"));
        }

        EventBus.register(new EventHandler<EventPacket.Read>() {
            private final MsTimer timer = new MsTimer();

            @Override
            public void handle(EventPacket.Read event) {
                if (event.getPacket() instanceof S3APacketTabComplete) {
                    S3APacketTabComplete packet = (S3APacketTabComplete) event.getPacket();
                    Set<String> plugins = new HashSet<>();
                    for (String match : packet.getMatches()) {
                        if (!match.contains(":") || !match.startsWith("/")) continue;

                        plugins.add(match.substring(1).split(":")[0].toLowerCase());
                    }

                    Wrapper.printMessage(String.format("Detected \247a(%d)\247r Plugins", plugins.size()));
                    Wrapper.printMessage("\247a" + String.join("\247f, \247a", plugins));
                    EventBus.unregister(this);
                } else if (timer.hasPassed(20000)) {
                    EventBus.unregister(this);
                    Wrapper.printMessage(ErrorMessages.format("Could not receive plugin information within 20 seconds."));
                }
            }
        });
    }
}
