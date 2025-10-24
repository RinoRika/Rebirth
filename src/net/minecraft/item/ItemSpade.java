
package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemSpade extends ItemTool {

    private static Block[] blocksEffectiveAgainst;

    public ItemSpade(final int itemid, final int toolTier) {
        super(itemid, 1, toolTier, ItemSpade.blocksEffectiveAgainst);
    }

    static {
        ItemSpade.blocksEffectiveAgainst = new Block[] {
                Block.grass,
                Block.dirt,
                Block.sand,
                Block.gravel,
                // Stars: Add blocks that should be effective
                Block.tilledField
        };
    }
}
