
package net.minecraft.client.renderer.entity;

import net.minecraft.client.entity.model.ModelBiped;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;

public class RenderPlayer extends RenderLiving {

    private final ModelBiped modelBipedMain;
    private final ModelBiped modelArmorChestplate;
    private final ModelBiped modelArmor;
    private static final String[] armorFilenamePrefix;

    public RenderPlayer() {
        super(new ModelBiped(0.0f), 0.5f);
        this.modelBipedMain = (ModelBiped) this.mainModel;
        this.modelArmorChestplate = new ModelBiped(1.0f);
        this.modelArmor = new ModelBiped(0.5f);
    }

    protected boolean shouldRenderPass(final EntityLiving eVar, final int i) {
        final ItemStack armorItemInSlot = ((EntityPlayer) eVar).inventory.armorItemInSlot(3 - i);
        if (armorItemInSlot != null) {
            final Item item = armorItemInSlot.getItem();
            if (item instanceof ItemArmor) {
                this.loadTexture("/armor/" + RenderPlayer.armorFilenamePrefix[((ItemArmor) item).renderIndex] + "_" + ((i == 2) ? 2 : 1) + ".png");
                final ModelBiped renderPassModel = (i == 2) ? this.modelArmor : this.modelArmorChestplate;
                renderPassModel.bipedHead.showModel = (i == 0);
                renderPassModel.bipedHeadwear.showModel = (i == 0);
                renderPassModel.bipedBody.showModel = (i == 1 || i == 2);
                renderPassModel.bipedRightArm.showModel = (i == 1);
                renderPassModel.bipedLeftArm.showModel = (i == 1);
                renderPassModel.bipedRightLeg.showModel = (i == 2 || i == 3);
                renderPassModel.bipedLeftLeg.showModel = (i == 2 || i == 3);
                this.setRenderPassModel(renderPassModel);
                return true;
            }
        }
        return false;
    }

    // Stars: Fix doRender() not override, causing incorrect player model position.
    @Override
    public void doRender(EntityLiving entityLiving, double xCoord, double sqrt_double, double yCoord, float nya1, float nya2) {
        super.doRender(entityLiving, xCoord, sqrt_double - entityLiving.yOffset, yCoord, nya1, nya2);
    }

    public void drawFirstPersonHand() {
        this.modelBipedMain.setRotationAngles(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
        this.modelBipedMain.bipedRightArm.render(0.0625f);
    }

    static {
        armorFilenamePrefix = new String[]{"cloth", "chain", "iron", "diamond", "gold"};
    }
}
