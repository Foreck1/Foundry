package exter.foundry.container.slot;

import exter.foundry.config.FoundryConfig;
import exter.foundry.util.MiscUtil;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotFiltered extends Slot
{
    private final ItemStack filter;

    public SlotFiltered(IInventory inventory, int par2, int par3, int par4, ItemStack filter)
    {
        super(inventory, par2, par3, par4);
        this.filter = filter;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        MiscUtil.parseItem(FoundryConfig.CLAYBLOCK);
        return filter.isItemEqual(stack);
    }
}
