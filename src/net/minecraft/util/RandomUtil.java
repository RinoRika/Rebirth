package net.minecraft.util;

import java.util.Random;

public class RandomUtil {
    private static final Random random = new Random();

    public static int nextInt(int startInclusive, int endExclusive) {
        if (endExclusive - startInclusive <= 0) {
            return startInclusive;
        } else {
            return startInclusive + random.nextInt(endExclusive - startInclusive);
        }
    }

    public static String random(int length, String chars) {
        return random(length, chars.toCharArray());
    }

    public static String random(int length, char[] chars) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(chars[random.nextInt(chars.length)]);
        }
        return stringBuilder.toString();
    }

    public static double nextDouble(double startInclusive, double endInclusive) {
        if (startInclusive == endInclusive || endInclusive - startInclusive <= 0.0) {
            return startInclusive;
        } else {
            return startInclusive + (endInclusive - startInclusive) * Math.random();
        }
    }

    public static boolean nextBoolean() {
        return random.nextBoolean();
    }

    public static float nextFloat(float startInclusive, float endInclusive) {
        if (startInclusive == endInclusive || endInclusive - startInclusive <= 0f) {
            return startInclusive;
        } else {
            return (float) (startInclusive + (endInclusive - startInclusive) * Math.random());
        }
    }

    public static String randomNumber(int length) {
        return randomString(length, "123456789");
    }

    public static String randomString(int length) {
        return randomString(length, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
    }

    public static String randomString(int length, String chars) {
        return randomString(length, chars.toCharArray());
    }

    public static String randomString(int length, char[] chars) {
        return random(length, chars);
    }
}