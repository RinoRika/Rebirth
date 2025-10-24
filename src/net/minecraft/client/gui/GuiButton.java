
package net.minecraft.client.gui;

import net.minecraft.Minecraft;
import net.minecraft.client.font.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class GuiButton extends Gui {

    private int width;
    private int height;
    public int xPosition;
    public int yPosition;
    public String displayString;
    public int id;
    public boolean enabled;
    public boolean enabled2;

    public GuiButton(final int integer1, final int integer2, final int integer3, final String string) {
        this(integer1, integer2, integer3, 200, 20, string);
    }

    protected GuiButton(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final String string) {
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.enabled2 = true;
        this.id = integer1;
        this.xPosition = integer2;
        this.yPosition = integer3;
        this.width = integer4;
        this.height = integer5;
        this.displayString = string;
    }

    public void drawButton(final Minecraft aw, final int integer2, final int integer3) {
        if (!this.enabled2) {
            return;
        }
        final FontRenderer fontRenderer = aw.fontRenderer;
        GlStateManager.bindTexture(aw.renderEngine.getTexture("/gui/gui.png"));
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        int n = 1;
        final boolean b = integer2 >= this.xPosition && integer3 >= this.yPosition && integer2 < this.xPosition + this.width && integer3 < this.yPosition + this.height;
        if (!this.enabled) {
            n = 0;
        } else if (b) {
            n = 2;
        }
        this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + n * 20, this.width / 2, this.height);
        this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + n * 20, this.width / 2, this.height);
        if (!this.enabled) {
            this.drawCenteredString(fontRenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, -6250336);
        } else if (b) {
            this.drawCenteredString(fontRenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 16777120);
        } else {
            this.drawCenteredString(fontRenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 14737632);
        }
    }

    public boolean mousePressed(final int integer1, final int integer2) {
        return this.enabled && integer1 >= this.xPosition && integer2 >= this.yPosition && integer1 < this.xPosition + this.width && integer2 < this.yPosition + this.height;
    }
}
