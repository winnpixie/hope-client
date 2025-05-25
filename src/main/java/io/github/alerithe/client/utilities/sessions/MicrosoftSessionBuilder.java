package io.github.alerithe.client.utilities.sessions;

import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import net.minecraft.util.Session;

public class MicrosoftSessionBuilder implements SessionBuilder {
    private static final MicrosoftAuthenticator MICROSOFT_AUTHENTICATOR = new MicrosoftAuthenticator();

    @Override
    public Session createSession(String username, String password) throws Exception {
        MicrosoftAuthResult auth = MICROSOFT_AUTHENTICATOR.loginWithCredentials(username, password);

        return new Session(auth.getProfile().getName(),
                auth.getProfile().getId(),
                auth.getAccessToken(),
                "mojang");
    }
}
