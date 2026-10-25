package io.github.alerithe.client.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

public class MathHelper {
    private static final ThreadLocalRandom LOCAL_RANDOM = ThreadLocalRandom.current();

    private MathHelper() {
    }

    public static int clamp(int value, int min, int max) {
        return value < min ? min : value > max ? max : value;
    }

    public static long clamp(long value, long min, long max) {
        return value < min ? min : value > max ? max : value;
    }

    public static float clamp(float value, float min, float max) {
        return value < min ? min : value > max ? max : value;
    }

    public static double clamp(double value, double min, double max) {
        return value < min ? min : value > max ? max : value;
    }

    // Do I really need floor and ceil for doubles?
    public static int floor(float value) {
        int n = (int) value;
        return n > value ? n - 1 : n;
    }

    public static long floor(double value) {
        long n = (long) value;
        return n > value ? n - 1 : n;
    }

    public static int ceil(float value) {
        int n = (int) value;
        return n < value ? n + 1 : n;
    }

    public static long ceil(double value) {
        long n = (long) value;
        return n < value ? n + 1 : n;
    }

    // Linear interpolation, https://en.wikipedia.org/wiki/Linear_interpolation
    public static float lerpf(float v0, float v1, float t) {
        return (1f - t) * v0 + t * v1;
    }

    // Linear interpolation, https://en.wikipedia.org/wiki/Linear_interpolation
    public static double lerpd(double v0, double v1, double t) {
        return (1.0 - t) * v0 + t * v1;
    }

    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isLong(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isFloat(String value) {
        try {
            float n = Float.parseFloat(value);
            return !Float.isNaN(n) && !Float.isInfinite(n);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isDouble(String value) {
        try {
            double n = Double.parseDouble(value);
            return !Double.isNaN(n) && !Double.isInfinite(n);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static float round(float value, int decimalPlaces) {
        return new BigDecimal(Float.toString(value))
                .setScale(decimalPlaces, RoundingMode.HALF_UP)
                .floatValue();
    }

    public static double round(double value, int decimalPlaces) {
        return BigDecimal.valueOf(value)
                .setScale(decimalPlaces, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public static int randomInt(int min, int max) {
        if (min >= max) {
            return min;
        }

        return LOCAL_RANDOM.nextInt(min, max);
    }

    public static long randomLong(long min, long max) {
        if (min >= max) {
            return min;
        }

        return LOCAL_RANDOM.nextLong(min, max);
    }

    public static float randomFloat(float min, float max) {
        if (min >= max) {
            return min;
        }

        return min + (LOCAL_RANDOM.nextFloat() * (max - min));
    }

    public static double randomDouble(double min, double max) {
        if (min >= max) {
            return min;
        }

        return LOCAL_RANDOM.nextDouble(min, max);
    }
}
