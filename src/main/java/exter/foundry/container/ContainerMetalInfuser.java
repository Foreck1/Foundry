package exter.foundry.container;

import exter.foundry.container.slot.SlotFluidContainer;
import exter.foundry.container.slot.SlotOutput;
import exter.foundry.tileentity.TileEntityMetalInfuser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMetalInfuser extends ContainerFoundry
{

    // Slot numbers
    public static final int SLOTS_TE = 0;

    public static final int SLOTS_TE_SIZE = 5;
    public static final int SLOTS_INVENTORY = 5;
    private static final int SLOTS_HOTBAR = 5 + 3 * 9;
    private static final int SLOT_INVENTORY_X = 8;

    private static final int SLOT_INVENTORY_Y = 127;
    private static final int SLOT_HOTBAR_X = 8;

    private static final int SLOT_HOTBAR_Y = 185;
    private final TileEntityMetalInfuser te_infuser;

    public ContainerMetalInfuser(TileEntityMetalInfuser infuser, EntityPlayer player)
    {
        super(infuser);
        te_infuser = infuser;
        te_infuser.openInventory(player);
        int i, j;

        addSlotToContainer(new Slot(te_infuser, TileEntityMetalInfuser.INVENTORY_SUBSTANCE_INPUT, 30, 59));
        addSlotToContainer(
                new SlotFluidContainer(te_infuser, TileEntityMetalInfuser.INVENTORY_CONTAINER_INPUT_INPUT, 74, 15));
        addSlotToContainer(
                new SlotOutput(te_infuser, TileEntityMetalInfuser.INVENTORY_CONTAINER_INPUT_OUTPUT, 74, 102));
        addSlotToContainer(
                new SlotFluidContainer(te_infuser, TileEntityMetalInfuser.INVENTORY_CONTAINER_OUTPUT_INPUT, 123, 15));
        addSlotToContainer(
                new SlotOutput(te_infuser, TileEntityMetalInfuser.INVENTORY_CONTAINER_OUTPUT_OUTPUT, 123, 102));

        //Player Inventory
        for (i = 0; i < 3; ++i)
        {
            for (j = 0; j < 9; ++j)
            {
                addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, SLOT_INVENTORY_X + j * 18,
                        SLOT_INVENTORY_Y + i * 18));
            }
        }
        for (i = 0; i < 9; ++i)
        {
            addSlotToContainer(new Slot(player.inventory, i, SLOT_HOTBAR_X + i * 18, SLOT_HOTBAR_Y));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return te_infuser.isUsableByPlayer(par1EntityPlayer);
    }

    @Override
    public void onContainerClosed(EntityPlayer player)
    {
        super.onContainerClosed(player);
        te_infuser.closeInventory(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot_index)
    {
        ItemStack slot_stack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(slot_index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack stack = slot.getStack();
            slot_stack = stack.copy();

            if (slot_index >= SLOTS_INVENTORY && slot_index <= SLOTS_HOTBAR + 9)
            {
                if (!mergeItemStack(stack, SLOTS_TE, SLOTS_TE + SLOTS_TE_SIZE, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (slot_index >= SLOTS_HOTBAR && slot_index < SLOTS_HOTBAR + 9)
            {
                if (!mergeItemStack(stack, SLOTS_INVENTORY, SLOTS_INVENTORY + 3 * 9, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!mergeItemStack(stack, SLOTS_INVENTORY, SLOTS_HOTBAR + 9, true))
            {
                return ItemStack.EMPTY;
            }

            slot.onSlotChanged();
            if (stack.getCount() == slot_stack.getCount())
            {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, stack);
        }

        return slot_stack;
    }
}
