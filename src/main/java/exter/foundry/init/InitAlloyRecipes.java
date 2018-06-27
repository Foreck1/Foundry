package exter.foundry.init;

import exter.foundry.api.recipe.matcher.IItemMatcher;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.api.recipe.matcher.OreMatcher;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.config.FoundryConfig;
import exter.foundry.fluid.FoundryFluids;
import exter.foundry.fluid.LiquidMetalRegistry;
import exter.foundry.recipes.manager.AlloyFurnaceRecipeManager;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.AlloyingCrucibleRecipeManager;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class InitAlloyRecipes {
	// Create recipes for all alloy making machines.
	static private void addSimpleAlloy(String output, String input_a, int amount_a, String input_b, int amount_b) {
		ItemStack alloy_ingot = FoundryMiscUtils.getModItemFromOreDictionary(FoundryConfig.prefModID, "ingot" + output, amount_a + amount_b);
		if (!alloy_ingot.isEmpty()) {
			checkAndAddRecipe(alloy_ingot, input_a, amount_a, input_b, amount_b);
		}

		Fluid fluid_out = LiquidMetalRegistry.INSTANCE.getFluid(output);
		Fluid fluid_in_a = LiquidMetalRegistry.INSTANCE.getFluid(input_a);
		Fluid fluid_in_b = LiquidMetalRegistry.INSTANCE.getFluid(input_b);

		AlloyingCrucibleRecipeManager.INSTANCE.addRecipe(new FluidStack(fluid_out, (amount_a + amount_b) * 3), new FluidStack(fluid_in_a, amount_a * 3), new FluidStack(fluid_in_b, amount_b * 3));

		AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(fluid_out, amount_a + amount_b), new FluidStack[] { new FluidStack(fluid_in_a, amount_a), new FluidStack(fluid_in_b, amount_b) });
	}

	private static void checkAndAddRecipe(ItemStack alloy_ingot, String input_a, int amount_a, String input_b, int amount_b) {
		IItemMatcher[] a = new IItemMatcher[2];
		IItemMatcher[] b = new IItemMatcher[2];
		if (OreDictionary.doesOreNameExist("ingot" + input_a)) a[0] = new OreMatcher("ingot" + input_a, amount_a);
		if (OreDictionary.doesOreNameExist("dust" + input_a)) a[1] = new OreMatcher("dust" + input_a, amount_a);
		if (OreDictionary.doesOreNameExist("ingot" + input_b)) b[0] = new OreMatcher("ingot" + input_b, amount_b);
		if (OreDictionary.doesOreNameExist("dust" + input_b)) b[1] = new OreMatcher("dust" + input_b, amount_b);

		AlloyFurnaceRecipeManager.INSTANCE.addRecipe(alloy_ingot, a, b);
	}

	static public void init() {

		AlloyFurnaceRecipeManager.INSTANCE.addRecipe(new ItemStack(FoundryBlocks.block_refractory_glass), new ItemStackMatcher(Blocks.SAND), new ItemStackMatcher(Items.CLAY_BALL));

		addSimpleAlloy("Bronze", "Copper", 3, "Tin", 1);

		addSimpleAlloy("Brass", "Copper", 3, !OreDictionary.getOres("ingotZinc", false).isEmpty() ? "Zinc" : "Aluminium", 1);

		addSimpleAlloy("Invar", "Iron", 2, "Nickel", 1);

		addSimpleAlloy("Electrum", "Gold", 1, "Silver", 1);

		addSimpleAlloy("Cupronickel", "Copper", 1, "Nickel", 1);

		Fluid liquid_redstone = FluidRegistry.getFluid("liquidredstone");
		Fluid liquid_glowstone = FluidRegistry.getFluid("liquidglowstone");
		Fluid liquid_enderpearl = FluidRegistry.getFluid("liquidenderpearl");

		if (liquid_redstone != null) AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(FoundryFluids.liquid_signalum, 108), new FluidStack[] { new FluidStack(FoundryFluids.liquid_copper, 81), new FluidStack(FoundryFluids.liquid_silver, 27), new FluidStack(liquid_redstone, 250) });

		if (liquid_glowstone != null) AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(FoundryFluids.liquid_lumium, 108), new FluidStack[] { new FluidStack(FoundryFluids.liquid_tin, 81), new FluidStack(FoundryFluids.liquid_silver, 27), new FluidStack(liquid_glowstone, 250) });

		if (liquid_enderpearl != null) AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(FoundryFluids.liquid_enderium, 108), new FluidStack[] { new FluidStack(FoundryFluids.liquid_tin, 54), new FluidStack(FoundryFluids.liquid_silver, 27), new FluidStack(FoundryFluids.liquid_platinum, 27), new FluidStack(liquid_enderpearl, 250) });

		if (FoundryConfig.recipe_steel) {
			InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(FoundryFluids.liquid_steel, 36), new FluidStack(FoundryFluids.liquid_iron, 36), new OreMatcher("dustCoal"), 160000);
			InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(FoundryFluids.liquid_steel, 12), new FluidStack(FoundryFluids.liquid_iron, 12), new OreMatcher("dustCharcoal"), 160000);
			if (OreDictionary.doesOreNameExist("dustSmallCoal")) InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(FoundryFluids.liquid_steel, 9), new FluidStack(FoundryFluids.liquid_iron, 9), new OreMatcher("dustSmallCoal"), 40000);
			if (OreDictionary.doesOreNameExist("dustSmallCharcoal")) InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(FoundryFluids.liquid_steel, 3), new FluidStack(FoundryFluids.liquid_iron, 3), new OreMatcher("dustSmallCharcoal"), 40000);
		}
	}
}
