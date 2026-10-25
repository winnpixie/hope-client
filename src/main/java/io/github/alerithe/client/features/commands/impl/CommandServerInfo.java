package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.bus.Subscriber;
import io.github.alerithe.client.events.game.EventPacket;
import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.NetworkHelper;
import io.github.alerithe.client.utilities.Stopwatch;
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
        if (GameHelper.getGame().getCurrentServerData() == null || GameHelper.getGame().isSingleplayer()) {
            GameHelper.printChatMessage(ErrorMessages.format("No server detected, are you in single-player?"));
            return;
        }

        GameHelper.printChatMessage("\247eServer Information:");
        GameHelper.printChatMessage("Name in List: \247e" + GameHelper.getGame().getCurrentServerData().serverName);

        String[] ipAndPort = GameHelper.getGame().getCurrentServerData().serverIP.split(":");
        String addr = ipAndPort[0];
        int port = ipAndPort.length > 1 ? Integer.parseInt(ipAndPort[1]) : 25565;
        GameHelper.printChatMessage("IP: \247e" + addr);
        GameHelper.printChatMessage("Port: \247e" + port);
        GameHelper.printChatMessage("Brand: \247e" + EntityHelper.getUser().getClientBrand());

        if (args.length > 0) {
            GameHelper.printChatMessage("Searching for plugins that begin with '" + args[0] + "'...");
            NetworkHelper.sendPacket(new C14PacketTabComplete("/" + args[0]));
        } else {
            GameHelper.printChatMessage("Searching for plugins...");
            NetworkHelper.sendPacket(new C14PacketTabComplete("/"));
        }

        Client.EVENT_BUS.subscribe(EventPacket.Read.class, new Subscriber<EventPacket.Read>() {
            private final Stopwatch timer = new Stopwatch();

            @Override
            public void handle(EventPacket.Read event) {
                if (event.getPacket() instanceof S3APacketTabComplete) {
                    S3APacketTabComplete packet = (S3APacketTabComplete) event.getPacket();
                    Set<String> plugins = new HashSet<>();
                    for (String match : packet.func_149630_c()) {
                        if (!match.contains(":") || !match.startsWith("/")) continue;

                        plugins.add(match.substring(1).split(":")[0].toLowerCase());
                    }

                    GameHelper.printChatMessage("Detected \247a(" + plugins.size() + ")\247r plugin(s)");
                    GameHelper.printChatMessage("\247a" + String.join("\247f, \247a", plugins));
                    Client.EVENT_BUS.unsubscribe(this);
                } else if (timer.hasPassed(20000)) {
                    Client.EVENT_BUS.unsubscribe(this);
                    GameHelper.printChatMessage(ErrorMessages.format("Could not receive plugin information within 20 seconds."));
                }
            }
        });
    }
}
