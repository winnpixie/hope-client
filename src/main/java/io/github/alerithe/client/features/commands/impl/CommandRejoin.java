package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.features.commands.Command;
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
        if (Wrapper.getMC().getCurrentServerData() == null || Wrapper.getMC().isSingleplayer()) {
            Wrapper.printChat("\247cNo server detected, are you in single-player?");
            return;
        }

        ServerData serverData = Wrapper.getMC().getCurrentServerData();

        Wrapper.getWorld().sendQuittingDisconnectingPacket();
        Wrapper.getMC().loadWorld(null);
        Wrapper.getMC().displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
        Wrapper.getMC().displayGuiScreen(new GuiConnecting(new GuiMultiplayer(new GuiMainMenu()), Wrapper.getMC(),
                serverData));

        Wrapper.getMC().setServerData(serverData);
    }
}
