package nahamawiki.oef.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerEETool extends Container {

	private InventoryEETool inventory;
	/** EEToolのインベントリの第一スロットの番号 */
	private static final int index0 = 0;
	/** プレイヤーのインベントリの第一スロットの番号 */
	private static final int index1 = 54;
	/** クイックスロットの第一スロットの番号 */
	private static final int index2 = 81;
	/** このコンテナの全体のスロット数 */
	private static final int index3 = 90;

	public ContainerEETool(InventoryPlayer inventoryPlayer) {
		inventory = new InventoryEETool(inventoryPlayer);
		inventory.openInventory();

		int i = 2 * 18 + 1;

		for (int j = 0; j < 6; ++j) {
			for (int k = 0; k < 9; ++k) {
				this.addSlotToContainer(new Slot(inventory, k + j * 9, 8 + k * 18, 18 + j * 18));
			}
		}

		for (int j = 0; j < 3; ++j) {
			for (int k = 0; k < 9; ++k) {
				this.addSlotToContainer(new Slot(inventoryPlayer, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
			}
		}

		for (int j = 0; j < 9; ++j) {
			this.addSlotToContainer(new Slot(inventoryPlayer, j, 8 + j * 18, 161 + i));
		}

	}

	/* Containerが開いてられるか */
	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return true;
	}

	@Override
	public ItemStack slotClick(int slotNumber, int par2, int par3, EntityPlayer player) {
		if (slotNumber - index1 == player.inventory.currentItem + 27) {
			return null;
		}
		return super.slotClick(slotNumber, par2, par3, player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(slotNumber);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (slotNumber < this.inventory.getSizeInventory()) {
				if (!this.mergeItemStack(itemstack1, this.inventory.getSizeInventory(), this.inventorySlots.size(), true)) {
					return null;
				}
			} else {
				if (slotNumber - index1 == player.inventory.currentItem + 27)
					return null;
				if (!this.mergeItemStack(itemstack1, 0, this.inventory.getSizeInventory(), false)) {
					return null;
				}
				if (itemstack1.stackSize == 0) {
					slot.putStack((ItemStack) null);
				} else {
					slot.onSlotChanged();
				}
			}
		}

		return itemstack;
	}

	/* Containerを閉じるときに呼ばれる */
	@Override
	public void onContainerClosed(EntityPlayer p_75134_1_) {
		super.onContainerClosed(p_75134_1_);
		this.inventory.closeInventory();
	}

}
