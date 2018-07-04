package exter.foundry.tileentity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import exter.foundry.block.BlockRefractoryHopper;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankPropertiesWrapper;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TileEntityRefractoryHopper extends TileEntityFoundry
{
    protected class FluidHandler implements IFluidHandler
    {
        private final IFluidTankProperties[] props;

        public FluidHandler()
        {
            props = new IFluidTankProperties[getTankCount()];
            for (int i = 0; i < props.length; i++)
            {
                props[i] = new FluidTankPropertiesWrapper(getTank(i));
            }
        }

        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain)
        {
            return null;
        }

        @Override
        public FluidStack drain(int maxDrain, boolean doDrain)
        {
            return null;
        }

        @Override
        public int fill(FluidStack resource, boolean doFill)
        {
            int result = fillTank(0, resource, doFill);
            if (doFill && resource != null && result > 0)
            {
                next_drain = 12;
            }
            return result;

        }

        @Override
        public IFluidTankProperties[] getTankProperties()
        {
            return props;
        }
    }

    static public final int INVENTORY_CONTAINER_DRAIN = 0;
    static public final int INVENTORY_CONTAINER_FILL = 1;

    private final FluidTank tank;
    private final IFluidHandler fluid_handler;

    private int next_drain;
    private int next_world_drain;
    private int next_fill;

    // Used by world draining system.
    private final boolean[] visited;

    public TileEntityRefractoryHopper()
    {
        visited = new boolean[41 * 20 * 41];

        next_drain = 12;
        next_world_drain = 300;
        next_fill = 3;

        tank = new FluidTank(2000);
        fluid_handler = new FluidHandler();

        addContainerSlot(new ContainerSlot(0, INVENTORY_CONTAINER_DRAIN, false));
        addContainerSlot(new ContainerSlot(0, INVENTORY_CONTAINER_FILL, true));
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {

    }

    @Override
    protected IFluidHandler getFluidHandler(EnumFacing facing)
    {
        EnumFacing side = world.getBlockState(getPos()).getValue(BlockRefractoryHopper.FACING).facing;
        return facing == EnumFacing.UP || facing == side ? fluid_handler : null;
    }

    @Override
    public int getSizeInventory()
    {
        return 2;
    }

    @Override
    public FluidTank getTank(int slot)
    {
        return tank;
    }

    @Override
    public int getTankCount()
    {
        return 1;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return false;
    }

    @Override
    protected void onInitialize()
    {

    }

    @Override
    public void openInventory(EntityPlayer player)
    {

    }

    @Override
    public void readFromNBT(NBTTagCompound compund)
    {
        super.readFromNBT(compund);

        if (compund.hasKey("next_drain"))
        {
            next_drain = compund.getInteger("next_drain");
        }
        if (compund.hasKey("next_world_drain"))
        {
            next_world_drain = compund.getInteger("next_world_drain");
        }
        if (compund.hasKey("next_fill"))
        {
            next_fill = compund.getInteger("next_fill");
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    protected void updateClient()
    {

    }

    @Override
    protected void updateServer()
    {

        // Drain from the the world.
        if (--next_world_drain == 0)
        {
            next_world_drain = 300;

            FluidStack todrain = FoundryMiscUtils.drainFluidFromWorld(world, getPos().add(0, 1, 0), false);

            if (todrain != null && tank.fill(todrain, false) == todrain.amount)
            {
                Fluid drainfluid = todrain.getFluid();
                if (!drainfluid.isGaseous(todrain) && drainfluid.getDensity(todrain) >= 0)
                {
                    int i;
                    for (i = 0; i < 41 * 20 * 41; i++)
                    {
                        visited[i] = false;
                    }

                    List<Integer> queue = new ArrayList<>();
                    Set<Integer> newqueue = new HashSet<>();
                    i = 20 + 20 * 20 * 41; // x = 20, y = 0, z = 20
                    visited[i] = true;
                    int top_y = 0;

                    queue.add(i - 1); // x - 1
                    queue.add(i + 1); // x + 1
                    queue.add(i - 20 * 41); // z - 1
                    queue.add(i + 20 * 41); // z + 1
                    queue.add(i + 41); // y + 1
                    int drainblock = i;
                    do
                    {
                        newqueue.clear();
                        for (int p : queue)
                        {
                            int x = p % 41;
                            int y = p / 41 % 20;
                            int z = p / (41 * 20);

                            todrain = FoundryMiscUtils.drainFluidFromWorld(world, getPos().add(x - 20, y + 1, z - 20),
                                    false);
                            if (todrain != null && todrain.getFluid() == drainfluid
                                    && tank.fill(todrain, false) == todrain.amount)
                            {
                                if (y > top_y)
                                {
                                    top_y = y;
                                }
                                if (y == top_y)
                                {
                                    drainblock = p;
                                }
                                if (x > 0 && !visited[p - 1])
                                {
                                    newqueue.add(p - 1); // x - 1
                                }
                                if (x < 40 && !visited[p + 1])
                                {
                                    newqueue.add(p + 1); // x + 1
                                }
                                if (z > 0 && !visited[p - 20 * 41])
                                {
                                    newqueue.add(p - 20 * 41); // z - 1
                                }
                                if (z < 40 && !visited[p + 20 * 41])
                                {
                                    newqueue.add(p + 20 * 41); // z + 1
                                }
                                if (y < 19 && !visited[p + 41])
                                {
                                    newqueue.add(p + 41); // y + 1
                                }
                            }
                            visited[p] = true;
                        }
                        queue.clear();
                        queue.addAll(newqueue);
                    }
                    while (!queue.isEmpty());

                    int x = drainblock % 41;
                    int z = drainblock / (41 * 20);
                    todrain = FoundryMiscUtils.drainFluidFromWorld(world, getPos().add(x - 20, top_y + 1, z - 20),
                            true);
                    tank.fill(todrain, true);
                    updateTank(0);
                    markDirty();
                }
            }
        }

        if (--next_drain == 0)
        {
            next_drain = 12;

            // Drain from the top TileEntity
            TileEntity source = world.getTileEntity(getPos().add(0, 1, 0));
            if (source != null
                    && source.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN))
            {
                IFluidHandler hsource = source.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
                        EnumFacing.DOWN);
                FluidStack drained = hsource.drain(40, false);
                if (drained != null && !drained.getFluid().isGaseous(drained)
                        && drained.getFluid().getDensity(drained) > 0)
                {
                    drained.amount = tank.fill(drained, false);
                    if (drained.amount > 0)
                    {
                        hsource.drain(drained, true);
                        tank.fill(drained, true);
                        updateTank(0);
                        markDirty();
                    }
                }
            }
        }

        if (--next_fill == 0)
        {
            next_fill = 3;

            // Fill to the sides/bottom
            if (tank.getFluid() != null && tank.getFluid().amount > 0)
            {
                EnumFacing side = world.getBlockState(getPos()).getValue(BlockRefractoryHopper.FACING).facing;
                TileEntity dest = world.getTileEntity(getPos().add(side.getDirectionVec()));
                side = side.getOpposite();
                if (dest != null && dest.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side))
                {
                    IFluidHandler hdest = dest.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
                    FluidStack drained = tank.drain(10, false);
                    if (drained != null)
                    {
                        drained.amount = hdest.fill(drained, false);
                        if (drained.amount > 0)
                        {
                            tank.drain(drained.amount, true);
                            hdest.fill(drained, true);
                            updateTank(0);
                            markDirty();
                        }
                    }
                }
            }
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
        compound.setInteger("next_drain", next_drain);
        compound.setInteger("next_world_drain", next_world_drain);
        compound.setInteger("next_fill", next_fill);
        return compound;
    }
}
