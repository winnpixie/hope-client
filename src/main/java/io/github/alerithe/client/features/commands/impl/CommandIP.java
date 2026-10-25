package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.winnpixie.http4j.client.HttpClient;
import io.github.winnpixie.http4j.shared.throwables.HttpException;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class CommandIP extends Command {
    private final HttpClient http = HttpClient.newClient();

    public CommandIP() {
        super("ip", new String[0], "<resolve|query> <domain|ip>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            GameHelper.printChatMessage(ErrorMessages.NOT_ENOUGH_ARGS);
            return;
        }

        String target = args[1];
        switch (args[0].toLowerCase()) {
            case "resolve":
                try {
                    InetAddress[] resolutions = InetAddress.getAllByName(target);

                    GameHelper.printChatMessage("\247e" + resolutions.length + " address(es) found:");
                    for (int i = 0; i < resolutions.length; i++) {
                        InetAddress address = resolutions[i];
                        GameHelper.printChatMessage("\247e[" + i + "]\247r " + address.getHostAddress());
                    }
                } catch (UnknownHostException uhe) {
                    GameHelper.printChatMessage("\247cNo addresses available");

                    uhe.printStackTrace();
                }
                break;
            case "query":
                try {
                    http.sendAsync(http.newRequest()
                                    .setUrl("https://ipinfo.io/" + target + "/json")
                                    .setHeader("User-Agent", "query")
                                    .build())
                            .whenComplete(((response, throwable) -> {
                                if (throwable != null) {
                                    return;
                                }

                                GameHelper.printChatMessage("\247eQuery for\247r " + target);
                                GameHelper.printChatMessage(response.getBodyAsString());
                            }));
                } catch (HttpException he) {
                    he.printStackTrace();
                }
                break;
            default:
                GameHelper.printChatMessage(ErrorMessages.INVALID_ARG);
                break;
        }
    }
}
