
package net.minecraft.client.gui;

import net.minecraft.client.MinecraftRunner;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.VideoUtil;
import org.lwjgl.opengl.GL11;

public class GuiMainMenu extends GuiScreen {

    private final String currentSplash;

    public GuiMainMenu() {
        this.currentSplash = "Give your life a rebirth!";
    }

    @Override
    protected void keyTyped(final char character, final int integer) {
    }

    @Override
    public void initGui() {
        this.controlList.clear();
        this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 48, "Single player"));
        this.controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 72, "Coming soon..."));
        this.controlList.add(new GuiButton(3, this.width / 2 - 100, this.height / 4 + 96, "Update Log"));
        this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, 95, 20, "Options"));
        this.controlList.add(new GuiButton(-1, this.width / 2 + 5, this.height / 4 + 120 + 12, 95, 20, "Exit"));
        this.controlList.get(1).enabled = false;
    }

    @Override
    protected void actionPerformed(final GuiButton iq) {
        if (iq.id == -1) {
            MinecraftRunner.minecraft.shutdown();
        }
        if (iq.id == 0) {
            this.id.displayGuiScreen(new GuiOptions(this, this.id.gameSettings));
        }
        if (iq.id == 1) {
            this.id.displayGuiScreen(new GuiCreateWorld(this));
        }
        if (iq.id == 3) {
            this.id.displayGuiScreen(new GuiUpdateLog());
        }
    }

    @Override
    public void drawScreen(final int integer1, final int integer2, final float float3) {
        drawDefaultBackground();
        final Tessellator instance = Tessellator.instance;
        GlStateManager.bindTexture(this.id.renderEngine.getTexture("/gui/logo.png"));
        final int integer3 = 256;
        final int integer4 = 49;
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        instance.setColorOpaque_I(16777215);
        this.drawTexturedModalRect((this.width - integer3) / 2, 30, 0, 0, integer3, integer4);
        GL11.glPushMatrix();
        GL11.glTranslatef((float) (this.width / 2 + 90), 70.0f, 0.0f);
        GL11.glRotatef(-20.0f, 0.0f, 0.0f, 1.0f);
        float n = 1.8f - MathHelper.abs(MathHelper.sin(System.currentTimeMillis() % 1000L / 1000.0f * 3.1415927f * 2.0f) * 0.1f);
        n = n * 100.0f / (this.fontRenderer.getStringWidth(this.currentSplash) + 32);
        GL11.glScalef(n, n, n);
        this.drawCenteredString(this.fontRenderer, this.currentSplash, 0, -8, 16776960);
        GL11.glPopMatrix();
        final String s = "Rebirth 25w04a.";
        this.drawString(this.fontRenderer, s, this.width - this.fontRenderer.getStringWidth(s) - 2, this.height - 10, 16777215);
        this.drawString(this.fontRenderer, "Minecraft Infdev-20100618", 2, this.height - 10, 16777215);
        final long maxMemory = Runtime.getRuntime().maxMemory();
        final long totalMemory = Runtime.getRuntime().totalMemory();
        final String string = "Free memory: " + (maxMemory - Runtime.getRuntime().freeMemory()) * 100L / maxMemory + "% of " + maxMemory / 1024L / 1024L + "MB";
        this.drawString(this.fontRenderer, string, this.width - this.fontRenderer.getStringWidth(string) - 2, 2, 8421504);
        final String string2 = "Allocated memory: " + totalMemory * 100L / maxMemory + "% (" + totalMemory / 1024L / 1024L + "MB)";
        this.drawString(this.fontRenderer, string2, this.width - this.fontRenderer.getStringWidth(string2) - 2, 12, 8421504);
        super.drawScreen(integer1, integer2, float3);
    }
}
