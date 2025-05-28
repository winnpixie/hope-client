package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.NetworkHelper;
import net.minecraft.network.play.client.C03PacketPlayer;

public class CommandForward extends Command {
    public CommandForward() {
        super("forward", new String[]{"fw"}, "<distance>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            GameHelper.printChatMessage(ErrorMessages.NOT_ENOUGH_ARGS);
            return;
        }

        if (!MathHelper.isDouble(args[0])) {
            GameHelper.printChatMessage(ErrorMessages.INVALID_ARG_TYPE);
            return;
        }

        double distance = Double.parseDouble(args[0]);
        float[] vector = EntityHelper.getUser().getLookVector();
        if (MathHelper.abs(distance) > 1.28) {
            GameHelper.getGame().timer.timerSpeed = 0f;
            for (double i = 0; i < MathHelper.abs(distance) * 3; i++) {
                double x;
                double z;

                if (distance < 0) {
                    x = EntityHelper.getUser().posX - (vector[0] * 0.3);
                    z = EntityHelper.getUser().posZ - (vector[1] * 0.3);
                } else {
                    x = EntityHelper.getUser().posX + (vector[0] * 0.3);
                    z = EntityHelper.getUser().posZ + (vector[1] * 0.3);
                }

                NetworkHelper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, EntityHelper.getUser().posY, z,
                        EntityHelper.getUser().onGround));
                EntityHelper.getUser().setPosition(x, EntityHelper.getUser().posY, z);
            }
            GameHelper.getGame().timer.timerSpeed = 1f;
        } else {
            double x = EntityHelper.getUser().posX + (vector[0] * distance);
            double z = EntityHelper.getUser().posZ + (vector[1] * distance);
            EntityHelper.getUser().setPosition(x, EntityHelper.getUser().posY, z);
        }
    }
}
