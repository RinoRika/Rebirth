
package net.minecraft.client.renderer.entity;

import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityArrow;
import org.lwjgl.opengl.GL11;

public class RenderArrow extends Render<EntityArrow> {

    @Override
    public void doRender(EntityArrow entityLiving, double xCoord, double sqrt_double, double yCoord, float nya1, float nya2) {
        this.loadTexture("/item/arrows.png");
        GL11.glPushMatrix();
        GL11.glTranslatef((float) xCoord, (float) sqrt_double, (float) yCoord);
        GL11.glRotatef( (entityLiving.prevRotationYaw + (entityLiving.rotationYaw - entityLiving.prevRotationYaw) * nya2 - 90.0f),  0.0f,  1.0f,  0.0f);
        GL11.glRotatef( (entityLiving.prevRotationPitch + (entityLiving.rotationPitch - entityLiving.prevRotationPitch) * nya2),  0.0f,  0.0f,  1.0f);
        Tessellator ag2 = Tessellator.instance;
        int n = 0;
        float f3 = 0.0f;
        float f4 = 0.5f;
        float f5 = 0;
        float f6 =  5 / 32.0f;
        float f7 = 0.0f;
        float f8 = 0.15625f;
        float f9 =  5 / 32.0f;
        float f10 =  10 / 32.0f;
        float f11 = 0.05625f;
        GL11.glEnable((int) 32826);
        float f12 =  entityLiving.arrowShake - nya2;
        if (f12 > 0.0f) {
            float f13 = -MathHelper.sin(f12 * 3.0f) * f12;
            GL11.glRotatef( f13,  0.0f,  0.0f,  1.0f);
        }
        GL11.glRotatef( 45.0f,  1.0f,  0.0f,  0.0f);
        GL11.glScalef( f11,  f11,  f11);
        GL11.glTranslatef( -4.0f,  0.0f,  0.0f);
        GL11.glNormal3f( f11,  0.0f,  0.0f);
        ag2.startDrawingQuads();
        ag2.addVertexWithUV(-7.0, -2.0, -2.0, f7, f9);
        ag2.addVertexWithUV(-7.0, -2.0, 2.0, f8, f9);
        ag2.addVertexWithUV(-7.0, 2.0, 2.0, f8, f10);
        ag2.addVertexWithUV(-7.0, 2.0, -2.0, f7, f10);
        ag2.draw();
        GL11.glNormal3f( (-f11),  0.0f,  0.0f);
        ag2.startDrawingQuads();
        ag2.addVertexWithUV(-7.0, 2.0, -2.0, f7, f9);
        ag2.addVertexWithUV(-7.0, 2.0, 2.0, f8, f9);
        ag2.addVertexWithUV(-7.0, -2.0, 2.0, f8, f10);
        ag2.addVertexWithUV(-7.0, -2.0, -2.0, f7, f10);
        ag2.draw();
        for (int i = 0; i < 4; ++i) {
            GL11.glRotatef( 90.0f,  1.0f,  0.0f,  0.0f);
            GL11.glNormal3f( 0.0f,  0.0f,  f11);
            ag2.startDrawingQuads();
            ag2.addVertexWithUV(-8.0, -2.0, 0.0, f3, f5);
            ag2.addVertexWithUV(8.0, -2.0, 0.0, f4, f5);
            ag2.addVertexWithUV(8.0, 2.0, 0.0, f4, f6);
            ag2.addVertexWithUV(-8.0, 2.0, 0.0, f3, f6);
            ag2.draw();
        }
        GL11.glDisable((int) 32826);
        GL11.glPopMatrix();
    }
}
