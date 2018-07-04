package exter.foundry.tileentity;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.ICastingTableRecipe;
import exter.foundry.api.recipe.ICastingTableRecipe.TableType;

public class TileEntityCastingTableIngot extends TileEntityCastingTableBase
{
    public TileEntityCastingTableIngot()
    {
        super();
    }

    @Override
    public int getDefaultCapacity()
    {
        return FoundryAPI.FLUID_AMOUNT_INGOT;
    }

    @Override
    public TableType getTableType()
    {
        return ICastingTableRecipe.TableType.INGOT;
    }
}
