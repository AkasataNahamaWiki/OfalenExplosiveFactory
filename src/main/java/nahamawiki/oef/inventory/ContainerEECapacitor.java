package nahamawiki.oef.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.tileentity.TileEntityEECapacitor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerEECapacitor extends Container {

	private TileEntityEECapacitor tileEntity;
	private int lastHoldingEE;
	private int lastCoolTime;

	public ContainerEECapacitor(EntityPlayer player, TileEntityEECapacitor tileEntity) {
		this.tileEntity = tileEntity;
		int i;

		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
		}
	}

	@Override
	public void addCraftingToCrafters(ICrafting iCrafting) {
		super.addCraftingToCrafters(iCrafting);
		iCrafting.sendProgressBarUpdate(this, 0, this.tileEntity.getHoldingEE());
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < this.crafters.size(); ++i) {
			ICrafting iCrafting = (ICrafting) this.crafters.get(i);

			if (this.lastHoldingEE != this.tileEntity.getHoldingEE()) {
				iCrafting.sendProgressBarUpdate(this, 0, this.tileEntity.getHoldingEE());
			}
			this.lastHoldingEE = this.tileEntity.getHoldingEE();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		if (par1 == 0) {
			this.tileEntity.setHoldingEE(par2);
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
		ItemStack itemStack = null;
		Slot slot = (Slot) this.inventorySlots.get(slotNumber);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemStack1 = slot.getStack();
			itemStack = itemStack1.copy();

			if (slotNumber >= 0 && slotNumber < 27) {
				if (!this.mergeItemStack(itemStack1, 27, 36, false)) {
					return null;
				}
			} else if (slotNumber >= 27 && slotNumber < 36 && !this.mergeItemStack(itemStack1, 0, 27, false)) {
				return null;
			}
			if (itemStack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}

			if (itemStack1.stackSize == itemStack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(player, itemStack1);
		}

		return itemStack;
	}

}
