package exter.foundry.api.recipe;

import exter.foundry.api.recipe.matcher.IItemMatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IAtomizerRecipe
{

    /**
     * Get the fluid required for atomizing.
     * @return FluidStack containing the required fluid.
     */
    public FluidStack getInput();

    /**
     * Get the actual item produced by atomizing.
     * @return ItemStack containing the item produced. Can be null if using an Ore Dictionary name with nothing registered with it.
     */
    public ItemStack getOutput();

    /**
     * Get the output's matcher.
     */
    public IItemMatcher getOutputMatcher();

    /**
     * Check if a fluid stack and mold matches this recipe.
     * @param fluid_stack fluid to check (must contain the fluid in the recipe).
     * @return true if the fluid matches, false otherwise.
     */
    public boolean matchesRecipe(FluidStack fluid_stack);
}
