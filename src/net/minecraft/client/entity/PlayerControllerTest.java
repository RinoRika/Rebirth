
package net.minecraft.client.entity;

import net.minecraft.Minecraft;
import net.minecraft.Session;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PlayerControllerTest extends PlayerController {

    public PlayerControllerTest(final Minecraft aw) {
        super(aw);
        this.field_1064_b = true;
    }

    @Override
    public void func_6473_b(final EntityPlayer gi) {
        for (int i = 0; i < 9; ++i) {
            if (gi.inventory.mainInventory[i] == null) {
                this.mc.thePlayer.inventory.mainInventory[i] = new ItemStack(((Block) Session.registeredBlocksList.get(i)).blockID);
            } else {
                this.mc.thePlayer.inventory.mainInventory[i].stackSize = 1;
            }
        }
    }

    @Override
    public boolean shouldDrawHUD() {
        return false;
    }

    @Override
    public void func_717_a(final World fe) {
        super.func_717_a(fe);
    }

    @Override
    public void updateController() {
    }
}
