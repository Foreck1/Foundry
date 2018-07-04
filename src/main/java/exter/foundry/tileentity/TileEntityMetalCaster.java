package exter.foundry.tileentity;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.ICastingRecipe;
import exter.foundry.config.FoundryConfig;
import exter.foundry.recipes.manager.CastingRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityMetalCaster extends TileEntityFoundryPowered
{
    static public final int CAST_TIME = 400000;

    static public final int ENERGY_REQUIRED = 10000;

    static public final int INVENTORY_OUTPUT = 0;
    static public final int INVENTORY_MOLD = 1;
    static public final int INVENTORY_EXTRA = 2;
    static public final int INVENTORY_CONTAINER_DRAIN = 3;
    static public final int INVENTORY_CONTAINER_FILL = 4;
    static public final int INVENTORY_MOLD_STORAGE = 5;
    static public final int INVENTORY_MOLD_STORAGE_SIZE = 9;

    static private final Set<Integer> IH_SLOTS_INPUT = ImmutableSet.of(INVENTORY_EXTRA);
    static private final Set<Integer> IH_SLOTS_OUTPUT = ImmutableSet.of(INVENTORY_OUTPUT);

    private final FluidTank tank;
    private final IFluidHandler fluid_handler;
    private final ItemHandler item_handler;

    private ICastingRecipe current_recipe;

    private int progress;

    public TileEntityMetalCaster()
    {
        super();

        tank = new FluidTank(FoundryAPI.CASTER_TANK_CAPACITY);
        fluid_handler = new FluidHandler(0, 0);
        item_handler = new ItemHandler(getSizeInventory(), IH_SLOTS_INPUT, IH_SLOTS_OUTPUT);

        current_recipe = null;

        addContainerSlot(new ContainerSlot(0, INVENTORY_CONTAINER_DRAIN, false));
        addContainerSlot(new ContainerSlot(0, INVENTORY_CONTAINER_FILL, true));

        update_energy = true;
    }

    private void beginCasting()
    {
        if (current_recipe != null && canCastCurrentRecipe()
                && ((!FoundryConfig.metalCasterPower) || getStoredFoundryEnergy() >= ENERGY_REQUIRED))
        {
            if (FoundryConfig.metalCasterPower)
                useFoundryEnergy(ENERGY_REQUIRED, true);
            progress = 0;
        }
    }

    private boolean canCastCurrentRecipe()
    {
        if (current_recipe.requiresExtra())
        {
            if (!current_recipe.containsExtra(inventory.get(INVENTORY_EXTRA)))
            {
                return false;
            }
        }

        ItemStack recipe_output = current_recipe.getOutput();

        ItemStack inv_output = inventory.get(INVENTORY_OUTPUT);
        if (!inv_output.isEmpty() && (!inv_output.isItemEqual(recipe_output)
                || inv_output.getCount() + recipe_output.getCount() > inv_output.getMaxStackSize()))
        {
            return false;
        }
        return true;
    }

    private void checkCurrentRecipe()
    {
        if (current_recipe == null)
        {
            progress = -1;
            return;
        }

        if (!current_recipe.matchesRecipe(inventory.get(INVENTORY_MOLD), tank.getFluid(),
                inventory.get(INVENTORY_EXTRA)))
        {
            progress = -1;
            current_recipe = null;
            return;
        }
    }

    @Override
    protected IFluidHandler getFluidHandler(EnumFacing facing)
    {
        return fluid_handler;
    }

    @Override
    public int getFoundryEnergyCapacity()
    {
        return 40000;
    }

    @Override
    protected IItemHandler getItemHandler(EnumFacing side)
    {
        return item_handler;
    }

    public int getProgress()
    {
        return progress;
    }

    @Override
    public int getSizeInventory()
    {
        return 14;
    }

    @Override
    public FluidTank getTank(int slot)
    {
        if (slot != 0)
        {
            return null;
        }
        return tank;
    }

    @Override
    public int getTankCount()
    {
        return 1;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack)
    {
        return slot == INVENTORY_EXTRA;
    }

    @Override
    public void readFromNBT(NBTTagCompound compund)
    {
        super.readFromNBT(compund);

        if (compund.hasKey("progress"))
        {
            progress = compund.getInteger("progress");
        }
    }

    @Override
    protected void updateClient()
    {

    }

    @Override
    protected void updateServer()
    {
        super.updateServer();
        int last_progress = progress;

        checkCurrentRecipe();

        if (current_recipe == null)
        {
            current_recipe = CastingRecipeManager.INSTANCE.findRecipe(tank.getFluid(), inventory.get(INVENTORY_MOLD),
                    inventory.get(INVENTORY_EXTRA));
            progress = -1;
        }

        if (progress < 0)
        {
            switch (getRedstoneMode())
            {
            case RSMODE_IGNORE:
                beginCasting();
                break;
            case RSMODE_OFF:
                if (!redstone_signal)
                {
                    beginCasting();
                }
                break;
            case RSMODE_ON:
                if (redstone_signal)
                {
                    beginCasting();
                }
                break;
            case RSMODE_PULSE:
                if (redstone_signal && !last_redstone_signal)
                {
                    beginCasting();
                }
                break;
            }
        }
        else
        {
            if (canCastCurrentRecipe())
            {
                FluidStack input_fluid = current_recipe.getInput();
                int increment = 18000 * current_recipe.getCastingSpeed() / input_fluid.amount;
                if (increment > CAST_TIME / 4)
                {
                    increment = CAST_TIME / 4;
                }
                if (increment < 1)
                {
                    increment = 1;
                }
                progress += increment;

                if (progress >= CAST_TIME)
                {
                    progress = -1;
                    tank.drain(input_fluid.amount, true);
                    if (current_recipe.requiresExtra())
                    {
                        decrStackSize(INVENTORY_EXTRA, current_recipe.getInputExtra().getAmount());
                        updateInventoryItem(INVENTORY_EXTRA);
                    }
                    if (inventory.get(INVENTORY_OUTPUT).isEmpty())
                    {
                        inventory.set(INVENTORY_OUTPUT, current_recipe.getOutput());
                    }
                    else
                    {
                        inventory.get(INVENTORY_OUTPUT).grow(current_recipe.getOutput().getCount());
                    }
                    updateInventoryItem(INVENTORY_OUTPUT);
                    updateTank(0);
                    markDirty();
                }
            }
            else
            {
                progress = -1;
            }
        }

        if (last_progress != progress)
        {
            updateValue("progress", progress);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        if (compound == null)
        {
            compound = new NBTTagCompound();
        }
        super.writeToNBT(compound);
        compound.setInteger("progress", progress);
        return compound;
    }
}
