package io.github.alerithe.client.utilities;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MathHelper {
    private static final Random random = ThreadLocalRandom.current();

    public static int min(int a, int b) {
        return a < b ? a : b;
    }

    public static int max(int a, int b) {
        return a > b ? a : b;
    }

    public static int abs(int val) {
        return val < 0 ? -val : val;
    }

    public static int clamp(int value, int min, int max) {
        return value < min ? min : value > max ? max : value;
    }

    public static float min(float a, float b) {
        return a < b ? a : b;
    }

    public static float max(float a, float b) {
        return a > b ? a : b;
    }

    public static float abs(float val) {
        return val < 0f ? -val : val;
    }

    public static float clamp(float value, float min, float max) {
        return value < min ? min : value > max ? max : value;
    }

    public static double min(double a, double b) {
        return a < b ? a : b;
    }

    public static double max(double a, double b) {
        return a > b ? a : b;
    }

    public static double abs(double val) {
        return val < 0.0 ? -val : val;
    }

    public static double clamp(double value, double min, double max) {
        return value < min ? min : value > max ? max : value;
    }

    public static int floor(float value) {
        int ivalue = (int) value;
        return ivalue > value ? ivalue - 1 : ivalue;
    }

    public static int floor(double value) {
        int ivalue = (int) value;
        return ivalue > value ? ivalue - 1 : ivalue;
    }

    public static int ceil(float value) {
        int ivalue = (int) value;
        return ivalue < value ? ivalue + 1 : ivalue;
    }

    public static int ceil(double value) {
        int ivalue = (int) value;
        return ivalue < value ? ivalue + 1 : ivalue;
    }

    // Linear interpolation, https://en.wikipedia.org/wiki/Linear_interpolation
    public static float lerpf(float v0, float v1, float t) {
        return (1f - t) * v0 + t * v1;
    }

    // Linear interpolation, https://en.wikipedia.org/wiki/Linear_interpolation
    public static double lerpd(double v0, double v1, double t) {
        return (1.0 - t) * v0 + t * v1;
    }

    public static boolean isInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isFloat(String value) {
        try {
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static float formatf(float value, int places) {
        return Float.parseFloat(String.format("%." + places + "f", value));
    }

    public static double formatd(double value, int places) {
        return Double.parseDouble(String.format("%." + places + "f", value));
    }

    public static float randomf(float min, float max) {
        return min + (random.nextFloat() * (max - min + 1));
    }

    public static double randomd(double min, double max) {
        return min + (random.nextDouble() * (max - min + 1));
    }
}
