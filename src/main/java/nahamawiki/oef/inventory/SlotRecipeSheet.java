package nahamawiki.oef.inventory;

import nahamawiki.oef.core.OEFItemCore;
import nahamawiki.oef.tileentity.TileEntityEECraftingTable;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SlotRecipeSheet extends Slot {

	private TileEntityEECraftingTable tileEntity;

	public SlotRecipeSheet(TileEntityEECraftingTable tileEntity, int index, int x, int y) {
		super(tileEntity, index, x, y);
		this.tileEntity = tileEntity;
	}

	@Override
	public boolean isItemValid(ItemStack itemStack) {
		return itemStack.getItem() == OEFItemCore.recipeSheet;
	}

	@Override
	public void onSlotChanged() {
		super.onSlotChanged();
		if (this.getStack() == null)
			return;
		if (!this.getStack().hasTagCompound()) {
			if (tileEntity.getStackInSlot(27) == null)
				return;
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setBoolean("IsWrited", true);
			tileEntity.writeToRecipeSheet(nbt);
			this.getStack().setTagCompound(nbt);
		} else if (!this.getStack().getTagCompound().getBoolean("IsWrited")) {
			if (tileEntity.getStackInSlot(27) == null)
				return;
			NBTTagCompound nbt = this.getStack().getTagCompound();
			nbt.setBoolean("IsWrited", true);
			tileEntity.writeToRecipeSheet(nbt);
			this.getStack().setTagCompound(nbt);
		} else {
			tileEntity.readFromRecipeSheet(this.getStack().getTagCompound());
		}
	}

}
