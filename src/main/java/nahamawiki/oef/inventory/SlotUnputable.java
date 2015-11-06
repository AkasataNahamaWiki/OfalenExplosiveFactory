package nahamawiki.oef.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotUnputable extends Slot {

	public SlotUnputable(IInventory iInventory, int index, int x, int y) {
		super(iInventory, index, x, y);
	}

	@Override
	public boolean isItemValid(ItemStack itemStack) {
		return false;
	}

}
