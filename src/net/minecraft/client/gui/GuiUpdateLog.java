
package net.minecraft.client.gui;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class GuiUpdateLog extends GuiScreen {

    private final ArrayList<String> logs = new ArrayList<>();

    @Override
    protected void keyTyped(final char character, final int integer) {
    }

    @Override
    public void initGui() {
        this.controlList.clear();
        this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height - 40, "Back"));

        logs.clear();
        logs.add("25w01a");
        logs.add("+ Fix incorrect player model position.");
        logs.add("+ Add compatibilities for java version 8+.");
        logs.add("+ Add ctrl+drop to drop the whole item stack.");
        logs.add("+ Add video background.");
        logs.add("+ Add scaleFactor (GuiScale) with the ability to adjust gui scale.");
        logs.add("+ Fix some blocks are not affected by tool efficiency. (eg. Workbench, Furnace...)");
        logs.add("25w02a");
        logs.add("+ Update most of the textures to new minecraft textures.");
        logs.add("+ Fix the head of furnace displaying stone texture.");
        logs.add("+ Add sprint feature.");
        logs.add("25w03a");
        logs.add("+ Adjust FOV to 90; Fix the whole item renderer.");
        logs.add("+ Improve logging (gun mu?!).");
        logs.add("- Remove rain (F4) feature. Wait for future weather system update.");
        logs.add("- Remove unnecessary bobbing & hurt camera methods invoking.");
        logs.add("25w04a");
        logs.add("+ Add modern minecraft rendering system.");
        logs.add("+ Add new weather system.");
        logs.add("+ Add 2nd perspective.");
        logs.add("+ Fix the 2nd/3rd person player model position influencing first-person hand.");
        logs.add("- Remove indescribable arms movement in 2nd/3rd perspective.");
    }

    @Override
    protected void actionPerformed(final GuiButton button) {
        if (button.id == 0) {
            this.id.displayGuiScreen(new GuiMainMenu());
        }
    }

    @Override
    public void drawScreen(final int integer1, final int integer2, final float float3) {
        this.drawDefaultBackground();
        for (String string : logs) {
            this.drawString(this.fontRenderer, string, 5, 5 + logs.indexOf(string) * 10, 16777215);
        }
        final String s = "Rebirth 25w04a.";
        this.drawString(this.fontRenderer, s, this.width - this.fontRenderer.getStringWidth(s) - 2, this.height - 10, 16777215);
        this.drawString(this.fontRenderer, "Minecraft Infdev-20100618", 2, this.height - 10, 16777215);
        super.drawScreen(integer1, integer2, float3);
    }
}
