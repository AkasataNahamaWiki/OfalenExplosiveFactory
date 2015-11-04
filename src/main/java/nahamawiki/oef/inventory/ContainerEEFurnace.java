package nahamawiki.oef.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.tileentity.TileEntityEEFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class ContainerEEFurnace extends Container {

	private TileEntityEEFurnace tileEntity;
	private int lastHoldingEE;
	private int lastCoolTime;

	public ContainerEEFurnace(EntityPlayer player, TileEntityEEFurnace tileEntity) {
		this.tileEntity = tileEntity;

		this.addSlotToContainer(new Slot(this.tileEntity, 0, 51, 35));
		this.addSlotToContainer(new SlotFurnace(player, tileEntity, 1, 109, 35));
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
		iCrafting.sendProgressBarUpdate(this, 1, this.tileEntity.cookTime);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < this.crafters.size(); ++i) {
			ICrafting iCrafting = (ICrafting) this.crafters.get(i);

			if (this.lastHoldingEE != this.tileEntity.getHoldingEE()) {
				iCrafting.sendProgressBarUpdate(this, 0, this.tileEntity.getHoldingEE());
			}
			if (this.lastCoolTime != this.tileEntity.cookTime) {
				iCrafting.sendProgressBarUpdate(this, 1, this.tileEntity.cookTime);
			}

			this.lastHoldingEE = this.tileEntity.getHoldingEE();
			this.lastCoolTime = this.tileEntity.cookTime;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		if (par1 == 0) {
			this.tileEntity.setHoldingEE(par2);
		} else if (par1 == 1) {
			this.tileEntity.cookTime = (short) par2;
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

			if (slotNumber == 1) {
				// 完成品スロットならインベントリへ移動。
				if (!this.mergeItemStack(itemStack1, 2, 38, true)) {
					return null;
				}
				// 経験値をドロップ。
				slot.onSlotChange(itemStack1, itemStack);
			} else if (slotNumber != 0) {
				// インベントリなら、
				if (FurnaceRecipes.smelting().getSmeltingResult(itemStack1) != null) {
					// 製錬可能なら材料スロットへ移動。
					if (!this.mergeItemStack(itemStack1, 0, 1, false)) {
						return null;
					}
				} else if (slotNumber >= 2 && slotNumber < 29) {
					// インベントリならクイックスロットへ移動。
					if (!this.mergeItemStack(itemStack1, 29, 38, false)) {
						return null;
					}
				} else if (slotNumber >= 29 && slotNumber < 38 && !this.mergeItemStack(itemStack1, 2, 29, false)) {
					// クイックスロットならインベントリへ移動。
					return null;
				}
			} else if (!this.mergeItemStack(itemStack1, 2, 38, false)) {
				// 材料スロットならインベントリへ移動。
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
