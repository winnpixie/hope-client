package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.Wrapper;
import net.minecraft.network.play.client.C03PacketPlayer;

public class CommandForward extends Command {
    public CommandForward() {
        super("forward", new String[]{"fw"}, "<distance>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            Wrapper.printChat(ErrorMessages.NOT_ENOUGH_ARGS);
            return;
        }

        if (!MathHelper.isDouble(args[0])) {
            Wrapper.printChat(ErrorMessages.INVALID_ARG_TYPE);
            return;
        }

        double distance = Double.parseDouble(args[0]);
        float[] vector = Wrapper.getPlayer().getLookVector();
        if (MathHelper.abs(distance) > 1.28) {
            Wrapper.getGame().timer.timerSpeed = 0f;
            for (double i = 0; i < MathHelper.abs(distance) * 3; i++) {
                double x;
                double z;

                if (distance < 0) {
                    x = Wrapper.getPlayer().posX - (vector[0] * 0.3);
                    z = Wrapper.getPlayer().posZ - (vector[1] * 0.3);
                } else {
                    x = Wrapper.getPlayer().posX + (vector[0] * 0.3);
                    z = Wrapper.getPlayer().posZ + (vector[1] * 0.3);
                }

                Wrapper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, Wrapper.getPlayer().posY, z,
                        Wrapper.getPlayer().onGround));
                Wrapper.getPlayer().setPosition(x, Wrapper.getPlayer().posY, z);
            }
            Wrapper.getGame().timer.timerSpeed = 1f;
        } else {
            double x = Wrapper.getPlayer().posX + (vector[0] * distance);
            double z = Wrapper.getPlayer().posZ + (vector[1] * distance);
            Wrapper.getPlayer().setPosition(x, Wrapper.getPlayer().posY, z);
        }
    }
}
