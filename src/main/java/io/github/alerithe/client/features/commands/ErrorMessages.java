package io.github.alerithe.client.features.commands;

public class ErrorMessages {
    public static final String NOT_ENOUGH_ARGS = "\247cNot enough arguments provided.";
    public static final String INVALID_ARG = "\247cInvalid argument provided.";
    public static final String INVALID_ARG_TYPE = "\247cIncorrect argument type provided.";
    public static final String INVALID_TARGET = "\247cNo target found.";
    public static final String INVALID_COMMAND = "\247cNo command found.";

    public static String format(String message) {
        return "\247c" + message;
    }
}
