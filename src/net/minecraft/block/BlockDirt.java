
package net.minecraft.block;

import net.minecraft.block.material.Material;

public class BlockDirt extends Block {

    protected BlockDirt(final int blockid, final int blockIndexInTexture) {
        super(blockid, blockIndexInTexture, Material.ground);
    }
}
