package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.Property;
import io.github.alerithe.client.features.properties.impl.*;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.Wrapper;

import java.util.Arrays;

public class CommandModule extends Command {
    public CommandModule() {
        super("module", new String[]{"mod"}, "<module> <property> <value>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 3) {
            Wrapper.printMessage(ErrorMessages.NOT_ENOUGH_ARGS);
            return;
        }

        Module module = Client.MODULE_MANAGER.find(args[0]);
        if (module == null) {
            Wrapper.printMessage(ErrorMessages.INVALID_TARGET);
            return;
        }

        Property<?> property = module.getPropertyManager().find(args[1]);
        if (property == null) {
            Wrapper.printMessage(ErrorMessages.INVALID_TARGET);
            return;
        }

        if (property instanceof BooleanProperty) {
            ((BooleanProperty) property).setValue(Boolean.parseBoolean(args[2]));
        } else if (property instanceof IntProperty) {
            if (MathHelper.isInt(args[2])) ((IntProperty) property).setValue(Integer.parseInt(args[2]));
        } else if (property instanceof DoubleProperty) {
            if (MathHelper.isDouble(args[2])) ((DoubleProperty) property).setValue(Double.parseDouble(args[2]));
        } else if (property instanceof ObjectProperty) {
            ObjectProperty prop = (ObjectProperty) property;
            ObjectProperty.Value value = prop.get(args[2]);
            if (value != null) prop.setValue(value);
        } else if (property instanceof StringProperty) {
            ((StringProperty) property).setValue(String.join(" ", Arrays.copyOfRange(args, 2, args.length)));
        }

        Wrapper.printMessage(String.format("%s is now %s.", property.getName(), property.getValue().toString()));
    }
}
