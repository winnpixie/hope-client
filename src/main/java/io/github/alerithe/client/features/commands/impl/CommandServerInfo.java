package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.game.EventPacket;
import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.MsTimer;
import io.github.alerithe.client.utilities.NetworkHelper;
import io.github.alerithe.events.impl.EventBusImpl;
import io.github.alerithe.events.impl.Subscriber;
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
        GameHelper.printChatMessage(String.format("Name in List: \247e%s", GameHelper.getGame().getCurrentServerData().serverName));

        String[] ipAndPort = GameHelper.getGame().getCurrentServerData().serverIP.split(":");
        String addr = ipAndPort[0];
        int port = ipAndPort.length > 1 ? Integer.parseInt(ipAndPort[1]) : 25565;
        GameHelper.printChatMessage(String.format("IP: \247e%s", addr));
        GameHelper.printChatMessage(String.format("Port: \247e%s", port));
        GameHelper.printChatMessage(String.format("Brand: \247e%s", EntityHelper.getUser().getClientBrand()));

        if (args.length > 0) {
            GameHelper.printChatMessage(String.format("Searching for plugins that begin with '%s'...", args[0]));
            NetworkHelper.sendPacket(new C14PacketTabComplete("/" + args[0]));
        } else {
            GameHelper.printChatMessage("Searching for plugins...");
            NetworkHelper.sendPacket(new C14PacketTabComplete("/"));
        }

        Client.EVENT_BUS.subscribe(new Subscriber<EventPacket.Read>() {
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

                    GameHelper.printChatMessage(String.format("Detected \247a(%d)\247r Plugins", plugins.size()));
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
