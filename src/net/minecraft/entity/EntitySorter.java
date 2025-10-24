
package net.minecraft.entity;

import java.util.Comparator;
import net.minecraft.world.WorldRenderer;

public class EntitySorter implements Comparator<WorldRenderer> {

    private Entity entity;

    public EntitySorter(final Entity eq) {
        this.entity = eq;
    }

    @Override
    public int compare(final WorldRenderer dl1, final WorldRenderer dl2) {
        return (dl1.chunkIndex(this.entity) < dl2.chunkIndex(this.entity)) ? 1 : 0;
    }
}
