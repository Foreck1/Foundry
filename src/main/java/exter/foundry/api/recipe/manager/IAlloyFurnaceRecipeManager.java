package exter.foundry.api.recipe.manager;

import java.util.List;

import exter.foundry.api.recipe.IAlloyFurnaceRecipe;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import net.minecraft.item.ItemStack;

public interface IAlloyFurnaceRecipeManager
{
    /**
     * Register an Alloy Mixer recipe.
     * @param out Output.
     * @param in_a Input A.
     * @param in_b Input B.
     */
    public void addRecipe(ItemStack out, IItemMatcher in_a, IItemMatcher in_b);

    /**
     * Register an Alloy Mixer recipe using combination of items.
     * @param in_a Inputs A.
     * @param in_b Inputs B.
     */
    public void addRecipe(ItemStack out, IItemMatcher[] in_a, IItemMatcher[] in_b);

    /**
     * Find a valid recipe that contains the given inputs.
     * A recipe is found if the recipe's inputs contains the fluid in the parameters.
     * @param in_a FluidStack for the first input.
     * @param in_b FluidStack for the second input.
     * @param order [Output] Order in which the input fluids are matched.
     * @return
     */
    public IAlloyFurnaceRecipe findRecipe(ItemStack in_a, ItemStack in_b);

    /**
     * Get a list of all the recipes
     * @return List of all the recipes
     */
    public List<IAlloyFurnaceRecipe> getRecipes();

    /**
     * Removes a recipe.
     * @param The recipe to remove.
     */
    public void removeRecipe(IAlloyFurnaceRecipe recipe);
}
