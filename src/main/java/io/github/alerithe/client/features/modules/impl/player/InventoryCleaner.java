package io.github.alerithe.client.features.modules.impl.player;

import io.github.alerithe.client.features.modules.Module;

public class InventoryCleaner extends Module {
    public InventoryCleaner() {
        super("InventoryCleaner", new String[]{"invcleaner", "inventoryclean", "invclean"}, Type.PLAYER);
    }
}
