package io.github.alerithe.client.features.modules.impl.miscellaneous;

import io.github.alerithe.client.events.EventPacket;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.events.Register;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoCaptcha extends Module {
    // &cPlease type '&6[captcha]&c' to continue sending messages/commands.
    private final Pattern pattern = Pattern.compile("\247cPlease type '\2476(.+)\247c' to continue sending messages/commands\\.");

    public AutoCaptcha() {
        super("AutoCaptcha", new String[0], Type.MISCELLANEOUS);
        hidden.setValue(true);
    }

    @Register
    private void onPacketRead(EventPacket.Read event) {
        if (!(event.getPacket() instanceof S02PacketChat)) return;

        S02PacketChat packet = (S02PacketChat) event.getPacket();
        if (!packet.isChat()) return;

        String message = packet.getChatComponent().getFormattedText();
        Matcher matcher = pattern.matcher(message);
        if (!matcher.matches()) return;
        if (matcher.groupCount() < 1) return;

        String key = matcher.group(1);
        event.getNetworkManager().sendPacket(new C01PacketChatMessage(key));
    }
}
