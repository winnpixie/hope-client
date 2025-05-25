package io.github.alerithe.client.utilities.sessions;

import net.minecraft.util.Session;

public interface SessionBuilder {
    Session createSession(String username, String password) throws Exception;
}
