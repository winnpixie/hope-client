package io.github.alerithe.client.utilities;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class MathHelper {
    private static final ThreadLocalRandom LOCAL_RANDOM = ThreadLocalRandom.current();

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
            return false;
        }
    }

    public static boolean isFloat(String value) {
        try {
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static float round(float value, int places) {
        DecimalFormat decFormat = new DecimalFormat();
        decFormat.setRoundingMode(RoundingMode.HALF_UP);
        decFormat.setMaximumFractionDigits(places);

        return Float.parseFloat(decFormat.format(value));
    }

    public static double round(double value, int places) {
        DecimalFormat decFormat = new DecimalFormat();
        decFormat.setRoundingMode(RoundingMode.HALF_UP);
        decFormat.setMaximumFractionDigits(places);

        return Double.parseDouble(decFormat.format(value));
    }

    public static int getRandomInt(int min, int max) {
        return min + floor(LOCAL_RANDOM.nextFloat() * (max - min + 1));
    }

    public static float getRandomFloat(float min, float max) {
        return min + (LOCAL_RANDOM.nextFloat() * (max - min));
    }

    public static double getRandomDouble(double min, double max) {
        return min + (LOCAL_RANDOM.nextDouble() * (max - min));
    }
}
