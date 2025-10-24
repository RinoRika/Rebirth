
package net.minecraft.entity;

import net.minecraft.Minecraft;
import net.minecraft.Session;
import net.minecraft.client.gui.GuiChest;
import net.minecraft.client.gui.GuiCrafting;
import net.minecraft.client.gui.GuiEditSign;
import net.minecraft.client.gui.GuiFurnace;
import net.minecraft.client.input.MovementInput;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;

public class EntityPlayerSP extends EntityPlayer {

    public MovementInput movementInput;
    private final Minecraft mc;

    public EntityPlayerSP(final Minecraft mc, final World world, final Session session) {
        super(world);
        this.mc = mc;
        if (world != null) {
            if (world.player != null) {
                world.setEntityDead(world.player);
            }
            world.player = this;
        }
        if (session != null && session.username != null && !session.username.isEmpty()) {
            this.skinUrl = "http://www.minecraft.net/skin/" + session.username + ".png";
        }
        this.username = session.username;
    }

    @Override
    public void updatePlayerActionState() {
        this.moveStrafing = this.movementInput.moveStrafe;
        this.moveForward = this.movementInput.moveForward;
        this.isJumping = this.movementInput.jump;
        this.isSprinting = this.movementInput.sprint;

        // Passing sprint logics
        boolean isMoving = Math.abs(this.moveStrafing) > 0.1f || Math.abs(this.moveForward) > 0.1f;

        if (this.isSprinting) {
            this.sprintingToggled = true;
        }

        if (this.sprintingToggled && !isMoving) {
            this.sprintingToggled = false;
            this.isSprinting = false;
        }

        if (this.sprintingToggled) {
            this.isSprinting = true;
        }

        this.wasMoving = isMoving;
    }

    @Override
    public void onLivingUpdate() {
        this.movementInput.updatePlayerMoveState(this);
        super.onLivingUpdate();
    }

    public void resetPlayerKeyState() {
        this.movementInput.resetKeyState();
    }

    public void handleKeyPress(final int integer, final boolean boolean2) {
        this.movementInput.checkKeyForMovementInput(integer, boolean2);
    }

    @Override
    public void writeEntityToNBT(final NBTTagCompound nbtTagCompound) {
        super.writeEntityToNBT(nbtTagCompound);
        nbtTagCompound.setInteger("Score", this.score);
        nbtTagCompound.setTag("Inventory", (NBTBase) this.inventory.writeToNBT(new NBTTagList()));
    }

    @Override
    public void readEntityFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readEntityFromNBT(nbtTagCompound);
        this.score = nbtTagCompound.getInteger("Score");
        this.inventory.readFromNBT(nbtTagCompound.getTagList("Inventory"));
    }

    @Override
    public void displayGUIChest(final IInventory kd) {
        this.mc.displayGuiScreen(new GuiChest(this.inventory, kd));
    }

    @Override
    public void displayGUIEditSign(final TileEntitySign jn) {
        this.mc.displayGuiScreen(new GuiEditSign(jn));
    }

    @Override
    public void displayWorkbenchGUI() {
        this.mc.displayGuiScreen(new GuiCrafting(this.inventory));
    }

    @Override
    public void displayGUIFurnace(final TileEntityFurnace el) {
        this.mc.displayGuiScreen(new GuiFurnace(this.inventory, el));
    }

    public ItemStack getCurrentEquippedItem() {
        return this.inventory.getCurrentItem();
    }

    public void displayGUIInventory() {
        this.inventory.setInventorySlotContents(this.inventory.currentItem, null);
    }

    public void a(final Entity eq) {
        final int damageVsEntity = this.inventory.getDamageVsEntity(eq);
        if (damageVsEntity > 0) {
            eq.attackEntityFrom(this, damageVsEntity);
            final ItemStack currentEquippedItem = this.getCurrentEquippedItem();
            if (currentEquippedItem != null && eq instanceof EntityLiving) {
                currentEquippedItem.hitEntity((EntityLiving) eq);
                if (currentEquippedItem.stackSize <= 0) {
                    currentEquippedItem.onItemDestroyedByUse(this);
                    this.displayGUIInventory();
                }
            }
        }
    }

    @Override
    public void onItemPickup(final Entity eq) {
        this.mc.effectRenderer.addEffect(new EntityPickupFX(this.mc.theWorld, eq, this, -0.5f));
    }

    public int getPlayerArmorValue() {
        return this.inventory.getTotalArmorValue();
    }

    @Override
    public void c(final Entity eq) {
        if (eq.interact(this)) {
            return;
        }
        final ItemStack currentEquippedItem = this.getCurrentEquippedItem();
        if (currentEquippedItem != null && eq instanceof EntityLiving) {
            currentEquippedItem.useItemOnEntity((EntityLiving) eq);
            if (currentEquippedItem.stackSize <= 0) {
                currentEquippedItem.onItemDestroyedByUse(this);
                this.displayGUIInventory();
            }
        }
    }
}
