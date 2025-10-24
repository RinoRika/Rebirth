
package net.minecraft.client.renderer.entity;

import net.minecraft.client.entity.model.ModelBase;
import net.minecraft.entity.EntitySheep;

public class RenderSheep extends RenderLiving {

    public RenderSheep(final ModelBase it1, final ModelBase it2, final float float3) {
        super(it1, float3);
        this.setRenderPassModel(it2);
    }

    protected boolean shouldRenderPass(final EntitySheep eVar, final int i) {
        this.loadTexture("/mob/sheep_fur.png");
        return i == 0 && !eVar.sheared;
    }
}
