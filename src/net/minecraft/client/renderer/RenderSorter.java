
package net.minecraft.client.renderer;

import java.util.Comparator;
import net.minecraft.entity.EntityPlayer;
import net.minecraft.world.WorldRenderer;

public class RenderSorter implements Comparator<WorldRenderer> {

    private final EntityPlayer entity;

    public RenderSorter(final EntityPlayer gi) {
        this.entity = gi;
    }

    // Stars: Upgrade sorter to java8+.
    public int compare(final WorldRenderer dl1, final WorldRenderer dl2) {
        final boolean isInFrustum1 = dl1.isInFrustum;
        final boolean isInFrustum2 = dl2.isInFrustum;

        if (isInFrustum1 != isInFrustum2) {
            return isInFrustum1 ? -1 : 1;
        }

        final float chunkIndex1 = dl1.chunkIndex(this.entity);
        final float chunkIndex2 = dl2.chunkIndex(this.entity);

        if (chunkIndex1 != chunkIndex2) {
            return Float.compare(chunkIndex1, chunkIndex2);
        }

        return 0;
    }
}
