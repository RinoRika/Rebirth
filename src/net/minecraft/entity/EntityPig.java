
package net.minecraft.entity;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityPig extends EntityAnimal {

    public boolean getSaddled;

    public EntityPig(final World world) {
        super(world);
        this.getSaddled = false;
        this.texture = "/mob/pig.png";
        this.setSize(0.9f, 0.9f);
        this.getSaddled = false;
    }

    @Override
    public void writeEntityToNBT(final NBTTagCompound nbtTagCompound) {
        super.writeEntityToNBT(nbtTagCompound);
        nbtTagCompound.setBoolean("Saddle", this.getSaddled);
    }

    @Override
    public void readEntityFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readEntityFromNBT(nbtTagCompound);
        this.getSaddled = nbtTagCompound.getBoolean("Saddle");
    }

    @Override
    protected String livingSound() {
        return "mob.pig";
    }

    @Override
    protected String getHurtSound() {
        return "mob.pig";
    }

    @Override
    protected String getDeathSound() {
        return "mob.pigdeath";
    }

    @Override
    public boolean interact(final EntityPlayer entityPlayer) {
        if (this.getSaddled) {
            entityPlayer.mountEntity(this);
            return true;
        }
        return false;
    }

    @Override
    protected int scoreValue() {
        return Item.porkRaw.shiftedIndex;
    }
}
