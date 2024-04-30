package io.github.alerithe.client.features.modules.impl.combat;

import io.github.alerithe.client.features.modules.Module;

public class AutoHeal extends Module {
    public AutoHeal() {
        super("AutoHeal", new String[]{"autopot", "autopotion", "autosoup"}, Type.COMBAT);
    }
}
