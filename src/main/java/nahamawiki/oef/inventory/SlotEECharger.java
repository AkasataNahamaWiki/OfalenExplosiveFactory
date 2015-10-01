package nahamawiki.oef.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotEECharger extends Slot {

	public SlotEECharger(IInventory iInventory, int index, int x, int y) {
		super(iInventory, index, x, y);
	}

	@Override
	public boolean isItemValid(ItemStack itemStack) {
		if (itemStack != null) {
			if (itemStack.hasTagCompound() && itemStack.getTagCompound().getBoolean("canChargeEE")) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

}
