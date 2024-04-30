package io.github.alerithe.client.ui.click;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.ui.click.elements.impl.Button;
import io.github.alerithe.client.ui.click.elements.impl.Window;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiClick extends GuiScreen {
    private List<Window> windows = new ArrayList<>();

    public GuiClick() {
        int typeY = 1;
        for (Module.Type type : Module.Type.values()) {
            Window window = new Window(type.getLabel(), 1, typeY, 100, 15);

            int modY = typeY + 15;
            for (Module module : Client.MODULE_MANAGER.getAllInType(type)) {
                Button button = new Button(module.getName(), window.getX(), modY, 98, 15) {
                    @Override
                    public void onClick(int mouseX, int mouseY, int button) {
                        if (isInBounds(mouseX, mouseY) && button == 0) module.toggle();
                    }

                    @Override
                    public void update() {
                        setTextColor(module.isEnabled() ? -1 : 0xFFAAAAAA);
                    }

                    @Override
                    public void draw(int mouseX, int mouseY, float partialTicks) {
                        setBackgroundColor(isInBounds(mouseX, mouseY) ? 0xFF111111 : 0xFF222222);
                        super.draw(mouseX, mouseY, partialTicks);
                    }
                };

                window.getChildren().add(button);
            }

            windows.add(window);
            typeY += 20;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
        windows.forEach(Window::update);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (Window window : windows) {
            window.onClick(mouseX, mouseY, mouseButton);
            if (window.isDragging()) break;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        for (Window window : windows) {
            window.onRelease(mouseX, mouseY, releaseButton);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (Window window : windows) {
            window.draw(mouseX, mouseY, partialTicks);
        }
    }
}
