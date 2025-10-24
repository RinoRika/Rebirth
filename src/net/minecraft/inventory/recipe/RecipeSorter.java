
package net.minecraft.inventory.recipe;

import java.util.Comparator;

public class RecipeSorter implements Comparator<IRecipe> {

    public final CraftingManager craftingManager;

    public RecipeSorter(final CraftingManager gy) {
        this.craftingManager = gy;
    }

    public int compare(final IRecipe dw1, final IRecipe dw2) {
        return Integer.compare(dw2.getRecipeSize(), dw1.getRecipeSize());
    }
}
