
package net.minecraft.client.gui;

import java.io.File;
import net.minecraft.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class GuiCreateWorld extends GuiScreen {

    protected GuiScreen parentGuiScreen;
    protected String screenHeader;
    private boolean createClicked;

    public GuiCreateWorld(final GuiScreen dc) {
        this.screenHeader = "Select world";
        this.createClicked = false;
        this.parentGuiScreen = dc;
    }

    @Override
    public void initGui() {
        final File minecraftDir = Minecraft.getMinecraftDir();
        for (int i = 0; i < 5; ++i) {
            final NBTTagCompound potentiallySavesFolderLocation = World.potentiallySavesFolderLocation(minecraftDir, new StringBuilder().append("World").append(i + 1).toString());
            if (potentiallySavesFolderLocation == null) {
                this.controlList.add(new GuiButton(i, this.width / 2 - 100, this.height / 6 + 24 * i, "- empty -"));
            } else {
                this.controlList.add(new GuiButton(i, this.width / 2 - 100, this.height / 6 + 24 * i, new StringBuilder().append("World ").append(i + 1).toString() + " (" + potentiallySavesFolderLocation.getLong("SizeOnDisk") / 1024L * 100L / 1024L / 100.0f + " MB)"));
            }
        }
        this.initButtons();
    }

    protected String getSaveFileName(final int integer) {
        return (World.potentiallySavesFolderLocation(Minecraft.getMinecraftDir(), new StringBuilder().append("World").append(integer).toString()) != null) ? new StringBuilder().append("World").append(integer).toString() : null;
    }

    public void initButtons() {
        this.controlList.add(new GuiButton(5, this.width / 2 - 100, this.height / 6 + 120 + 12, "Delete world..."));
        this.controlList.add(new GuiButton(6, this.width / 2 - 100, this.height / 6 + 168, "Cancel"));
    }

    @Override
    protected void actionPerformed(final GuiButton iq) {
        if (!iq.enabled) {
            return;
        }
        if (iq.id < 5) {
            this.actionPerformed(iq.id + 1);
        } else if (iq.id == 5) {
            this.id.displayGuiScreen(new GuiDeleteWorld(this));
        } else if (iq.id == 6) {
            this.id.displayGuiScreen(this.parentGuiScreen);
        }
    }

    public void actionPerformed(final int integer) {
        this.id.displayGuiScreen(null);
        if (this.createClicked) {
            return;
        }
        this.createClicked = true;
        this.id.startWorld(new StringBuilder().append("World").append(integer).toString());
        this.id.displayGuiScreen(null);
    }

    @Override
    public void drawScreen(final int integer1, final int integer2, final float float3) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.screenHeader, this.width / 2, 20, 16777215);
        super.drawScreen(integer1, integer2, float3);
    }
}
