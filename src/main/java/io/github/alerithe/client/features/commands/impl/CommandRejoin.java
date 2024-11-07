package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.Wrapper;
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
        if (Wrapper.getGame().getCurrentServerData() == null || Wrapper.getGame().isSingleplayer()) {
            Wrapper.printChat(ErrorMessages.format("No server detected, are you in single-player?"));
            return;
        }

        ServerData serverData = Wrapper.getGame().getCurrentServerData();

        Wrapper.getWorld().sendQuittingDisconnectingPacket();
        Wrapper.getGame().loadWorld(null);
        Wrapper.getGame().displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
        Wrapper.getGame().displayGuiScreen(new GuiConnecting(new GuiMultiplayer(new GuiMainMenu()), Wrapper.getGame(),
                serverData));

        Wrapper.getGame().setServerData(serverData);
    }
}
