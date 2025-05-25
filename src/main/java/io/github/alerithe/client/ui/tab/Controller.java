package io.github.alerithe.client.ui.tab;

import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.Property;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.features.properties.impl.DoubleProperty;
import io.github.alerithe.client.features.properties.impl.IntProperty;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;
import io.github.alerithe.client.utilities.MathHelper;
import org.lwjgl.input.Keyboard;

public class Controller extends Component {
    private int typeIndex;
    private int moduleIndex;
    private int propertyIndex;

    public Controller(Container container) {
        super(container);
    }

    public void onKeyPress(int key) {
        switch (key) {
            case Keyboard.KEY_LEFT:
                switch (getContainer().getSection()) {
                    case TYPE:
                        break;
                    case MODULE:
                        getContainer().setSection(Section.TYPE);
                        break;
                    case PROPERTY:
                        Property<?> property = getContainer().getProperty();
                        if (property instanceof BooleanProperty) {
                            BooleanProperty boolProp = (BooleanProperty) property;
                            boolProp.setValue(!boolProp.getValue());
                        } else if (property instanceof IntProperty) {
                            IntProperty intProp = (IntProperty) property;
                            intProp.setValue(intProp.getValue() - 1);
                        } else if (property instanceof DoubleProperty) {
                            DoubleProperty dblProp = (DoubleProperty) property;
                            dblProp.setValue(MathHelper.truncate(dblProp.getValue() - 0.1, 1));
                        } else if (property instanceof ObjectProperty) {
                            ObjectProperty objProp = (ObjectProperty) property;
                            int index = objProp.getValues().indexOf(objProp.getValue());
                            index = ((index - 1) + objProp.getValues().size()) % objProp.getValues().size();
                            objProp.setValue(objProp.getValues().get(index));
                        }
                        break;
                }
                break;
            case Keyboard.KEY_RIGHT:
                switch (getContainer().getSection()) {
                    case TYPE:
                        moduleIndex = 0;
                        getContainer().setSection(Section.MODULE);
                        break;
                    case MODULE:
                        getContainer().getModule().toggle();
                        break;
                    case PROPERTY:
                        Property<?> property = getContainer().getProperty();
                        if (property instanceof BooleanProperty) {
                            BooleanProperty boolProp = (BooleanProperty) property;
                            boolProp.setValue(!boolProp.getValue());
                        } else if (property instanceof IntProperty) {
                            IntProperty intProp = (IntProperty) property;
                            intProp.setValue(intProp.getValue() + 1);
                        } else if (property instanceof DoubleProperty) {
                            DoubleProperty dblProp = (DoubleProperty) property;
                            dblProp.setValue(MathHelper.truncate(dblProp.getValue() + 0.1, 1));
                        } else if (property instanceof ObjectProperty) {
                            ObjectProperty objProp = (ObjectProperty) property;
                            int index = objProp.getValues().indexOf(objProp.getValue());
                            index = (index + 1) % objProp.getValues().size();
                            objProp.setValue(objProp.getValues().get(index));
                        }
                        break;
                }
                break;
            case Keyboard.KEY_UP:
                switch (getContainer().getSection()) {
                    case TYPE:
                        typeIndex = ((typeIndex - 1) + Module.Type.values().length) % Module.Type.values().length;
                        break;
                    case MODULE:
                        moduleIndex = ((moduleIndex - 1) + getContainer().getModules().size()) % getContainer().getModules().size();
                        break;
                    case PROPERTY:
                        propertyIndex = ((propertyIndex - 1) + getContainer().getProperties().size()) % getContainer().getProperties().size();
                        break;
                }
                break;
            case Keyboard.KEY_DOWN:
                switch (getContainer().getSection()) {
                    case TYPE:
                        typeIndex = (typeIndex + 1) % Module.Type.values().length;
                        break;
                    case MODULE:
                        moduleIndex = (moduleIndex + 1) % getContainer().getModules().size();
                        break;
                    case PROPERTY:
                        propertyIndex = (propertyIndex + 1) % getContainer().getProperties().size();
                        break;
                }
                break;
            case Keyboard.KEY_RETURN:
                switch (getContainer().getSection()) {
                    case TYPE:
                        moduleIndex = 0;
                        getContainer().setSection(Section.MODULE);
                        break;
                    case MODULE:
                        propertyIndex = 0;
                        getContainer().setSection(Section.PROPERTY);
                        break;
                    case PROPERTY:
                        propertyIndex = 0;
                        getContainer().setSection(Section.MODULE);
                        break;
                }
                break;
        }
    }

    public int getTypeIndex() {
        return typeIndex;
    }

    public int getModuleIndex() {
        return moduleIndex;
    }

    public int getPropertyIndex() {
        return propertyIndex;
    }
}
