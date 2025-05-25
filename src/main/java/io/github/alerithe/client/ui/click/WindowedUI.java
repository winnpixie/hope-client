package io.github.alerithe.client.ui.click;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.Property;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.features.properties.impl.DoubleProperty;
import io.github.alerithe.client.features.properties.impl.IntProperty;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;
import io.github.alerithe.client.ui.click.elements.Element;
import io.github.alerithe.client.ui.click.elements.handlers.EventHandler;
import io.github.alerithe.client.ui.click.elements.handlers.impl.Draggable;
import io.github.alerithe.client.ui.click.elements.impl.Label;
import io.github.alerithe.client.ui.click.elements.impl.input.Button;
import io.github.alerithe.client.ui.click.elements.impl.input.CheckBox;
import io.github.alerithe.client.ui.click.elements.impl.input.Slider;
import io.github.alerithe.client.ui.click.elements.styling.ElementStyle;
import io.github.alerithe.client.ui.click.elements.styling.text.TextAlignment;
import io.github.alerithe.client.ui.click.elements.styling.text.TextPosition;
import io.github.alerithe.client.utilities.MathHelper;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.List;

public class WindowedUI extends GuiScreen {
    private final Element root = new Element(0, 0, 8192, 8192);

    private final float WINDOW_WIDTH = 480;
    private final float WINDOW_HEIGHT = 240;

    private final Element moduleContainer = new Element(WINDOW_WIDTH / 4f, 0, WINDOW_WIDTH / 4f, WINDOW_HEIGHT);
    private final Element propertyContainer = new Element(WINDOW_WIDTH / 2f, 0, WINDOW_WIDTH / 2f, WINDOW_HEIGHT);

    public WindowedUI() {
        root.getNormalStyle().setShowBackground(false);

        Label windowTitle = new Label("Configuration", 1, 1, WINDOW_WIDTH, 20);
        windowTitle.getNormalStyle().setBackgroundColor(0xFF000000);
        windowTitle.getNormalStyle().textStyle.setColor(0xFFFFFFFF);
        windowTitle.getNormalStyle().textStyle.setAlignment(TextAlignment.CENTER);
        windowTitle.getNormalStyle().textStyle.setPosition(TextPosition.MIDDLE);
        windowTitle.addHandler(new Draggable());

        Element windowContainer = new Element(0, 20, WINDOW_WIDTH, WINDOW_HEIGHT);
        windowContainer.getNormalStyle().setBackgroundColor(0xFF111111);

        moduleContainer.getNormalStyle().setShowBackground(false);
        propertyContainer.getNormalStyle().setShowBackground(false);
        windowContainer.addChildren(moduleContainer, propertyContainer);
        windowTitle.addChild(windowContainer);

        buildCategories(windowContainer);

        root.addChild(windowTitle);
    }

    private void buildCategories(Element container) {
        Module.Type[] types = Module.Type.values();
        int count = types.length;
        float height = WINDOW_HEIGHT / count;

        for (int i = 0; i < count; i++) {
            Module.Type type = types[i];

            float x = 0;
            float y = height * i;

            Button typeLbl = new Button(type.getLabel(), x, y, WINDOW_WIDTH / 4f, height) {
                @Override
                public void onClicked(int mouseX, int mouseY, int button) {
                    moduleContainer.clearChildren();
                    propertyContainer.clearChildren();

                    buildModules(type, moduleContainer);
                }
            };

            typeLbl.getNormalStyle().setBackgroundColor(0xFFB00B1E);
            typeLbl.getNormalStyle().textStyle.setColor(0xFFFFFFFF);
            typeLbl.getNormalStyle().textStyle.setAlignment(TextAlignment.CENTER);
            typeLbl.getNormalStyle().textStyle.setPosition(TextPosition.MIDDLE);

            typeLbl.getHoveredStyle().setBackgroundColor(0xFF990000);

            container.addChild(typeLbl);
        }
    }

