package io.github.alerithe.client.features.modules.impl.world;

import io.github.alerithe.client.events.game.EventGameScreen;
import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.IntProperty;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.Stopwatch;
import io.github.alerithe.events.impl.Subscribe;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class ChestStealer extends Module {
    private final IntProperty cps = new IntProperty("CPS", new String[0],
            15, 1, 20);

    private final Stopwatch timer = new Stopwatch();

    private int index;

    public ChestStealer() {
        super("ChestStealer", new String[]{"autoloot", "autosteal"}, Type.WORLD);

        getPropertyManager().add(cps);
    }

    @Subscribe
    private void onScreenOpen(EventGameScreen.Open event) {
        if (!(event.getScreen() instanceof GuiChest)) return;

        index = 0;
        timer.update();
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
            timer.update();
        }
        index++;
    }

    // TODO: Move to a utility class eventually
    private boolean isFull(Container container) {
        for (Slot slot : container.inventorySlots) {
            if (!slot.getHasStack()) return false;
        }

        return true;
    }
}
