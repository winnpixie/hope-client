package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.WorldHelper;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;

public class CommandRejoin extends Command {
    public CommandRejoin() {
        super("rejoin", new String[]{"relog", "reconnect"}, "");
    }

    @Override
    public void execute(String[] args) {
        if (GameHelper.getGame().getCurrentServerData() == null || GameHelper.getGame().isSingleplayer()) {
            GameHelper.printChatMessage(ErrorMessages.format("No server detected, are you in single-player?"));
            return;
        }

        ServerData serverData = GameHelper.getGame().getCurrentServerData();

        WorldHelper.getWorld().sendQuittingDisconnectingPacket();
        GameHelper.getGame().loadWorld(null);
        GameHelper.getGame().displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
        GameHelper.getGame().displayGuiScreen(new GuiConnecting(new GuiMultiplayer(new GuiMainMenu()), GameHelper.getGame(),
                serverData));

        GameHelper.getGame().setServerData(serverData);
    }
}
