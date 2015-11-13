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
		// レシピシートしか置けないようにする。
		return itemStack.getItem() == OEFItemCore.recipeSheet;
	}

	/** スロットが更新された時の処理。 */
	@Override
	public void onSlotChanged() {
		super.onSlotChanged();
		if (this.getStack() == null)
			return;
		if (!this.getStack().hasTagCompound()) {
			// NBTを持っていないなら今並んでいるレシピを記入する。
			if (tileEntity.getStackInSlot(27) == null)
				return;
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setBoolean("IsWrited", true);
			tileEntity.writeToRecipeSheet(nbt);
			this.getStack().setTagCompound(nbt);
		} else if (!this.getStack().getTagCompound().getBoolean("IsWrited")) {
			// 記入済みだが無効化されていたら記入しなおす。
			if (tileEntity.getStackInSlot(27) == null)
				return;
			NBTTagCompound nbt = this.getStack().getTagCompound();
			nbt.setBoolean("IsWrited", true);
			tileEntity.writeToRecipeSheet(nbt);
			this.getStack().setTagCompound(nbt);
		} else {
			// 記入済みで有効なら反映する。
			tileEntity.readFromRecipeSheet(this.getStack().getTagCompound());
		}
	}

}
