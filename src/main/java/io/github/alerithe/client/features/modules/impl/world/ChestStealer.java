package io.github.alerithe.client.features.modules.impl.world;

import io.github.alerithe.client.events.EventGuiOpen;
import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.NumberProperty;
import io.github.alerithe.client.utilities.Timer;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Slot;

public class ChestStealer extends Module {
    private final NumberProperty<Integer> cps = new NumberProperty<>("CPS", new String[0], 15, 1, 20);

    private final Timer timer = new Timer();

    private int index;

    public ChestStealer() {
        super("ChestStealer", new String[]{"autoloot", "autosteal"}, Type.WORLD);

        getPropertyManager().add(cps);
    }

    @Register
    private void onGuiOpen(EventGuiOpen event) {
        if (!(event.getScreen() instanceof GuiChest)) return;

        index = 0;
        timer.update();
    }

    @Register
    private void onPreUpdate(EventUpdate.Pre event) {
        if (!(Wrapper.getMC().currentScreen instanceof GuiChest)) return;

        GuiChest gui = (GuiChest) Wrapper.getMC().currentScreen;
        int slots = gui.inventoryRows * 9;
        if (index >= slots || Wrapper.getPlayer().isInventoryFull()) {
            Wrapper.getPlayer().closeScreen();
            return;
        }

        // FIXME: Actual clicks/sec does not match expected values?
        if (!timer.hasPassed(1000 / cps.getValue())) return;

        Slot slot = gui.inventorySlots.inventorySlots.get(index);
        if (slot.getHasStack()) {
            Wrapper.getPlayer().clickWindow(gui.inventorySlots.windowId, index, 0, 1);
            timer.update();
        }
        index++;
    }
}
