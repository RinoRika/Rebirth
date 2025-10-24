package net.minecraft.util;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.nio.ByteBuffer;

public class RenderUtil {
    private static int imageWidth;
    private static int imageHeight;
    private static int internalformat;
    private static ByteBuffer imageBuffer;
    private static int textureID = -1;
    private static boolean textureUpdated = false;

    public static void setBuffer(ByteBuffer buffer, int width, int height) {
        textureUpdated = true;
        internalformat = 6407;
        imageWidth = width;
        imageHeight = height;
        imageBuffer = buffer;
    }

    public static void bindTexture() {
        if (!textureUpdated) {
            GlStateManager.bindTexture(textureID);
            return;
        }
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        if (textureID == -1) {
            textureID = GL11.glGenTextures();
        }
        GlStateManager.bindTexture(textureID);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, internalformat, imageWidth, imageHeight, 0, internalformat, 5121, imageBuffer);
        textureUpdated = false;
    }
}
