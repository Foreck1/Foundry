package exter.foundry.recipes;

import exter.foundry.api.recipe.IInfuserRecipe;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Metal Infuser recipe manager
 */
public class InfuserRecipe implements IInfuserRecipe
{

    /**
     * Required fluid.
     */
    public final FluidStack fluid;

    /**
     * Item required.
     */
    public final IItemMatcher item;

    /**
     * Amount of energy needed to extract.
     */
    public final int extract_energy;

    /**
     * Fluid produced.
     */
    public final FluidStack output;

    public InfuserRecipe(FluidStack result, FluidStack in_fluid, IItemMatcher in_item, int energy)
    {
        if (energy < 1)
            throw new IllegalArgumentException("Infuser substance recipe energy nust be > 0.");
        if (in_fluid == null)
            throw new IllegalArgumentException("Infuser recipe input fluid cannot be null!");
        if (result == null)
            throw new IllegalArgumentException("Infuser recipe output cannot be null!");
        if (in_item == null)
            throw new IllegalArgumentException("Infuser Recipe input item cannot be null!");

        item = in_item;
        fluid = in_fluid.copy();
        extract_energy = energy;
        output = result.copy();
    }

    @Override
    public int getEnergyNeeded()
    {
        return extract_energy;
    }

    @Override
    public IItemMatcher getInput()
    {
        return item;
    }

    @Override
    public FluidStack getInputFluid()
    {
        return fluid.copy();
    }

    @Override
    public FluidStack getOutput()
    {
        return output.copy();
    }

    @Override
    public boolean matchesRecipe(FluidStack in_fluid, ItemStack item_stack)
    {
        if (in_fluid == null || item_stack.isEmpty()) return false;
        return item.apply(item_stack) && in_fluid.containsFluid(fluid);
    }

}
