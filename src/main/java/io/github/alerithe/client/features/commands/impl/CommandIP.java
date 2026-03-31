package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.http.HttpClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class CommandIP extends Command {
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

                    GameHelper.printChatMessage(String.format("\247e%d address(es) found:", resolutions.length));
                    for (int i = 0; i < resolutions.length; i++) {
                        InetAddress address = resolutions[i];
                        GameHelper.printChatMessage(String.format("\247e[%d]\247r %s", i, address.getHostAddress()));
                    }
                } catch (UnknownHostException uhe) {
                    GameHelper.printChatMessage("\247cNo addresses available");

                    uhe.printStackTrace();
                }
                break;
            case "query":
                try {
                    String infoBody = HttpClient.send(
                            HttpClient.newRequest()
                                    .url("https://ipinfo.io/" + target + "/json")
                                    .header("User-Agent", "ipinfo")
                                    .build()
                    ).getBodyAsString();

                    GameHelper.printChatMessage(String.format("\247eQuery for\247r %s", target));
                    GameHelper.printChatMessage(infoBody);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                break;
            default:
                GameHelper.printChatMessage(ErrorMessages.INVALID_ARG);
                break;
        }
    }
}
