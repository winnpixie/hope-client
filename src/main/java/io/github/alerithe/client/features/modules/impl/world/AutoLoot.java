package io.github.alerithe.client.features.modules.impl.world;

import io.github.alerithe.client.events.bus.Subscribe;
import io.github.alerithe.client.events.game.EventGameScreen;
import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.IntProperty;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.Stopwatch;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Slot;

public class AutoLoot extends Module {
    private final IntProperty cps = new IntProperty("CPS", new String[0],
            15, 1, 20);

    private final Stopwatch timer = new Stopwatch();

    private int index;

    public AutoLoot() {
        super("AutoLoot", new String[]{"cheststealer", "autosteal"}, Type.WORLD);

        getPropertyManager().add(cps);
    }

    @Subscribe
    private void onScreenOpen(EventGameScreen.Open event) {
        if (!(event.getScreen() instanceof GuiChest)) return;

        index = 0;
        timer.reset();
    }

    @Subscribe
    private void onPreUpdate(EventUpdate.Pre event) {
        if (!(GameHelper.getGame().currentScreen instanceof GuiChest)) return;

        GuiChest gui = (GuiChest) GameHelper.getGame().currentScreen;
        int slots = gui.inventoryRows * 9;
        if (index >= slots || EntityHelper.getUser().isInventoryFull()) {
            EntityHelper.getUser().closeScreen();
            return;
        }

        // FIXME: Actual clicks/sec does not match expected values?
        if (!timer.hasPassed(1000 / cps.getValue())) return;

        Slot slot = gui.inventorySlots.inventorySlots.get(index);
        if (slot.getHasStack()) {
            EntityHelper.getUser().clickWindow(gui.inventorySlots.windowId, index, 0, 1);
            timer.reset();
        }
        index++;
    }
}
