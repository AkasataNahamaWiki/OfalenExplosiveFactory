package nahamawiki.oef.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.tileentity.TileEntityEEMiner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerEEMiner extends Container {

	private TileEntityEEMiner tileEntity;
	private int lastHoldingEE;
	private int lastCoolTime;

	public ContainerEEMiner(EntityPlayer player, TileEntityEEMiner tileEntity) {
		this.tileEntity = tileEntity;

		int i = 2 * 18 + 1;

		for (int j = 0; j < 6; ++j) {
			for (int k = 0; k < 9; ++k) {
				this.addSlotToContainer(new SlotUnputable(this.tileEntity, k + j * 9, 8 + k * 18, 18 + j * 18));
			}
		}

		for (int j = 0; j < 3; ++j) {
			for (int k = 0; k < 9; ++k) {
				this.addSlotToContainer(new Slot(player.inventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
			}
		}

		for (int j = 0; j < 9; ++j) {
			this.addSlotToContainer(new Slot(player.inventory, j, 8 + j * 18, 161 + i));
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
		return this.tileEntity.isUseableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
		ItemStack itemStack = null;
		Slot slot = (Slot) this.inventorySlots.get(slotNumber);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemStack1 = slot.getStack();
			itemStack = itemStack1.copy();

			if (slotNumber < 54) {
				if (!this.mergeItemStack(itemStack1, 54, 90, true)) {
					return null;
				}
				slot.onSlotChange(itemStack1, itemStack);
			} else {
				if (slotNumber >= 54 && slotNumber < 81) {
					if (!this.mergeItemStack(itemStack1, 81, 90, false)) {
						return null;
					}
				} else if (slotNumber >= 81 && slotNumber < 90 && !this.mergeItemStack(itemStack1, 54, 81, false)) {
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