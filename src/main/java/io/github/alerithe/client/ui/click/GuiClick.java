package io.github.alerithe.client.ui.click;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.Property;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.ui.click.elements.Element;
import io.github.alerithe.client.ui.click.elements.impl.*;
import io.github.alerithe.client.ui.click.elements.styling.Style;
import io.github.alerithe.client.ui.click.elements.styling.TextAlignment;
import io.github.alerithe.client.ui.click.elements.styling.TextPosition;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class GuiClick extends GuiScreen {
    private final Root root = new Root(0, 0, 8192, 8192);

    public GuiClick() {
        root.getRenderer().showBackground = false;

        int typeY = 1;
        for (Module.Type type : Module.Type.values()) {
            Window typeWin = new Window(type.getLabel(), 1, typeY, 100, 15);
            typeWin.setStyle(new Style() {
                {
                    backgroundColor = 0xFFB00B1E;
                    textColor = 0xFFFFFFFF;
                    textAlignment = TextAlignment.CENTER;
                    textPosition = TextPosition.MIDDLE;
                }
            });

            int modY = 15;
            for (Module module : Client.MODULE_MANAGER.getAllInType(type)) {
                Button moduleBtn = new Button(module.getName(), 1, modY, 98, 15) {
                    @Override
                    public void onClick(int mouseX, int mouseY, int button) {
                        if (button == 0) module.toggle();
                    }

                    @Override
                    public void onUpdate() {
                        getStyle().textColor = module.isEnabled() ? 0xFFFFFFFF : 0xFFAAAAAA;
                    }

                    @Override
                    public void onDraw(int mouseX, int mouseY, float partialTicks) {
                        getStyle().backgroundColor = isInBounds(mouseX, mouseY) ? 0xEE222222 : 0xEE111111;
                    }
                };

                moduleBtn.setStyle(new Style() {
                    {
                        textAlignment = TextAlignment.CENTER;
                        textPosition = TextPosition.MIDDLE;
                    }
                });

                moduleBtn.getController().closeable = true;
                moduleBtn.getController().expanded = false;

                moduleBtn.addChild(new Label("Settings", 98, 0, 100, 15) {
                    {
                        setStyle(new Style() {
                            {
                                backgroundColor = 0xFF222222;
                                textColor = 0xFFFFFFFF;
                                textAlignment = TextAlignment.CENTER;
                                textPosition = TextPosition.MIDDLE;
                            }
                        });
                    }
                });

                int propY = 15;
                for (Property<?> property : module.getPropertyManager().getElements()) {
                    Element propElem = null;

                    if (property instanceof BooleanProperty) {
                        BooleanProperty boolProp = (BooleanProperty) property;
                        propElem = new CheckBox(boolProp.getName(), 98, propY, 100, 15) {
                            @Override
                            public void onValueChanged(boolean newValue) {
                                boolProp.setValue(newValue);
                            }
                        };
                    }

                    if (propElem != null) {
                        propElem.setStyle(new Style() {
                            {
                                backgroundColor = 0xEE111111;
                                foregroundColor = 0xFFB00B1E;
                                textColor = 0xFFAAAAAA;
                                textPosition = TextPosition.MIDDLE;
                            }
                        });

                        moduleBtn.addChild(propElem);
                        propY += 15;
                    }
                }

                typeWin.addChild(moduleBtn);
                modY += 15;
            }

            root.addChild(typeWin);
            typeY += 20;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
        root.update();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        root.click(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        root.release(mouseX, mouseY, releaseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        root.draw(mouseX, mouseY, partialTicks);
    }
}
