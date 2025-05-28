package io.github.alerithe.client.utilities.sessions;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class MojangSessionBuilder implements SessionBuilder {
    @Override
    public Session createSession(String username, String password) throws Exception {
        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Minecraft.getInstance().getProxy(), "");
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
