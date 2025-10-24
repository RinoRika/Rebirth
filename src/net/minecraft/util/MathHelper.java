
package net.minecraft.util;

public class MathHelper {

    private static float[] SIN_TABLE;

    public static float sin(final float float1) {
        return MathHelper.SIN_TABLE[(int) (float1 * 10430.378f) & 0xFFFF];
    }

    public static float cos(final float float1) {
        return MathHelper.SIN_TABLE[(int) (float1 * 10430.378f + 16384.0f) & 0xFFFF];
    }

    public static int clamp_int(int num, int min, int max)
    {
        return num < min ? min : (Math.min(num, max));
    }

    public static float clamp_float(float num, float min, float max)
    {
        return num < min ? min : (Math.min(num, max));
    }

    public static double clamp_double(double num, double min, double max)
    {
        return num < min ? min : (Math.min(num, max));
    }

    public static float sqrt_float(final float float1) {
        return (float) Math.sqrt((double) float1);
    }

    public static float sqrt_double(final double double1) {
        return (float) Math.sqrt(double1);
    }

    public static int floor_float(final float float1) {
        return floor_double(float1);
    }

    public static int floor_double(final double double1) {
        final int n = (int) double1;
        return (double1 < n) ? (n - 1) : n;
    }

    public static float abs(final float float1) {
        return (float1 >= 0.0f) ? float1 : (-float1);
    }

    public static double abs_max(double double1, double double2) {
        if (double1 < 0.0) {
            double1 = -double1;
        }
        if (double2 < 0.0) {
            double2 = -double2;
        }
        return Math.max(double1, double2);
    }

    public static int a(final int integer1, final int integer2) {
        if (integer1 < 0) {
            return -((-integer1 - 1) / integer2) - 1;
        }
        return integer1 / integer2;
    }

    static {
        MathHelper.SIN_TABLE = new float[65536];
        for (int i = 0; i < 65536; ++i) {
            MathHelper.SIN_TABLE[i] = (float) Math.sin(i * 3.141592653589793 * 2.0 / 65536.0);
        }
    }
}
