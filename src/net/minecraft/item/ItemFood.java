
package net.minecraft.item;

import net.minecraft.entity.EntityPlayer;
import net.minecraft.world.World;

public class ItemFood extends Item {

    private int healAmount;

    public ItemFood(final int itemid, final int healAmount) {
        super(itemid);
        this.healAmount = healAmount;
        this.maxStackSize = 1;
    }

    @Override
    public ItemStack onItemRightClick(final ItemStack hw, final World fe, final EntityPlayer gi) {
        --hw.stackSize;
        gi.heal(this.healAmount);
        return hw;
    }
}
