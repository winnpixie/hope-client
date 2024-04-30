package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.Property;
import io.github.alerithe.client.features.properties.impl.NumberProperty;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;
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
            Wrapper.printChat("\247cNot enough arguments.");
            return;
        }

        Module module = Client.MODULE_MANAGER.get(args[0]);
        if (module == null) {
            Wrapper.printChat(String.format("\247cNo such module '%s'.", args[0]));
            return;

        }

        Property<?> property = module.getPropertyManager().get(args[1]);
        if (property == null) {
            Wrapper.printChat(String.format("\247cNo such property '%s'.", args[1]));
            return;
        }

        if (property.getValue() instanceof Boolean) {
            ((Property<Boolean>) property).setValue(args[2].equalsIgnoreCase("true"));
        } else if (property instanceof NumberProperty) {
            if (property.getValue() instanceof Integer) {
                if (MathHelper.isInt(args[2])) {
                    ((NumberProperty<Integer>) property).setValue(Integer.parseInt(args[2]));
                }
            } else if (property.getValue() instanceof Double) {
                if (MathHelper.isDouble(args[2])) {
                    ((NumberProperty<Double>) property).setValue(Double.parseDouble(args[2]));
                }
            }
        } else if (property instanceof ObjectProperty) {
            ObjectProperty<?> prop = (ObjectProperty<?>) property;
            ObjectProperty.Value value = prop.get(args[2]);
            if (value != null) {
                ((ObjectProperty<ObjectProperty.Value>) prop).setValue(value);
            }
        } else if (property.getValue() instanceof String) {
            ((Property<String>) property).setValue(String.join(" ",
                    Arrays.copyOfRange(args, 2, args.length)));
        }

        Wrapper.printChat(String.format("%s is now %s.", property.getName(), property.getValue().toString()));
    }
}
