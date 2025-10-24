
package net.minecraft.client.renderer.culling;

import net.minecraft.util.AxisAlignedBB;

public interface ICamera {

    boolean isBoundingBoxInFrustum(final AxisAlignedBB aabb);

    void setPosition(final double xCoord, final double yCoord, final double zCoord);
}
