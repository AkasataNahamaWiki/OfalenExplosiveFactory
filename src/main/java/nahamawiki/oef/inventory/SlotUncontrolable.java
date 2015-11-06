package nahamawiki.oef.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotUncontrolable extends Slot {

	public SlotUncontrolable(IInventory iinventory, int index, int x, int y) {
		super(iinventory, index, x, y);
	}

	@Override
	public boolean isItemValid(ItemStack itemStack) {
		return false;
	}

	@Override
	public boolean canTakeStack(EntityPlayer player) {
		return false;
	}

}
