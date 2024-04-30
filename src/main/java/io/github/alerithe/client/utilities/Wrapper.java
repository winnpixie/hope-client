package io.github.alerithe.client.utilities;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import io.github.alerithe.client.extensions.LocalPlayer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.Packet;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Session;

import java.util.UUID;

/**
 * Using a wrapper just like its 2015 again
 */
public class Wrapper {
    private static final MicrosoftAuthenticator MICROSOFT_AUTHENTICATOR = new MicrosoftAuthenticator();

    public static Minecraft getMC() {
        return Minecraft.getMinecraft();
    }

    public static FontRenderer getFontRenderer() {
        return getMC().fontRenderer;
    }

    public static PlayerControllerMP getPlayerController() {
        return getMC().playerController;
    }

    public static GameSettings getGameSettings() {
        return getMC().gameSettings;
    }

    public static LocalPlayer getPlayer() {
        return getMC().player;
    }

    public static WorldClient getWorld() {
        return getMC().world;
    }

    public static NetworkPlayerInfo getNetInfo(UUID uuid) {
        return getPlayer().sendQueue.getPlayerInfo(uuid);
    }

    public static Block getBlock(BlockPos pos) {
        return getWorld().getBlockState(pos).getBlock();
    }

    public static void sendPacket(Packet<?> packet) {
        getPlayer().sendQueue.addToSendQueue(packet);
    }

    public static void printChat(String message) {
        getMC().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(message));
    }

    public static Session createMicrosoftSession(String username, String password) throws MicrosoftAuthenticationException {
        MicrosoftAuthResult auth = MICROSOFT_AUTHENTICATOR.loginWithCredentials(username, password);

        return new Session(auth.getProfile().getName(),
                auth.getProfile().getId(),
                auth.getAccessToken(),
                "mojang");
    }

    public static Session createMojangSession(String username, String password) throws AuthenticationException {
        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(getMC().getProxy(), "");
        YggdrasilUserAuthentication auth = new YggdrasilUserAuthentication(service, Agent.MINECRAFT);
        auth.setUsername(username);
        auth.setPassword(password);
        auth.logIn();

        return new Session(auth.getSelectedProfile().getName(),
                auth.getSelectedProfile().getId().toString(),
                auth.getAuthenticatedToken(),
                auth.getUserType().getName());
    }
}
