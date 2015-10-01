package nahamawiki.oef.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.tileentity.TileEntityEECharger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerEECharger extends Container {

	private TileEntityEECharger tileEntity;
	private int lastHoldingEE;
	private int lastCoolTime;

	public ContainerEECharger(EntityPlayer player, TileEntityEECharger tileEntity) {
		this.tileEntity = tileEntity;

		this.addSlotToContainer(new SlotEECharger(this.tileEntity, 0, 78, 38));
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
		iCrafting.sendProgressBarUpdate(this, 0, this.tileEntity.holdingEE);
		iCrafting.sendProgressBarUpdate(this, 1, this.tileEntity.coolTime);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < this.crafters.size(); ++i) {
			ICrafting iCrafting = (ICrafting) this.crafters.get(i);

			if (this.lastHoldingEE != this.tileEntity.holdingEE) {
				iCrafting.sendProgressBarUpdate(this, 0, this.tileEntity.holdingEE);
			}
			if (this.lastCoolTime != this.tileEntity.coolTime) {
				iCrafting.sendProgressBarUpdate(this, 1, this.tileEntity.coolTime);
			}

			this.lastHoldingEE = this.tileEntity.holdingEE;
			this.lastCoolTime = this.tileEntity.coolTime;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		if (par1 == 0) {
			this.tileEntity.holdingEE = par2;
		} else if (par1 == 1) {
			this.tileEntity.coolTime = par2;
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.tileEntity.isUseableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
		ItemStack itemStack = null;
		Slot slot = (Slot) this.inventorySlots.get(slotNumber);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemStack1 = slot.getStack();
			itemStack = itemStack1.copy();

			if (slotNumber == 0) {
				if (!this.mergeItemStack(itemStack1, 1, 37, true)) {
					return null;
				}
				slot.onSlotChange(itemStack1, itemStack);
			} else {
				if (itemStack1.stackSize < 2 && itemStack1.hasTagCompound() && itemStack1.getTagCompound().getBoolean("canChargeEE")) {
					if (!this.mergeItemStack(itemStack1, 0, 1, false)) {
						return null;
					}
				} else if (slotNumber >= 1 && slotNumber < 28) {
					if (!this.mergeItemStack(itemStack1, 28, 37, false)) {
						return null;
					}
				} else if (slotNumber >= 28 && slotNumber < 37 && !this.mergeItemStack(itemStack1, 1, 28, false)) {
					return null;
				}
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