
package net.minecraft.entity;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Random;
import net.minecraft.Minecraft;
import net.minecraft.ScaledResolution;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.PlayerControllerTest;
import net.minecraft.client.input.MovingObjectPosition;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemRenderer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3D;
import net.minecraft.world.World;
import net.minecraft.world.WorldRenderer;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

public class EntityRenderer {

    private Minecraft mc;
    private boolean anaglyphEnable;
    private float farPlaneDistance;
    public ItemRenderer itemRenderer;
    private int rendererUpdateCount;
    private Entity pointedEntity;
    private int entityRendererInt1;
    private int entityRendererInt2;
    private final Random random;
    volatile int unusedVolatile0;
    volatile int unusedVolatile1;
    FloatBuffer fogColorBuffer;
    float fogColorRed;
    float fogColorGreen;
    float fogColorBlue;
    private float fogColor2;
    private float fogColor1;
    private final float[] rainXCoords = new float[1024];
    private final float[] rainYCoords = new float[1024];

    public EntityRenderer(final Minecraft aw) {
        this.anaglyphEnable = false;
        this.farPlaneDistance = 0.0f;
        this.pointedEntity = null;
        this.random = new Random();
        this.unusedVolatile0 = 0;
        this.unusedVolatile1 = 0;
        this.fogColorBuffer = GLAllocation.createFloatBuffer(16);
        this.mc = aw;
        this.itemRenderer = new ItemRenderer(aw);

        for (int i = 0; i < 32; ++i)
        {
            for (int j = 0; j < 32; ++j)
            {
                float f = (float)(j - 16);
                float f1 = (float)(i - 16);
                float f2 = MathHelper.sqrt_float(f * f + f1 * f1);
                this.rainXCoords[i << 5 | j] = -f1 / f2;
                this.rainYCoords[i << 5 | j] = f / f2;
            }
        }
    }

    public void updateRenderer() {
        this.fogColor2 = this.fogColor1;
        final float lightBrightness = this.mc.theWorld.getLightBrightness(MathHelper.floor_double(this.mc.thePlayer.posX), MathHelper.floor_double(this.mc.thePlayer.posY), MathHelper.floor_double(this.mc.thePlayer.posZ));
        final float n = (3 - this.mc.gameSettings.renderDistance) / 3.0f;
        this.fogColor1 += (lightBrightness * (1.0f - n) + n - this.fogColor1) * 0.1f;
        ++this.rendererUpdateCount;
        this.itemRenderer.updateEquippedItem();
    }

    private Vec3D orientCamera(final float float1) {
        final EntityPlayerSP thePlayer = this.mc.thePlayer;
        return Vec3D.createVector(thePlayer.prevPosX + (thePlayer.posX - thePlayer.prevPosX) * float1, thePlayer.prevPosY + (thePlayer.posY - thePlayer.prevPosY) * float1, thePlayer.prevPosZ + (thePlayer.posZ - thePlayer.prevPosZ) * float1);
    }

