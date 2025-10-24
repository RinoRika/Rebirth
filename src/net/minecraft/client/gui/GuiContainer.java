
package net.minecraft.client.gui;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.EntityPlayerSP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public abstract class GuiContainer extends GuiScreen {

    private static RenderItem itemRenderer;
    private ItemStack itemStack;
    public int xSize;
    public int ySize;
    protected List<Slot> inventorySlots;

    public GuiContainer() {
        this.itemStack = null;
        this.xSize = 176;
        this.ySize = 166;
        this.inventorySlots = new ArrayList<>();
    }

    @Override
    public void drawScreen(final int integer1, final int integer2, final float float3) {
        this.drawDefaultBackground();
        final int n = (this.width - this.xSize) / 2;
        final int n2 = (this.height - this.ySize) / 2;
        this.drawGuiContainerBackgroundLayer(float3);
        GL11.glPushMatrix();
        GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef((float) n, (float) n2, 0.0f);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(32826);
        for (Slot inventorySlot : this.inventorySlots) {
            this.inventorySlots(inventorySlot);
            if (inventorySlot.isAtCursorPos(integer1, integer2)) {
                GL11.glDisable(2896);
                GL11.glDisable(2929);
                final int xPos = inventorySlot.xPos;
                final int yPos = inventorySlot.yPos;
                this.drawGradientRect(xPos, yPos, xPos + 16, yPos + 16, -2130706433, -2130706433);
                GL11.glEnable(2896);
                GL11.glEnable(2929);
            }
        }
        if (this.itemStack != null) {
            GL11.glTranslatef(0.0f, 0.0f, 32.0f);
            GuiContainer.itemRenderer.drawItemIntoGui(this.fontRenderer, this.id.renderEngine, this.itemStack, integer1 - n - 8, integer2 - n2 - 8);
            GuiContainer.itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.id.renderEngine, this.itemStack, integer1 - n - 8, integer2 - n2 - 8);
        }
        GL11.glDisable(32826);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        this.drawGuiContainerForegroundLayer();
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GL11.glPopMatrix();
    }

    protected void drawGuiContainerForegroundLayer() {
    }

    protected abstract void drawGuiContainerBackgroundLayer(final float float1);

    private void inventorySlots(final Slot gq) {
        final IInventory inventory = gq.inventory;
        final int slotIndex = gq.slotIndex;
        final int xPos = gq.xPos;
        final int yPos = gq.yPos;
        final ItemStack stackInSlot = inventory.getStackInSlot(slotIndex);
        if (stackInSlot == null) {
            final int backgroundIconIndex = gq.getBackgroundIconIndex();
            if (backgroundIconIndex >= 0) {
                GL11.glDisable(2896);
                this.id.renderEngine.bindTexture(this.id.renderEngine.getTexture("/gui/items.png"));
                this.drawTexturedModalRect(xPos, yPos, backgroundIconIndex % 16 * 16, backgroundIconIndex / 16 * 16, 16, 16);
                GL11.glEnable(2896);
                return;
            }
        }
        GuiContainer.itemRenderer.drawItemIntoGui(this.fontRenderer, this.id.renderEngine, stackInSlot, xPos, yPos);
        GuiContainer.itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.id.renderEngine, stackInSlot, xPos, yPos);
    }

    private Slot getSelectedSlot(final int integer1, final int integer2) {
        for (Slot inventorySlot : this.inventorySlots) {
            if (inventorySlot.isAtCursorPos(integer1, integer2)) {
                return inventorySlot;
            }
        }
        return null;
    }

    @Override
    protected void drawSlotInventory(final int integer1, final int integer2, final int integer3) {
        if (integer3 == 0 || integer3 == 1) {
            final Slot a = this.getSelectedSlot(integer1, integer2);
            if (a != null) {
                a.onSlotChanged();
                final ItemStack slotIndex = a.slotIndex();
                if (slotIndex != null || this.itemStack != null) {
                    if (slotIndex != null && this.itemStack == null) {
                        final int integer4 = (integer3 == 0) ? slotIndex.stackSize : ((slotIndex.stackSize + 1) / 2);
                        this.itemStack = a.inventory.decrStackSize(a.slotIndex, integer4);
                        if (slotIndex.stackSize == 0) {
                            a.putStack(null);
                        }
                        a.onPickupFromSlot();
                    } else if (slotIndex == null && a.isItemValid(this.itemStack)) {
                        int integer4 = (integer3 == 0) ? this.itemStack.stackSize : 1;
                        if (integer4 > a.inventory.getInventoryStackLimit()) {
                            integer4 = a.inventory.getInventoryStackLimit();
                        }
                        a.putStack(this.itemStack.splitStack(integer4));
                        if (this.itemStack.stackSize == 0) {
                            this.itemStack = null;
                        }
                    } else if (slotIndex != null) {
                        if (a.isItemValid(this.itemStack)) {
                            if (slotIndex.itemID != this.itemStack.itemID) {
                                if (this.itemStack.stackSize <= a.inventory.getInventoryStackLimit()) {
                                    a.putStack(this.itemStack);
                                    this.itemStack = slotIndex;
                                }
                            } else {
                                if (integer3 == 0) {
                                    int integer4 = this.itemStack.stackSize;
                                    if (integer4 > a.inventory.getInventoryStackLimit() - slotIndex.stackSize) {
                                        integer4 = a.inventory.getInventoryStackLimit() - slotIndex.stackSize;
                                    }
                                    if (integer4 > this.itemStack.getMaxStackSize() - slotIndex.stackSize) {
                                        integer4 = this.itemStack.getMaxStackSize() - slotIndex.stackSize;
                                    }
                                    this.itemStack.splitStack(integer4);
                                    if (this.itemStack.stackSize == 0) {
                                        this.itemStack = null;
                                    }
                                    slotIndex.stackSize += integer4;
                                } else {
                                    int integer4 = 1;
                                    if (integer4 > a.inventory.getInventoryStackLimit() - slotIndex.stackSize) {
                                        integer4 = a.inventory.getInventoryStackLimit() - slotIndex.stackSize;
                                    }
                                    if (integer4 > this.itemStack.getMaxStackSize() - slotIndex.stackSize) {
                                        integer4 = this.itemStack.getMaxStackSize() - slotIndex.stackSize;
                                    }
                                    this.itemStack.splitStack(integer4);
                                    if (this.itemStack.stackSize == 0) {
                                        this.itemStack = null;
                                    }
                                    slotIndex.stackSize += integer4;
                                }
                            }
                        } else if (slotIndex.itemID == this.itemStack.itemID && this.itemStack.getMaxStackSize() > 1) {
                            final int integer4 = slotIndex.stackSize;
                            if (integer4 > 0 && integer4 + this.itemStack.stackSize <= this.itemStack.getMaxStackSize()) {
                                final ItemStack itemStack4 = this.itemStack;
                                itemStack4.stackSize += integer4;
                                slotIndex.splitStack(integer4);
                                if (slotIndex.stackSize == 0) {
                                    a.putStack(null);
                                }
                                a.onPickupFromSlot();
                            }
                        }
                    }
                }
            } else if (this.itemStack != null) {
                final int n = (this.width - this.xSize) / 2;
                final int integer4 = (this.height - this.ySize) / 2;
                if (integer1 < n || integer2 < integer4 || integer1 >= n + this.xSize || integer2 >= integer4 + this.xSize) {
                    final EntityPlayerSP thePlayer = this.id.thePlayer;
                    if (integer3 == 0) {
                        thePlayer.dropPlayerItem(this.itemStack);
                        this.itemStack = null;
                    }
                    if (integer3 == 1) {
                        thePlayer.dropPlayerItem(this.itemStack.splitStack(1));
                        if (this.itemStack.stackSize == 0) {
                            this.itemStack = null;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void drawBackground(final int integer1, final int integer2, final int integer3) {
    }

    @Override
    protected void keyTyped(final char character, final int integer) {
        if (integer == 1 || integer == this.id.gameSettings.keyBindInventory.keyCode) {
            this.id.displayGuiScreen(null);
        }
    }

    @Override
    public void onGuiClosed() {
        if (this.itemStack != null) {
            this.id.thePlayer.dropPlayerItem(this.itemStack);
        }
    }

    public void onCraftMatrixChanged(final IInventory kd) {
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    static {
        GuiContainer.itemRenderer = new RenderItem();//(RenderItem)RenderItem.RenderCreeper();
    }
}
