package io.github.alerithe.client.utilities;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.lenni0451.commons.httpclient.HttpClient;
import net.minecraft.util.Session;
import net.raphimc.minecraftauth.MinecraftAuth;
import net.raphimc.minecraftauth.java.JavaAuthManager;
import net.raphimc.minecraftauth.msa.model.MsaCredentials;
import net.raphimc.minecraftauth.msa.model.MsaDeviceCode;
import net.raphimc.minecraftauth.msa.service.impl.CredentialsMsaAuthService;
import net.raphimc.minecraftauth.msa.service.impl.DeviceCodeMsaAuthService;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class SessionHelper {
    private static final HttpClient HTTP_CLIENT = MinecraftAuth.createHttpClient("msa_cred_client");
    private static final JavaAuthManager.Builder AUTH_BUILDER = JavaAuthManager.create(HTTP_CLIENT);

    private SessionHelper() {
    }

    public static Session logInWithMicrosoft(Consumer<MsaDeviceCode> consumer) throws IOException, InterruptedException, TimeoutException {
        JavaAuthManager auth = AUTH_BUILDER.login(DeviceCodeMsaAuthService::new, consumer);

        return new Session(auth.getMinecraftProfile().getUpToDate().getName(),
                auth.getMinecraftProfile().getUpToDate().getId().toString(),
                auth.getMinecraftToken().getUpToDate().getToken(),
                "mojang");
    }

    public static Session logInWithMicrosoft(String email, String pass) throws IOException, InterruptedException, TimeoutException {
        JavaAuthManager auth = AUTH_BUILDER.login(CredentialsMsaAuthService::new, new MsaCredentials(email, pass));

        return new Session(auth.getMinecraftProfile().getUpToDate().getName(),
                auth.getMinecraftProfile().getUpToDate().getId().toString(),
                auth.getMinecraftToken().getUpToDate().getToken(),
                "mojang");
    }

    public static Session logInWithMojang(String user, String pass) throws AuthenticationException {
        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(GameHelper.getGame().getProxy(), "");
        YggdrasilUserAuthentication auth = new YggdrasilUserAuthentication(service, Agent.MINECRAFT);
        auth.setUsername(user);
        auth.setPassword(pass);
        auth.logIn();

        return new Session(auth.getSelectedProfile().getName(),
                auth.getSelectedProfile().getId().toString(),
                auth.getAuthenticatedToken(),
                auth.getUserType().getName());
    }
}
