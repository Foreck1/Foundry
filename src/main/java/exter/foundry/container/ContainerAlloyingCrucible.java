package exter.foundry.container;

import exter.foundry.container.slot.SlotFluidContainer;
import exter.foundry.container.slot.SlotOutput;
import exter.foundry.tileentity.TileEntityAlloyingCrucible;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAlloyingCrucible extends ContainerFoundry
{

    // Slot numbers
    public static final int SLOTS_TE = 0;

    public static final int SLOTS_TE_SIZE = 6;
    public static final int SLOTS_INVENTORY = 6;

    private static final int SLOTS_HOTBAR = 6 + 3 * 9;
    private static final int SLOT_INVENTORY_X = 8;

    private static final int SLOT_INVENTORY_Y = 127;
    private static final int SLOT_HOTBAR_X = 8;

    private static final int SLOT_HOTBAR_Y = 185;
    private final TileEntityAlloyingCrucible te_alloyingcrucible;

    public ContainerAlloyingCrucible(TileEntityAlloyingCrucible ac, EntityPlayer player)
    {
        super(ac);
        te_alloyingcrucible = ac;
        te_alloyingcrucible.openInventory(player);
        int i, j;

        addSlotToContainer(new SlotFluidContainer(te_alloyingcrucible,
                TileEntityAlloyingCrucible.INVENTORY_CONTAINER_INPUT_A_DRAIN, 35, 17));
        addSlotToContainer(new SlotOutput(te_alloyingcrucible,
                TileEntityAlloyingCrucible.INVENTORY_CONTAINER_INPUT_A_FILL, 35, 92));
        addSlotToContainer(new SlotFluidContainer(te_alloyingcrucible,
                TileEntityAlloyingCrucible.INVENTORY_CONTAINER_INPUT_B_DRAIN, 125, 17));
        addSlotToContainer(new SlotOutput(te_alloyingcrucible,
                TileEntityAlloyingCrucible.INVENTORY_CONTAINER_INPUT_B_FILL, 125, 92));
        addSlotToContainer(new SlotFluidContainer(te_alloyingcrucible,
                TileEntityAlloyingCrucible.INVENTORY_CONTAINER_OUTPUT_DRAIN, 80, 17));
        addSlotToContainer(new SlotOutput(te_alloyingcrucible,
                TileEntityAlloyingCrucible.INVENTORY_CONTAINER_OUTPUT_FILL, 80, 92));

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
        return te_alloyingcrucible.isUsableByPlayer(par1EntityPlayer);
    }

    @Override
    public void onContainerClosed(EntityPlayer player)
    {
        super.onContainerClosed(player);
        te_alloyingcrucible.closeInventory(player);
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