    private void getMouseOver(final float float1) {
        final EntityPlayerSP thePlayer = this.mc.thePlayer;
        final float n = thePlayer.prevRotationPitch + (thePlayer.rotationPitch - thePlayer.prevRotationPitch) * float1;
        final float n2 = thePlayer.prevRotationYaw + (thePlayer.rotationYaw - thePlayer.prevRotationYaw) * float1;
        final Vec3D orientCamera = this.orientCamera(float1);
        final float cos = MathHelper.cos(-n2 * 0.017453292f - 3.1415927f);
        final float sin = MathHelper.sin(-n2 * 0.017453292f - 3.1415927f);
        final float n3 = -MathHelper.cos(-n * 0.017453292f);
        final float sin2 = MathHelper.sin(-n * 0.017453292f);
        final float n4 = sin * n3;
        final float n6 = cos * n3;
        double n7 = this.mc.playerController.getBlockReachDistance();
        this.mc.objectMouseOver = this.mc.theWorld.rayTraceBlocks(orientCamera, orientCamera.addVector(n4 * n7, sin2 * n7, n6 * n7));
        double distanceTo = n7;
        final Vec3D orientCamera2 = this.orientCamera(float1);
        if (this.mc.objectMouseOver != null) {
            distanceTo = this.mc.objectMouseOver.hitVec.distanceTo(orientCamera2);
        }
        if (this.mc.playerController instanceof PlayerControllerTest) {
            n7 = 32.0;
        } else {
            if (distanceTo > 3.0) {
                distanceTo = 3.0;
            }
            n7 = distanceTo;
        }
        final Vec3D addVector = orientCamera2.addVector(n4 * n7, sin2 * n7, n6 * n7);
        this.pointedEntity = null;
        final List<Entity> entitiesWithinAABBExcludingEntity = this.mc.theWorld.getEntitiesWithinAABBExcludingEntity(thePlayer, thePlayer.boundingBox.addCoord(n4 * n7, sin2 * n7, n6 * n7));
        double n8 = 0.0;
        for (Entity entity : entitiesWithinAABBExcludingEntity) {
            if (entity.canBeCollidedWith()) {
                final float n9 = 0.1f;
                final MovingObjectPosition calculateIntercept = entity.boundingBox.expand(n9, n9, n9).calculateIntercept(orientCamera2, addVector);
                if (calculateIntercept != null) {
                    final double distanceTo2 = orientCamera2.distanceTo(calculateIntercept.hitVec);
                    if (distanceTo2 < n8 || n8 == 0.0) {
                        this.pointedEntity = entity;
                        n8 = distanceTo2;
                    }
                }
            }
        }
        if (this.pointedEntity != null && !(this.mc.playerController instanceof PlayerControllerTest)) {
            this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity);
        }
    }

    // Control FOV here
    private float getFOVModifier(final float partialTicks, boolean useFOVSettings) {
        final EntityPlayerSP thePlayer = this.mc.thePlayer;
        float n = useFOVSettings ? 90.0f : 70.0f;
        if (thePlayer.isInsideOfMaterial(Material.water)) {
            n -= 10.0f;
        }
        if (thePlayer.health <= 0) {
            n /= (1.0f - 500.0f / (thePlayer.deathTime + partialTicks + 500.0f)) * 2.0f + 1.0f;
        }
        return n;
    }

    private void hurtCameraEffect(final float float1) {
        final EntityPlayerSP thePlayer = this.mc.thePlayer;
        float sin = thePlayer.hurtTime - float1;
        if (thePlayer.health <= 0) {
            final float attackedAtYaw = thePlayer.deathTime + float1;
            GL11.glRotatef(40.0f - 8000.0f / (attackedAtYaw + 200.0f), 0.0f, 0.0f, 1.0f);
        }
        if (sin < 0.0f) {
            return;
        }
        sin /= thePlayer.maxHurtTime;
        sin = MathHelper.sin(sin * sin * sin * sin * 3.1415927f);
        final float attackedAtYaw = thePlayer.attackedAtYaw;
        GL11.glRotatef(-attackedAtYaw, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-sin * 14.0f, 0.0f, 0.0f, 1.0f);
        GL11.glRotatef(attackedAtYaw, 0.0f, 1.0f, 0.0f);
    }

    private void setupViewBobbing(final float partialTick) {
        if (this.mc.gameSettings.thirdPersonView > 0) {
            return;
        }
        final EntityPlayerSP thePlayer = this.mc.thePlayer;
        final float n = thePlayer.distanceWalkedModified + (thePlayer.distanceWalkedModified - thePlayer.prevDistanceWalkedModified) * partialTick;
        final float n2 = thePlayer.prevCameraYaw + (thePlayer.cameraYaw - thePlayer.prevCameraYaw) * partialTick;
        final float n3 = thePlayer.prevCameraPitch + (thePlayer.cameraPitch - thePlayer.prevCameraPitch) * partialTick;
        GL11.glTranslatef(MathHelper.sin(n * 3.1415927f) * n2 * 0.5f, -Math.abs(MathHelper.cos(n * 3.1415927f) * n2), 0.0f);
        GL11.glRotatef(MathHelper.sin(n * 3.1415927f) * n2 * 3.0f, 0.0f, 0.0f, 1.0f);
        GL11.glRotatef(Math.abs(MathHelper.cos(n * 3.1415927f + 0.2f) * n2) * 5.0f, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(n3, 1.0f, 0.0f, 0.0f);
    }

    private void rayTraceView(final float partialTicks) {
        final EntityPlayerSP entity = this.mc.thePlayer;
        final double double1 = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
        final double double2 = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks;
        final double double3 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
        if (this.mc.gameSettings.thirdPersonView > 0) {
            double n = 4.0;
            float yaw = entity.rotationYaw;
            float pitch = entity.rotationPitch;

            if (this.mc.gameSettings.thirdPersonView == 2)
            {
                pitch += 180.0F;
            }
            final double n2 = MathHelper.sin(-yaw / 180.0f * 3.1415927f) * MathHelper.cos(pitch / 180.0f * 3.1415927f) * n;
            final double n3 = MathHelper.cos(yaw / 180.0f * 3.1415927f) * MathHelper.cos(pitch / 180.0f * 3.1415927f) * n;
            final double n4 = MathHelper.sin(-pitch / 180.0f * 3.1415927f) * n;
            for (int i = 0; i < 8; ++i) {
                float n5 = (float) ((i & 0x1) * 2 - 1);
                float n6 = (float) ((i >> 1 & 0x1) * 2 - 1);
                float n7 = (float) ((i >> 2 & 0x1) * 2 - 1);
                n5 *= 0.1f;
                n6 *= 0.1f;
                n7 *= 0.1f;
                final MovingObjectPosition rayTraceBlocks = this.mc.theWorld.rayTraceBlocks(Vec3D.createVector(double1 + n5, double2 + n6, double3 + n7), Vec3D.createVector(double1 - n2 + n5 + n7, double2 - n4 + n6, double3 - n3 + n7));
                if (rayTraceBlocks != null) {
                    final double distanceTo = rayTraceBlocks.hitVec.distanceTo(Vec3D.createVector(double1, double2, double3));
                    if (distanceTo < n) {
                        n = distanceTo;
                    }
                }
            }
            GL11.glTranslatef(0.0f, 0.0f, (float) (-n));
        } else {
            GL11.glTranslatef(0.0f, 0.0f, -0.1f);
        }
        if (this.mc.gameSettings.thirdPersonView == 2)
        {
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        }
        GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0f, 0.0f, 1.0f, 0.0f);
    }

    private void setupCameraTransform(final float partialTicks, final int pass) {
        this.farPlaneDistance = (float) (256 >> this.mc.gameSettings.renderDistance);

        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();

        final float n = 0.07f;
        if (this.mc.gameSettings.anaglyph) {
            GL11.glTranslatef(-(pass * 2 - 1) * n, 0.0f, 0.0f);
        }

        float clipDistance = Math.min(173f, this.farPlaneDistance * 2.0F);

        GLU.gluPerspective(this.getFOVModifier(partialTicks, true), this.mc.displayWidth / (float) this.mc.displayHeight, 0.05f, clipDistance);

        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();

        if (this.mc.gameSettings.anaglyph) {
            GL11.glTranslatef((pass * 2 - 1) * 0.1f, 0.0f, 0.0f);
        }

        this.hurtCameraEffect(partialTicks);

        if (this.mc.gameSettings.viewBobbing) {
            this.setupViewBobbing(partialTicks);
        }

        this.rayTraceView(partialTicks);
    }

    private void renderHand(final float partialTicks, final int pass) {
        this.farPlaneDistance = (float) (256 >> this.mc.gameSettings.renderDistance);

        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();

        if (this.mc.gameSettings.anaglyph) {
            GL11.glTranslatef((pass * 2 - 1) * 0.1f, 0.0f, 0.0f);
        }

        GLU.gluPerspective(this.getFOVModifier(partialTicks, false), this.mc.displayWidth / (float) this.mc.displayHeight, 0.05f, this.farPlaneDistance * 2);

        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();

        GL11.glPushMatrix();

        if (this.mc.gameSettings.viewBobbing) {
            this.setupViewBobbing(partialTicks);
        }

        if (this.mc.gameSettings.thirdPersonView == 0) {
            this.itemRenderer.renderItemInFirstPerson(partialTicks);
        }

        GL11.glPopMatrix();

        if (this.mc.gameSettings.thirdPersonView == 0) {
            this.itemRenderer.renderOverlays(partialTicks);
        }
    }

    public void updateCameraAndRender(final float float1) {
        if (this.anaglyphEnable && !Display.isActive()) {
            this.mc.displayInGameMenu();
        }
        this.anaglyphEnable = Display.isActive();
        if (this.mc.inGameHasFocus) {
            final int entityRendererInt1 = Mouse.getDX();
            final int scaledWidth = Mouse.getDY();
            this.mc.mouseHelper.mouseXYChange();
            int scaledHeight = 1;
            if (this.mc.gameSettings.invertMouse) {
                scaledHeight = -1;
            }
            if (this.entityRendererInt1 != 0) {
                this.entityRendererInt1 = 0;
            }
            if (this.entityRendererInt2 != 0) {
                this.entityRendererInt2 = 0;
            }
            if (entityRendererInt1 != 0) {
                this.entityRendererInt1 = entityRendererInt1;
            }
            if (scaledWidth != 0) {
                this.entityRendererInt2 = scaledWidth;
            }
            this.mc.thePlayer.setAngles((float) entityRendererInt1, (float) (scaledWidth * scaledHeight));
        }
        if (this.mc.skipRenderWorld) {
            return;
        }
        final ScaledResolution scaledResolution = new ScaledResolution(this.mc.displayWidth, this.mc.displayHeight);
        final int scaledWidth = scaledResolution.getScaledWidth();
        int scaledHeight = scaledResolution.getScaledHeight();
        final int n = Mouse.getX() * scaledWidth / this.mc.displayWidth;
        final int n2 = scaledHeight - Mouse.getY() * scaledHeight / this.mc.displayHeight - 1;
        if (this.mc.theWorld != null) {
            this.renderWorld(float1);
            this.mc.ingameGUI.renderGameOverlay(float1, this.mc.currentScreen != null, n, n2);
        } else {
            GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
            GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            GL11.glClear(16640);
            GL11.glMatrixMode(5889);
            GL11.glLoadIdentity();
            GL11.glMatrixMode(5888);
            GL11.glLoadIdentity();
            this.setupOverlayRendering();
        }
        if (this.mc.currentScreen != null) {
            GL11.glClear(256);
            this.mc.currentScreen.drawScreen(n, n2, float1);
        }
    }

    public void renderWorld(final float float1) {
        this.getMouseOver(float1);
        final EntityPlayerSP thePlayer = this.mc.thePlayer;
        final RenderGlobal renderGlobal = this.mc.renderGlobal;
        final EffectRenderer effectRenderer = this.mc.effectRenderer;
        final double xCoord = thePlayer.lastTickPosX + (thePlayer.posX - thePlayer.lastTickPosX) * float1;
        final double yCoord = thePlayer.lastTickPosY + (thePlayer.posY - thePlayer.lastTickPosY) * float1;
        final double zCoord = thePlayer.lastTickPosZ + (thePlayer.posZ - thePlayer.lastTickPosZ) * float1;
        for (int i = 0; i < 2; ++i) {
            if (this.mc.gameSettings.anaglyph) {
                if (i == 0) {
                    GL11.glColorMask(false, true, true, false);
                } else {
                    GL11.glColorMask(true, false, false, false);
                }
            }
            GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
            this.updateFogColor(float1);
            GL11.glClear(16640);
            GL11.glEnable(2884);
            this.setupCameraTransform(float1, i);
            ClippingHelperImpl.getInstance();
            if (this.mc.gameSettings.renderDistance < 2) {
                this.setupFog(-1);
                renderGlobal.renderSky(float1);
            }
            GL11.glEnable(2912);
            this.setupFog(1);
            final Frustrum frustrum = new Frustrum();
            frustrum.setPosition(xCoord, yCoord, zCoord);
            this.mc.renderGlobal.clipRenderersByFrustrum(frustrum, float1);
            this.mc.renderGlobal.updateRenderers(thePlayer, false);
            this.setupFog(0);
            GL11.glEnable(2912);
            GlStateManager.bindTexture(this.mc.renderEngine.getTexture("/terrain.png"));
            RenderHelper.disableStandardItemLighting();
            renderGlobal.sortAndRender(thePlayer, 0, float1);
            RenderHelper.enableStandardItemLighting();
            renderGlobal.renderEntities(this.orientCamera(float1), frustrum, float1);
            effectRenderer.renderLitParticles(thePlayer, float1);
            RenderHelper.disableStandardItemLighting();
            this.setupFog(0);
            effectRenderer.renderParticles(thePlayer, float1);
            if (this.mc.objectMouseOver != null && thePlayer.isInsideOfMaterial(Material.water)) {
                GL11.glDisable(3008);
                renderGlobal.drawBlockBreaking(thePlayer, this.mc.objectMouseOver, 0, thePlayer.inventory.getCurrentItem(), float1);
                renderGlobal.drawSelectionBox(thePlayer, this.mc.objectMouseOver, 0, thePlayer.inventory.getCurrentItem(), float1);
                GL11.glEnable(3008);
            }
            GL11.glBlendFunc(770, 771);
            this.setupFog(0);
            GL11.glEnable(3042);
            GL11.glDisable(2884);
            GlStateManager.bindTexture(this.mc.renderEngine.getTexture("/terrain.png"));
            if (this.mc.gameSettings.fancyGraphics) {
                GL11.glColorMask(false, false, false, false);
                final int sortAndRender = renderGlobal.sortAndRender(thePlayer, 1, float1);
                GL11.glColorMask(true, true, true, true);
                if (this.mc.gameSettings.anaglyph) {
                    if (i == 0) {
                        GL11.glColorMask(false, true, true, false);
                    } else {
                        GL11.glColorMask(true, false, false, false);
                    }
                }
                if (sortAndRender > 0) {
                    renderGlobal.renderAllRenderLists(1, float1);
                }
            } else {
                renderGlobal.sortAndRender(thePlayer, 1, float1);
            }
            GL11.glDepthMask(true);
            GL11.glEnable(2884);
            GL11.glDisable(3042);
            if (this.mc.objectMouseOver != null && !thePlayer.isInsideOfMaterial(Material.water)) {
                GL11.glDisable(3008);
                renderGlobal.drawBlockBreaking(thePlayer, this.mc.objectMouseOver, 0, thePlayer.inventory.getCurrentItem(), float1);
                renderGlobal.drawSelectionBox(thePlayer, this.mc.objectMouseOver, 0, thePlayer.inventory.getCurrentItem(), float1);
                GL11.glEnable(3008);
            }
            GL11.glDisable(2912);
            if (this.mc.theWorld.isRaining()) {
                this.renderRainSnow(float1);
            }
            this.setupFog(0);
            GL11.glEnable(2912);
            renderGlobal.renderClouds(float1);
            GL11.glDisable(2912);
            this.setupFog(1);
            GL11.glClear(256);
            this.renderHand(float1, i);
            if (!this.mc.gameSettings.anaglyph) {
                return;
            }
        }
        GL11.glColorMask(true, true, true, false);
    }

    private void renderRainSnowOld(final float float1) {
        final EntityPlayerSP thePlayer = this.mc.thePlayer;
        final World theWorld = this.mc.theWorld;
        final int floor_double = MathHelper.floor_double(thePlayer.posX);
        final int floor_double2 = MathHelper.floor_double(thePlayer.posY);
        final int floor_double3 = MathHelper.floor_double(thePlayer.posZ);
        final Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(2884);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glAlphaFunc(516, 0.1f);
        int n = 5;
        for (int i = floor_double - n; i <= floor_double + n; ++i) {
            for (int j = floor_double3 - n; j <= floor_double3 + n; ++j) {
                final int topSolidBlock = theWorld.findTopSolidBlock(i, j);
                int n2 = floor_double2 - n;
                int n3 = floor_double2 + n;
                if (n2 < topSolidBlock) {
                    n2 = topSolidBlock;
                }
                if (n3 < topSolidBlock) {
                    n3 = topSolidBlock;
                }
                final float n4 = 2.0f;
                if (n2 != n3) {
                    final double n5 = ((this.rendererUpdateCount + i * i * 3121 + i * 45238971 + j * j * 418711 + j * 13761 & 31) + (double)float1) / 32.0D * (3.0D + this.random.nextDouble());
                    final double n6 = i + 0.5f - thePlayer.posX;
                    final double n7 = j + 0.5f - thePlayer.posZ;
                    final float n8 = MathHelper.sqrt_double(n6 * n6 + n7 * n7) / n;
                    GlStateManager.bindTexture(this.mc.renderEngine.getTexture("/rain.png"));
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, (1.0f - n8 * n8) * 0.7f);
                    tessellator.startDrawingQuads();
                    double d1 = n2 * n4 / 8.0f + n5 * n4;
                    double d2 = n3 * n4 / 8.0f + n5 * n4;
                    tessellator.addVertexWithUV(i, n2, j, 0.0f * n4, d1);
                    tessellator.addVertexWithUV(i + 1, n2, j + 1, n4, d1);
                    tessellator.addVertexWithUV(i + 1, n3, j + 1, n4, d2);
                    tessellator.addVertexWithUV(i, n3, j, 0.0f * n4, d2);
                    tessellator.addVertexWithUV(i, n2, j + 1, 0.0f * n4, d1);
                    tessellator.addVertexWithUV(i + 1, n2, j, n4, d1);
                    tessellator.addVertexWithUV(i + 1, n3, j, n4, d2);
                    tessellator.addVertexWithUV(i, n3, j + 1, 0.0f * n4, d2);
                    tessellator.draw();
                }
            }
        }
        GL11.glEnable(2884);
        GL11.glDisable(3042);
    }

    protected void renderRainSnow(float partialTicks) {
        Entity entity = this.mc.thePlayer;
        World world = this.mc.theWorld;
        int i = MathHelper.floor_double(entity.posX);
        int j = MathHelper.floor_double(entity.posY);
        int k = MathHelper.floor_double(entity.posZ);
        ModernTessellator tessellator = ModernTessellator.instance;
        ModernWorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.disableCull();
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.alphaFunc(516, 0.1F);
        double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks;
        double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks;
        double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks;
        int i1 = 5;
        int l = MathHelper.floor_double(d1);
        int j1 = -1;
        worldrenderer.setTranslation(-d0, -d1, -d2);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        for (int k1 = k - i1; k1 <= k + i1; ++k1) {
            for (int l1 = i - i1; l1 <= i + i1; ++l1) {
                int i2 = (k1 - k + 16) * 32 + l1 - i + 16;
                double d3 = (double) this.rainXCoords[i2] * 0.5D;
                double d4 = (double) this.rainYCoords[i2] * 0.5D;

                int j2 = this.mc.theWorld.getHighestBlockYAt(l1, k1);
                int k2 = j - i1;
                int l2 = j + i1;

                if (k2 < j2) {
                    k2 = j2;
                }

                if (l2 < j2) {
                    l2 = j2;
                }

                int i3 = Math.max(j2, l);

                if (k2 != l2) {
                    this.random.setSeed(l1 * l1 * 3121L + l1 * 45238971L ^ k1 * k1 * 418711L + k1 * 13761L);

                    if (j1 != 0) {
                        j1 = 0;
                        GlStateManager.bindTexture(mc.renderEngine.getTexture(new ResourceLocation("modern/rain.png")));
                        worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                    }

                    double d5 = ((double) (this.rendererUpdateCount + l1 * l1 * 3121 + l1 * 45238971 + k1 * k1 * 418711 + k1 * 13761 & 31) + (double) partialTicks) / 32.0D * (3.0D + this.random.nextDouble());
                    double d6 = (double) ((float) l1 + 0.5F) - entity.posX;
                    double d7 = (double) ((float) k1 + 0.5F) - entity.posZ;
                    float f2 = MathHelper.sqrt_double(d6 * d6 + d7 * d7) / (float) i1;
                    float f3 = ((1.0F - f2 * f2) * 0.5F + 0.5F) * 1;
                    int j3 = world.getBlockLightValue(l1, i3, k1);
                    int k3 = j3 >> 16 & 65535;
                    int l3 = j3 & 65535;
                    worldrenderer.pos((double) l1 - d3 + 0.5D, k2, (double) k1 - d4 + 0.5D).tex(0.0D, (double) k2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f3).lightmap(k3, l3).endVertex();
                    worldrenderer.pos((double) l1 + d3 + 0.5D, k2, (double) k1 + d4 + 0.5D).tex(1.0D, (double) k2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f3).lightmap(k3, l3).endVertex();
                    worldrenderer.pos((double) l1 + d3 + 0.5D, l2, (double) k1 + d4 + 0.5D).tex(1.0D, (double) l2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f3).lightmap(k3, l3).endVertex();
                    worldrenderer.pos((double) l1 - d3 + 0.5D, l2, (double) k1 - d4 + 0.5D).tex(0.0D, (double) l2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f3).lightmap(k3, l3).endVertex();
                }
            }
        }

        if (j1 == 0) {
            tessellator.draw();
        }

        worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc(516, 0.1F);
    }

    public void setupOverlayRendering() {
        final ScaledResolution scaledResolution = new ScaledResolution(this.mc.displayWidth, this.mc.displayHeight);
        final int scaledWidth = scaledResolution.getScaledWidth();
        final int scaledHeight = scaledResolution.getScaledHeight();
        GL11.glClear(256);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, (double) scaledWidth, (double) scaledHeight, 0.0, 1000.0, 3000.0);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0f, 0.0f, -2000.0f);
    }

    private void updateFogColor(final float float1) {
        final World theWorld = this.mc.theWorld;
        final EntityPlayerSP thePlayer = this.mc.thePlayer;
        float n = 1.0f / (4 - this.mc.gameSettings.renderDistance);
        n = 1.0f - (float) Math.pow((double) n, 0.25);
        final Vec3D skyColor = theWorld.getSkyColor(float1);
        final float n2 = (float) skyColor.xCoord;
        final float n3 = (float) skyColor.yCoord;
        final float n4 = (float) skyColor.zCoord;
        final Vec3D fogColor = theWorld.getFogColor(float1);
        this.fogColorRed = (float) fogColor.xCoord;
        this.fogColorGreen = (float) fogColor.yCoord;
        this.fogColorBlue = (float) fogColor.zCoord;
        this.fogColorRed += (n2 - this.fogColorRed) * n;
        this.fogColorGreen += (n3 - this.fogColorGreen) * n;
        this.fogColorBlue += (n4 - this.fogColorBlue) * n;
        if (thePlayer.isInsideOfMaterial(Material.water)) {
            this.fogColorRed = 0.02f;
            this.fogColorGreen = 0.02f;
            this.fogColorBlue = 0.2f;
        } else if (thePlayer.isInsideOfMaterial(Material.lava)) {
            this.fogColorRed = 0.6f;
            this.fogColorGreen = 0.1f;
            this.fogColorBlue = 0.0f;
        }
        final float n5 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * float1;
        this.fogColorRed *= n5;
        this.fogColorGreen *= n5;
        this.fogColorBlue *= n5;
        if (this.mc.gameSettings.anaglyph) {
            final float fogColorRed = (this.fogColorRed * 30.0f + this.fogColorGreen * 59.0f + this.fogColorBlue * 11.0f) / 100.0f;
            final float fogColorGreen = (this.fogColorRed * 30.0f + this.fogColorGreen * 70.0f) / 100.0f;
            final float fogColorBlue = (this.fogColorRed * 30.0f + this.fogColorBlue * 70.0f) / 100.0f;
            this.fogColorRed = fogColorRed;
            this.fogColorGreen = fogColorGreen;
            this.fogColorBlue = fogColorBlue;
        }
        GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0f);
    }

    private void setupFog(final int integer) {
        final EntityPlayerSP thePlayer = this.mc.thePlayer;
        GL11.glFog(2918, this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0f));
        GL11.glNormal3f(0.0f, -1.0f, 0.0f);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (thePlayer.isInsideOfMaterial(Material.water)) {
            GL11.glFogi(2917, 2048);
            GL11.glFogf(2914, 0.1f);
            float n = 0.4f;
            float n2 = 0.4f;
            float n3 = 0.9f;
            if (this.mc.gameSettings.anaglyph) {
                final float n4 = (n * 30.0f + n2 * 59.0f + n3 * 11.0f) / 100.0f;
                final float n5 = (n * 30.0f + n2 * 70.0f) / 100.0f;
                final float n6 = (n * 30.0f + n3 * 70.0f) / 100.0f;
                n = n4;
                n2 = n5;
                n3 = n6;
            }
        } else if (thePlayer.isInsideOfMaterial(Material.lava)) {
            GL11.glFogi(2917, 2048);
            GL11.glFogf(2914, 2.0f);
            float n = 0.4f;
            float n2 = 0.3f;
            float n3 = 0.3f;
            if (this.mc.gameSettings.anaglyph) {
                final float n4 = (n * 30.0f + n2 * 59.0f + n3 * 11.0f) / 100.0f;
                final float n5 = (n * 30.0f + n2 * 70.0f) / 100.0f;
                final float n6 = (n * 30.0f + n3 * 70.0f) / 100.0f;
                n = n4;
                n2 = n5;
                n3 = n6;
            }
        } else {
            GL11.glFogi(2917, 9729);
            GL11.glFogf(2915, this.farPlaneDistance * 0.25f);
            GL11.glFogf(2916, this.farPlaneDistance);
            if (integer < 0) {
                GL11.glFogf(2915, 0.0f);
                GL11.glFogf(2916, this.farPlaneDistance * 0.8f);
            }
            if (GLContext.getCapabilities().GL_NV_fog_distance) {
                GL11.glFogi(34138, 34139);
            }
        }
        GL11.glEnable(2903);
        GL11.glColorMaterial(1028, 4608);
    }

    private FloatBuffer setFogColorBuffer(final float float1, final float float2, final float float3, final float float4) {
        this.fogColorBuffer.clear();
        this.fogColorBuffer.put(float1).put(float2).put(float3).put(float4);
        this.fogColorBuffer.flip();
        return this.fogColorBuffer;
    }
}
