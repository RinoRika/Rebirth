
package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemAxe extends ItemTool {

    private static Block[] blocksEffectiveAgainst;

    public ItemAxe(final int itemid, final int toolTier) {
        super(itemid, 4, toolTier, ItemAxe.blocksEffectiveAgainst);
    }

    static {
        ItemAxe.blocksEffectiveAgainst = new Block[] {
                Block.planks,
                Block.bookshelf,
                Block.wood,
                Block.chest,
                // Stars: Add blocks that should be effective
                Block.workbench,
                Block.door,
                Block.ladder,
                Block.mushroomBrown,
                Block.mushroomRed,
                Block.stairPlanks,
        };
    }
}