    private void buildModules(Module.Type type, Element container) {
        List<Module> modules = Client.MODULE_MANAGER.getAllInType(type);
        int count = modules.size();
        float height = WINDOW_HEIGHT / count;

        for (int i = 0; i < count; i++) {
            Module module = modules.get(i);

            float x = 0;
            float y = height * i;

            Button moduleBtn = new Button(module.getName(), x, y, WINDOW_WIDTH / 4f, height);
            moduleBtn.getNormalStyle().setBackgroundColor(0xFF111111);
            moduleBtn.getNormalStyle().textStyle.setColor(module.isEnabled() ? 0xFFFFFFFF : 0xFFAAAAAA);

            moduleBtn.addHandler(new EventHandler() {
                @Override
                public void onClicked(int mouseX, int mouseY, int button) {
                    if (button == 0) {
                        module.toggle();

                        moduleBtn.getNormalStyle().textStyle.setColor(module.isEnabled() ? 0xFFFFFFFF : 0xFFAAAAAA);
                    } else if (button == 1) {
                        propertyContainer.clearChildren();

                        buildProperties(module, propertyContainer);
                    }
                }
            });

            container.addChild(moduleBtn);
        }
    }

    private void buildProperties(Module module, Element container) {
        List<Property<?>> properties = module.getPropertyManager().getElements();
        int count = properties.size();
        float width = WINDOW_WIDTH / 2f;
        float height = WINDOW_HEIGHT / count;

        for (int i = 0; i < count; i++) {
            Property<?> property = properties.get(i);
            Element propElem;

            float x = 0;
            float y = height * i;

            if (property instanceof BooleanProperty) {
                BooleanProperty boolProp = (BooleanProperty) property;
                CheckBox valueCheckBox = new CheckBox(boolProp.getName(),
                        x, y, width, height, boolProp.getValue()) {
                    @Override
                    public void onValueChanged(boolean oldValue, boolean newValue) {
                        boolProp.setValue(newValue);
                    }
                };

                ElementStyle checked = valueCheckBox.checkedStyle;
                checked.setBackgroundColor(0xFFB00B1E);

                ElementStyle unchecked = valueCheckBox.uncheckedStyle;
                unchecked.setBackgroundColor(0xFF696969);

                boolProp.addChangeListener(prop -> valueCheckBox.setChecked(prop.getValue()));

                propElem = valueCheckBox;
            } else if (property instanceof IntProperty) {
                IntProperty intProp = (IntProperty) property;
                Label nameLbl = new Label(intProp.getName(),
                        x - (width / 2f), 0, width / 2f, height);
                nameLbl.getNormalStyle().setBackgroundColor(0xFF111111);
                nameLbl.getNormalStyle().textStyle.setColor(0xFFAAAAAA);
                nameLbl.getNormalStyle().textStyle.setOffsetX(4);
                nameLbl.getNormalStyle().textStyle.setPosition(TextPosition.MIDDLE);

                Label valueLbl = new Label(intProp.getValue().toString(),
                        x + (width / 3f), 0, width / 6f, height);
                valueLbl.getNormalStyle().setBackgroundColor(0xFF111111);
                valueLbl.getNormalStyle().textStyle.setColor(0xFFFFFFFF);
                valueLbl.getNormalStyle().textStyle.setOffsetX(4);
                valueLbl.getNormalStyle().textStyle.setAlignment(TextAlignment.RIGHT);
                valueLbl.getNormalStyle().textStyle.setPosition(TextPosition.MIDDLE);

                Slider valueSlider = new Slider(x + (width / 2f), y, width / 3f, height,
                        intProp.getValue(), intProp.getMinimum(), intProp.getMaximum()) {
                    @Override
                    public void onValueChanged(double oldValue, double newValue) {
                        intProp.setValue(MathHelper.floor(newValue));
                    }
                };
                valueSlider.addChildren(nameLbl, valueLbl);

                intProp.addChangeListener(prop -> {
                    valueSlider.setValue(prop.getValue());
                    valueLbl.setText(String.format("%d", prop.getValue()));
                });

                propElem = valueSlider;
            } else if (property instanceof DoubleProperty) {
                DoubleProperty dblProp = (DoubleProperty) property;
                Label nameLbl = new Label(dblProp.getName(),
                        x - (width / 2f), 0, width / 2f, height);
                nameLbl.getNormalStyle().setBackgroundColor(0xFF111111);
                nameLbl.getNormalStyle().textStyle.setColor(0xFFAAAAAA);
                nameLbl.getNormalStyle().textStyle.setOffsetX(4);
                nameLbl.getNormalStyle().textStyle.setPosition(TextPosition.MIDDLE);

                Label valueLbl = new Label(dblProp.getValue().toString(),
                        x + (width / 3f), 0, width / 6f, height);
                valueLbl.getNormalStyle().setBackgroundColor(0xFF111111);
                valueLbl.getNormalStyle().textStyle.setColor(0xFFFFFFFF);
                valueLbl.getNormalStyle().textStyle.setOffsetX(4);
                valueLbl.getNormalStyle().textStyle.setAlignment(TextAlignment.RIGHT);
                valueLbl.getNormalStyle().textStyle.setPosition(TextPosition.MIDDLE);

                Slider valueSlider = new Slider(x + (width / 2f), y, width / 3f, height,
                        dblProp.getValue(), dblProp.getMinimum(), dblProp.getMaximum()) {
                    @Override
                    public void onValueChanged(double oldValue, double newValue) {
                        dblProp.setValue(MathHelper.truncate(newValue, 1));
                    }
                };
                valueSlider.addChildren(nameLbl, valueLbl);

                dblProp.addChangeListener(prop -> {
                    valueSlider.setValue(prop.getValue());
                    valueLbl.setText(String.format("%.1f", prop.getValue()));
                });

                propElem = valueSlider;
            } else if (property instanceof ObjectProperty) {
                ObjectProperty objProp = (ObjectProperty) property;
                Element objContainer = new Element(x, y, width, height);

                Label nameLbl = new Label(objProp.getName(), 0, 0, width / 2f, height);
                nameLbl.getNormalStyle().setShowBackground(false);
                nameLbl.getNormalStyle().textStyle.setColor(0xFFAAAAAA);
                nameLbl.getNormalStyle().textStyle.setOffsetX(4);
                nameLbl.getNormalStyle().textStyle.setPosition(TextPosition.MIDDLE);

                Button valueBtn = new Button(objProp.getValue().toString(),
                        width / 2f, 0, width / 2f, height) {
                    @Override
                    public void onClicked(int mouseX, int mouseY, int button) {
                        int index = objProp.getValues().indexOf(objProp.getValue());
                        index = (index + 1) % objProp.getValues().size();
                        objProp.setValue(objProp.getValues().get(index));
                    }
                };
                valueBtn.getNormalStyle().setShowBackground(false);
                valueBtn.getHoveredStyle().setInheritsShowBackground(false);
                valueBtn.getNormalStyle().textStyle.setColor(0xFFFFFFFF);
                valueBtn.getNormalStyle().textStyle.setOffsetX(4);
                valueBtn.getNormalStyle().textStyle.setAlignment(TextAlignment.RIGHT);

                objProp.addChangeListener(prop ->
                        valueBtn.setText(prop.getValue().toString()));

                objContainer.addChildren(nameLbl, valueBtn);
                propElem = objContainer;
            } else {
                propElem = new Label(property.getName() + "(n/a)",
                        x, y, width, height);
            }

            setupPropertyElement(propElem);
            container.addChild(propElem);
        }
    }

    private void setupPropertyElement(Element element) {
        element.getNormalStyle().setBackgroundColor(0xFF111111);
        element.getNormalStyle().setForegroundColor(0xFFB00B1E);
        element.getNormalStyle().textStyle.setColor(0xFFAAAAAA);
        element.getNormalStyle().textStyle.setOffsetX(4);
        element.getNormalStyle().textStyle.setAlignment(TextAlignment.LEFT);
    }

    @Override
    public void initGui() {
        root.setWidth(width);
        root.setHeight(height);
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
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        root.pressKey(typedChar, keyCode);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        root.draw(mouseX, mouseY, partialTicks);
    }
}
