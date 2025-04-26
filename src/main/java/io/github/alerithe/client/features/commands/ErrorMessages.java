package io.github.alerithe.client.features.commands;

public class ErrorMessages {
    public static final String NOT_ENOUGH_ARGS = format("Not enough arguments provided.");
    public static final String INVALID_ARG = format("Invalid argument provided.");
    public static final String INVALID_ARG_TYPE = format("Incorrect argument type provided.");
    public static final String INVALID_TARGET = format("No target found.");
    public static final String INVALID_COMMAND = format("No command found.");

    public static String format(String message) {
        return "\247c" + message;
    }
}
