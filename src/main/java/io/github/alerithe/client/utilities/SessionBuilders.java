package io.github.alerithe.client.utilities;

import io.github.alerithe.client.utilities.sessions.MicrosoftSessionBuilder;
import io.github.alerithe.client.utilities.sessions.MojangSessionBuilder;

public class SessionBuilders {
    public static final MojangSessionBuilder MOJANG = new MojangSessionBuilder();
    public static final MicrosoftSessionBuilder MICROSOFT = new MicrosoftSessionBuilder();
}
