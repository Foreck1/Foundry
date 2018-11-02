package exter.foundry.container;

import exter.foundry.container.slot.SlotFuel;
import exter.foundry.tileentity.TileEntityBurnerHeater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBurnerHeater extends ContainerFoundry
{

    // Slot numbers
    public static final int SLOTS_TE = 0;

    public static final int SLOTS_TE_SIZE = 4;
    public static final int SLOTS_INVENTORY = 4;

    public static final int SLOTS_HOTBAR = 4 + 3 * 9;
    private static final int SLOT_INVENTORY_X = 8;

    private static final int SLOT_INVENTORY_Y = 84;
    private static final int SLOT_HOTBAR_X = 8;

    private static final int SLOT_HOTBAR_Y = 142;
    private final TileEntityBurnerHeater te_burner;

    public ContainerBurnerHeater(TileEntityBurnerHeater burner, EntityPlayer player)
    {
        super(burner);
        te_burner = burner;
        te_burner.openInventory(player);
        int i, j;

        for (i = 0; i < 2; ++i)
        {
            for (j = 0; j < 2; ++j)
            {
                addSlotToContainer(new SlotFuel(te_burner, i * 2 + j, 71 + i * 18, 34 + j * 18));
            }
        }

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
        return te_burner.isUsableByPlayer(par1EntityPlayer);
    }

    @Override
    public void onContainerClosed(EntityPlayer player)
    {
        super.onContainerClosed(player);
        te_burner.closeInventory(player);
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
                if (!mergeItemStack(stack, SLOTS_TE, SLOTS_TE + SLOTS_TE_SIZE, false))
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
