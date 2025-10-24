
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;

public class BlockGlass extends BlockBreakable {

    public BlockGlass(final int blockid, final int blockIndexInTexture, final Material material, final boolean boolean4) {
        super(blockid, blockIndexInTexture, material, boolean4);
    }

    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
}
