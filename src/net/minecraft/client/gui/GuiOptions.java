
package net.minecraft.client.gui;

import net.minecraft.client.settings.GameSettings;

public class GuiOptions extends GuiScreen {

    private GuiScreen parentScreen;
    protected String screenTitle;
    private GameSettings options;

    public GuiOptions(final GuiScreen dc, final GameSettings ja) {
        this.screenTitle = "Options";
        this.parentScreen = dc;
        this.options = ja;
    }

    @Override
    public void initGui() {
        for (int i = 0; i < this.options.numberOfOptions; ++i) {
            this.controlList.add(new GuiSmallButton(i, this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), this.options.getOptionString(i)));
        }
        this.controlList.add(new GuiButton(100, this.width / 2 - 100, this.height / 6 + 120 + 12, "Controls..."));
        this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, "Done"));
    }

    @Override
    protected void actionPerformed(final GuiButton iq) {
        if (!iq.enabled) {
            return;
        }
        if (iq.id < 100) {
            this.options.setOptionFloatValue(iq.id, 1);
            iq.displayString = this.options.getOptionString(iq.id);
        }
        if (iq.id == 100) {
            this.id.displayGuiScreen(new GuiControls(this, this.options));
        }
        if (iq.id == 200) {
            this.id.displayGuiScreen(this.parentScreen);
        }
    }

    @Override
    public void drawScreen(final int integer1, final int integer2, final float float3) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
        super.drawScreen(integer1, integer2, float3);
    }
}
