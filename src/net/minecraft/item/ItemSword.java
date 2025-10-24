
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

public class ItemSword extends Item {

    private final int weaponDamage;

    public ItemSword(final int itemid, final int toolTier) {
        super(itemid);
        this.maxStackSize = 1;
        this.maxDamage = 32 << toolTier;
        this.weaponDamage = 4 + toolTier;
    }

    @Override
    public float getStrVsBlock(final ItemStack hw, final Block gs) {
        return 1.5f;
    }

    @Override
    public void hitEntity(final ItemStack hw, final EntityLiving ka) {
        hw.damageItem(1);
    }

    @Override
    public void onBlockDestroyed(final ItemStack hw, final int integer2, final int integer3, final int integer4, final int integer5) {
        hw.damageItem(2);
    }

    @Override
    public int getDamageVsEntity(final Entity eq) {
        return this.weaponDamage;
    }
}
