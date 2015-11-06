package nahamawiki.oef.inventory;

import net.minecraft.inventory.IInventory;

public class SlotCraftingMaterial extends SlotUncontrolable {

	public SlotCraftingMaterial(IInventory iinventory, int index, int x, int y) {
		super(iinventory, index, x, y);
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

}
