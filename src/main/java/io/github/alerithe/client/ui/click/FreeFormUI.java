package io.github.alerithe.client.ui.click;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.Property;
import io.github.alerithe.client.features.properties.impl.*;
import io.github.alerithe.client.ui.click.elements.Element;
import io.github.alerithe.client.ui.click.elements.handlers.EventHandler;
import io.github.alerithe.client.ui.click.elements.handlers.impl.Collapsible;
import io.github.alerithe.client.ui.click.elements.handlers.impl.Draggable;
import io.github.alerithe.client.ui.click.elements.impl.Label;
import io.github.alerithe.client.ui.click.elements.impl.input.Button;
import io.github.alerithe.client.ui.click.elements.impl.input.CheckBox;
import io.github.alerithe.client.ui.click.elements.impl.input.Slider;
import io.github.alerithe.client.ui.click.elements.impl.input.TextArea;
import io.github.alerithe.client.ui.click.elements.styling.ElementStyle;
import io.github.alerithe.client.ui.click.elements.styling.text.TextAlignment;
import io.github.alerithe.client.ui.click.elements.styling.text.TextPosition;
import io.github.alerithe.client.utilities.MathHelper;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class FreeFormUI extends GuiScreen {
    private final Element root = new Element(0f, 0f, 8192f, 8192f);

    public FreeFormUI() {
        root.getNormalStyle().setShowBackground(false);

        buildCategories(root);
    }

    private void buildCategories(Element container) {
        float x = 1f;
        float y = 1f;
        float width = 100f;
        float height = 16f;

        for (Module.Type type : Module.Type.values()) {
            Label typeLbl = new Label(type.getLabel(), x, y, width, height);
            typeLbl.addHandler(new Draggable());

            typeLbl.getNormalStyle().setBackgroundColor(0xFFB00B1E);
            typeLbl.getNormalStyle().textStyle.setColor(0xFFFFFFFF);
            typeLbl.getNormalStyle().textStyle.setAlignment(TextAlignment.CENTER);
            typeLbl.getNormalStyle().textStyle.setPosition(TextPosition.MIDDLE);

            container.addChild(typeLbl);
            y += height + 5f;

            Element moduleContainer = new Element(0f, height, 0f, 0f);
            moduleContainer.getNormalStyle().setVisible(false);
            typeLbl.addChild(moduleContainer);
            typeLbl.addHandler(new Collapsible(moduleContainer));

            buildModules(type, moduleContainer);
        }
    }

    private void buildModules(Module.Type type, Element container) {
        float x = 1f;
        float y = 0f;
        float width = 98f;
        float height = 16f;

        for (Module module : Client.MODULE_MANAGER.getAllInType(type)) {
            Button moduleBtn = new Button(module.getName(), x, y, width, height);
            moduleBtn.getNormalStyle().setBackgroundColor(0xEE111111);
            moduleBtn.getNormalStyle().textStyle.setColor(module.isEnabled() ? 0xFFFFFFFF : 0xFFAAAAAA);

            moduleBtn.addHandler(new EventHandler() {
                @Override
                public void onClicked(int mouseX, int mouseY, int button) {
                    if (button == 0) {
                        module.toggle();

                        moduleBtn.getNormalStyle().textStyle.setColor(module.isEnabled() ? 0xFFFFFFFF : 0xFFAAAAAA);
                    }
                }
            });

            container.addChild(moduleBtn);
            y += height;

            Element propertyContainer = new Element(width, 0f, 0f, 0f);
            propertyContainer.getNormalStyle().setVisible(false);
            moduleBtn.addChild(propertyContainer);
            moduleBtn.addHandler(new Collapsible(propertyContainer));

            buildProperties(module, propertyContainer);
        }
    }

    private void buildProperties(Module module, Element container) {
        float y = 0f;
        for (Property<?> property : module.getPropertyManager().getChildren()) {
            Element propElem = createPropertyElement(property, y);
            setupPropertyElement(propElem);

            y += property instanceof NumberProperty ? 32f : 16f;
            container.addChild(propElem);
        }
    }

    private void setupPropertyElement(Element element) {
        element.getNormalStyle().setBackgroundColor(0xEE111111);
        element.getNormalStyle().setForegroundColor(0xFFB00B1E);
        element.getNormalStyle().textStyle.setColor(0xFFAAAAAA);
        element.getNormalStyle().textStyle.setOffsetX(4f);
        element.getNormalStyle().textStyle.setAlignment(TextAlignment.LEFT);
    }

    private Element createPropertyElement(Property<?> property, float y) {
        float x = 0f;
        float width = 125f;
        float height = 16f;

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
            unchecked.setBackgroundColor(0xFF222222);

            boolProp.addChangeListener(prop -> valueCheckBox.setChecked(prop.getValue()));

            return valueCheckBox;
        } else if (property instanceof IntProperty) {
            IntProperty intProp = (IntProperty) property;
            Label nameLbl = new Label(intProp.getName(),
                    x, -height, width, height);
            nameLbl.getNormalStyle().setBackgroundColor(0xEE111111);
            nameLbl.getNormalStyle().textStyle.setColor(0xFFAAAAAA);
            nameLbl.getNormalStyle().textStyle.setOffsetX(4f);
            nameLbl.getNormalStyle().textStyle.setPosition(TextPosition.MIDDLE);

            y += height;

            Label valueLbl = new Label(intProp.getValue().toString(),
                    x + (width * 0.75f), 0f, width / 4f, height);
            valueLbl.getNormalStyle().setBackgroundColor(0xEE111111);
            valueLbl.getNormalStyle().textStyle.setColor(0xFFFFFFFF);
            valueLbl.getNormalStyle().textStyle.setOffsetX(4f);
            valueLbl.getNormalStyle().textStyle.setAlignment(TextAlignment.RIGHT);
            valueLbl.getNormalStyle().textStyle.setPosition(TextPosition.MIDDLE);

            Slider valueSlider = new Slider(x, y, width * 0.75f, height,
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

            return valueSlider;
        } else if (property instanceof DoubleProperty) {
            DoubleProperty dblProp = (DoubleProperty) property;
            Label nameLbl = new Label(dblProp.getName(),
                    x, -height, width, height);
            nameLbl.getNormalStyle().setBackgroundColor(0xEE111111);
            nameLbl.getNormalStyle().textStyle.setColor(0xFFAAAAAA);
            nameLbl.getNormalStyle().textStyle.setOffsetX(4f);
            nameLbl.getNormalStyle().textStyle.setPosition(TextPosition.MIDDLE);

            y += height;

            Label valueLbl = new Label(dblProp.getValue().toString(),
                    x + (width * 0.75f), 0f, width / 4f, height);
            valueLbl.getNormalStyle().setBackgroundColor(0xEE111111);
            valueLbl.getNormalStyle().textStyle.setColor(0xFFFFFFFF);
            valueLbl.getNormalStyle().textStyle.setOffsetX(4);
            valueLbl.getNormalStyle().textStyle.setAlignment(TextAlignment.RIGHT);
            valueLbl.getNormalStyle().textStyle.setPosition(TextPosition.MIDDLE);

            Slider valueSlider = new Slider(x, y, width * 0.75f, height,
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

            return valueSlider;
        } else if (property instanceof ObjectProperty) {
            ObjectProperty objProp = (ObjectProperty) property;
            Element objContainer = new Element(x, y, width, height);

            Label nameLbl = new Label(objProp.getName(), 0f, 0f, width / 2f, height);
            nameLbl.getNormalStyle().setShowBackground(false);
            nameLbl.getNormalStyle().textStyle.setColor(0xFFAAAAAA);
            nameLbl.getNormalStyle().textStyle.setOffsetX(4f);
            nameLbl.getNormalStyle().textStyle.setPosition(TextPosition.MIDDLE);

            Button valueBtn = new Button(objProp.getValue().toString(),
                    width / 2f, 0f, width / 2f, height) {
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

            objProp.addChangeListener(prop -> valueBtn.setText(prop.getValue().toString()));

            objContainer.addChildren(nameLbl, valueBtn);
            return objContainer;
        } else if (property instanceof StringProperty) {
            StringProperty strProp = (StringProperty) property;
            Element txtContainer = new Element(x, y, width, height);

            Label nameLbl = new Label(strProp.getName(), 0f, 0f, width, height);
            nameLbl.getNormalStyle().setShowBackground(false);
            nameLbl.getNormalStyle().textStyle.setColor(0xFFAAAAAA);
            nameLbl.getNormalStyle().textStyle.setOffsetX(4);
            nameLbl.getNormalStyle().textStyle.setPosition(TextPosition.MIDDLE);

            TextArea valueArea = new TextArea(strProp.getValue(), 0f, height, width, height) {
                @Override
                public void onValueChanged(String oldValue, String newValue) {
                    strProp.setValue(newValue);
                }
            };

            strProp.addChangeListener(prop -> valueArea.setText(prop.getValue()));

            txtContainer.addChildren(nameLbl, valueArea);
            return txtContainer;
        } else {
            return new Label(property.getName() + "(n/a)",
                    0f, y, width, height);
        }
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
