
package net.minecraft.client.renderer;

import java.nio.FloatBuffer;

import net.minecraft.util.Vec3D;
import org.lwjgl.opengl.GL11;

public class RenderHelper {

    private static FloatBuffer floatBuffer;

    public static void disableStandardItemLighting() {
        GL11.glDisable(2896);
        GL11.glDisable(16384);
        GL11.glDisable(16385);
        GL11.glDisable(2903);
    }

    public static void enableStandardItemLighting() {
        GL11.glEnable(2896);
        GL11.glEnable(16384);
        GL11.glEnable(16385);
        GL11.glEnable(2903);
        GL11.glColorMaterial(1032, 5634);
        final float n = 0.4f;
        final float n2 = 0.6f;
        final float n3 = 0.0f;
        final Vec3D normalize = Vec3D.createVector(0.699999988079071, 1.0, -0.20000000298023224).normalize();
        GL11.glLight(16384, 4611, getBuffer(normalize.xCoord, normalize.yCoord, normalize.zCoord, 0.0));
        GL11.glLight(16384, 4609, getBuffer(n2, n2, n2, 1.0f));
        GL11.glLight(16384, 4608, getBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GL11.glLight(16384, 4610, getBuffer(n3, n3, n3, 1.0f));
        final Vec3D normalize2 = Vec3D.createVector(-0.699999988079071, 1.0, 0.20000000298023224).normalize();
        GL11.glLight(16385, 4611, getBuffer(normalize2.xCoord, normalize2.yCoord, normalize2.zCoord, 0.0));
        GL11.glLight(16385, 4609, getBuffer(n2, n2, n2, 1.0f));
        GL11.glLight(16385, 4608, getBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GL11.glLight(16385, 4610, getBuffer(n3, n3, n3, 1.0f));
        GL11.glShadeModel(7424);
        GL11.glLightModel(2899, getBuffer(n, n, n, 1.0f));
    }

    private static FloatBuffer getBuffer(final double double1, final double double2, final double double3, final double double4) {
        return getBuffer((float) double1, (float) double2, (float) double3, (float) double4);
    }

    private static FloatBuffer getBuffer(final float float1, final float float2, final float float3, final float float4) {
        RenderHelper.floatBuffer.clear();
        RenderHelper.floatBuffer.put(float1).put(float2).put(float3).put(float4);
        RenderHelper.floatBuffer.flip();
        return RenderHelper.floatBuffer;
    }

    static {
        RenderHelper.floatBuffer = GLAllocation.createFloatBuffer(16);
    }
}
