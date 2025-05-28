package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.NetworkHelper;
import io.github.alerithe.client.utilities.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSign;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;

public class CommandEditSign extends Command {
    public CommandEditSign() {
        super("editsign", new String[]{"sign", "es"}, "<Line 1\\nLine 2\\nLine 3\\nLine 4>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            GameHelper.printChatMessage(ErrorMessages.NOT_ENOUGH_ARGS);
            return;
        }

        String[] lines = String.join(" ", args).split("\\\\n", 4);

        if (GameHelper.getGame().objectMouseOver == null
                || GameHelper.getGame().objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
            GameHelper.printChatMessage("\247cYou must be hovering over a sign.");
            return;
        }
        BlockPos pos = GameHelper.getGame().objectMouseOver.getBlockPos();
        Block block = WorldHelper.getBlock(pos);
        if (!(block instanceof BlockSign)) {
            GameHelper.printChatMessage("\247cYou must be hovering over a sign.");
            return;
        }

        ChatComponentText[] components = new ChatComponentText[4];
        for (int i = 0; i < 4; i++) {
            components[i] = i < lines.length ? new ChatComponentText(lines[i]) : new ChatComponentText("");
        }

        NetworkHelper.sendPacket(new C12PacketUpdateSign(pos, components));
    }
}
