package exter.foundry.integration.jei;

import exter.foundry.api.recipe.IAlloyFurnaceRecipe;
import exter.foundry.api.recipe.ICastingRecipe;
import exter.foundry.api.recipe.IMeltingRecipe;
import exter.foundry.block.BlockCastingTable;
import exter.foundry.block.BlockFoundryMachine;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.container.ContainerAlloyFurnace;
import exter.foundry.container.ContainerMeltingCrucible;
import exter.foundry.container.ContainerMetalCaster;
import exter.foundry.container.ContainerMetalInfuser;
import exter.foundry.container.ContainerMoldStation;
import exter.foundry.recipes.manager.AlloyFurnaceRecipeManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JEIFoundryPlugin implements IModPlugin {

	CastingTableJEI table_ingot;
	CastingTableJEI table_plate;
	CastingTableJEI table_rod;
	CastingTableJEI table_block;

	@Override
	public void register(IModRegistry registry) {
		IJeiHelpers helpers = registry.getJeiHelpers();

		table_ingot = new CastingTableJEI(BlockCastingTable.EnumTable.INGOT);
		table_plate = new CastingTableJEI(BlockCastingTable.EnumTable.PLATE);
		table_rod = new CastingTableJEI(BlockCastingTable.EnumTable.ROD);
		table_block = new CastingTableJEI(BlockCastingTable.EnumTable.BLOCK);

		IRecipeTransferRegistry transfer_registry = registry.getRecipeTransferRegistry();

		transfer_registry.addRecipeTransferHandler(ContainerAlloyFurnace.class, FoundryJEIConstants.AF_UID, ContainerAlloyFurnace.SLOTS_TE, ContainerAlloyFurnace.SLOTS_TE_SIZE, ContainerAlloyFurnace.SLOTS_INVENTORY, 36);
		registry.addRecipeCatalyst(new ItemStack(FoundryBlocks.block_alloy_furnace), FoundryJEIConstants.AF_UID);
		registry.handleRecipes(IAlloyFurnaceRecipe.class, AlloyFurnaceJEI.Wrapper::new, FoundryJEIConstants.AF_UID);
		registry.addRecipes(AlloyFurnaceRecipeManager.INSTANCE.getRecipes(), FoundryJEIConstants.AF_UID);
		
		transfer_registry.addRecipeTransferHandler(ContainerMeltingCrucible.class, FoundryJEIConstants.MELT_UID, ContainerMeltingCrucible.SLOTS_TE, ContainerMeltingCrucible.SLOTS_TE_SIZE, ContainerMeltingCrucible.SLOTS_INVENTORY, 36);
		registry.addRecipeCatalyst(FoundryBlocks.block_machine.asItemStack(BlockFoundryMachine.EnumMachine.CRUCIBLE_BASIC), FoundryJEIConstants.MELT_UID);
		registry.addRecipeCatalyst(FoundryBlocks.block_machine.asItemStack(BlockFoundryMachine.EnumMachine.CRUCIBLE_STANDARD), FoundryJEIConstants.MELT_UID);
		registry.addRecipeCatalyst(FoundryBlocks.block_machine.asItemStack(BlockFoundryMachine.EnumMachine.CRUCIBLE_ADVANCED), FoundryJEIConstants.MELT_UID);
		registry.handleRecipes(IMeltingRecipe.class, MeltingJEI.Wrapper::new, FoundryJEIConstants.MELT_UID);
		registry.addRecipes(MeltingRecipeManager.INSTANCE.getRecipes(), FoundryJEIConstants.MELT_UID);
		
		transfer_registry.addRecipeTransferHandler(ContainerMetalCaster.class, FoundryJEIConstants.CAST_UID, ContainerMetalCaster.SLOTS_TE, ContainerMetalCaster.SLOTS_TE_SIZE, ContainerMetalCaster.SLOTS_INVENTORY, 36);
		registry.addRecipeCatalyst(FoundryBlocks.block_machine.asItemStack(BlockFoundryMachine.EnumMachine.CASTER), FoundryJEIConstants.CAST_UID);
		registry.handleRecipes(ICastingRecipe.class, CastingJEI.Wrapper::new, FoundryJEIConstants.CAST_UID);
		registry.addRecipes(CastingRecipeManager.instance.getRecipes(), FoundryJEIConstants.CAST_UID);
		
		
		transfer_registry.addRecipeTransferHandler(ContainerMetalInfuser.class, "foundry.infuser", ContainerMetalInfuser.SLOTS_TE, ContainerMetalInfuser.SLOTS_TE_SIZE, ContainerMetalInfuser.SLOTS_INVENTORY, 36);

		transfer_registry.addRecipeTransferHandler(ContainerMoldStation.class, "foundry.mold", ContainerMoldStation.SLOTS_TE, ContainerMoldStation.SLOTS_TE_SIZE, ContainerMoldStation.SLOTS_INVENTORY, 36);

		registry.addRecipeCatalyst(FoundryBlocks.block_machine.asItemStack(BlockFoundryMachine.EnumMachine.ALLOYMIXER), "foundry.alloymixer");
		registry.addRecipeCatalyst(FoundryBlocks.block_machine.asItemStack(BlockFoundryMachine.EnumMachine.INFUSER), "foundry.infuser");
		registry.addRecipeCatalyst(FoundryBlocks.block_machine.asItemStack(BlockFoundryMachine.EnumMachine.ATOMIZER), "foundry.atomizer");
		registry.addRecipeCatalyst(FoundryBlocks.block_machine.asItemStack(BlockFoundryMachine.EnumMachine.ALLOYING_CRUCIBLE), "foundry.alloyingcrucible");
		registry.addRecipeCatalyst(new ItemStack(FoundryBlocks.block_mold_station), "foundry.mold");
		registry.addRecipeCatalyst(FoundryBlocks.block_casting_table.asItemStack(BlockCastingTable.EnumTable.INGOT), "foundry.casting_table.ingot");
		registry.addRecipeCatalyst(FoundryBlocks.block_casting_table.asItemStack(BlockCastingTable.EnumTable.PLATE), "foundry.casting_table.plate");
		registry.addRecipeCatalyst(FoundryBlocks.block_casting_table.asItemStack(BlockCastingTable.EnumTable.ROD), "foundry.casting_table.rod");
		registry.addRecipeCatalyst(FoundryBlocks.block_casting_table.asItemStack(BlockCastingTable.EnumTable.BLOCK), "foundry.casting_table.block");

		registry.addRecipes(AlloyFurnaceJEI.getRecipes(helpers));
		registry.addRecipes(MeltingJEI.getRecipes(helpers));
		registry.addRecipes(CastingJEI.getRecipes());
		registry.addRecipes(AlloyMixerJEI.getRecipes());
		registry.addRecipes(AlloyingCrucibleJEI.getRecipes());
		registry.addRecipes(InfuserJEI.getRecipes());
		registry.addRecipes(AtomizerJEI.getRecipes());
		registry.addRecipes(MoldStationJEI.getRecipes(helpers));
		registry.addRecipes(table_ingot.getRecipes());
		registry.addRecipes(table_plate.getRecipes());
		registry.addRecipes(table_rod.getRecipes());
		registry.addRecipes(table_block.getRecipes());
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		IJeiHelpers helpers = registry.getJeiHelpers();
		registry.addRecipeCategories(new AlloyFurnaceJEI.Category(helpers), 
				new MeltingJEI.Category(helpers), 
				new CastingJEI.Category(helpers), 
				new AlloyMixerJEI.Category(helpers), 
				new AlloyingCrucibleJEI.Category(helpers), 
				new InfuserJEI.Category(helpers), 
				new AtomizerJEI.Category(helpers), 
				new MoldStationJEI.Category(helpers), 
				table_ingot.new Category(helpers), 
				table_plate.new Category(helpers), 
				table_rod.new Category(helpers), 
				table_block.new Category(helpers));
	}
	
}
