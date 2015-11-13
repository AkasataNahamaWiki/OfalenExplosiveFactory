package nahamawiki.oef.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.core.OEFItemCore;
import nahamawiki.oef.tileentity.TileEntityEECraftingTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ContainerEECraftingTable extends Container {

	private TileEntityEECraftingTable tileEntity;
	private int lastHoldingEE;

	public ContainerEECraftingTable(EntityPlayer player, TileEntityEECraftingTable tileEntity) {
		this.tileEntity = tileEntity;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				this.addSlotToContainer(new Slot(this.tileEntity, i * 3 + j, j * 18 + 12, i * 18 + 78));
			}
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				this.addSlotToContainer(new SlotUnputable(this.tileEntity, i * 3 + j + 9, j * 18 + 112, i * 18 + 78));
			}
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				this.addSlotToContainer(new SlotCraftingMaterial(this.tileEntity, i * 3 + j + 18, j * 18 + 30, i * 18 + 17));
			}
		}

		this.addSlotToContainer(new SlotUncontrolable(this.tileEntity, 27, 124, 35));
		this.addSlotToContainer(new SlotRecipeSheet(this.tileEntity, 28, 80, 96));

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(player.inventory, i * 9 + j + 9, j * 18 + 8, i * 18 + 140));
			}
		}

		for (int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(player.inventory, i, i * 18 + 8, 198));
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

			if ((0 <= slotNumber && slotNumber < 18) || slotNumber == 28) {
				// 材料・完成品・レシピシートなら、インベントリに移動。
				if (!this.mergeItemStack(itemStack1, 29, 65, true)) {
					return null;
				}
			} else if (!(18 <= slotNumber && slotNumber < 28)) {
				// インベントリ内の場合。
				boolean flag = false;
				for (int i = 0; i < 9; i++) {
					if (tileEntity.getStackInSlot(i + 18) == null)
						continue;
					if (itemStack1.isItemEqual(tileEntity.getStackInSlot(i + 18))) {
						// 材料なら材料スロットに移動。
						if (!this.mergeItemStack(itemStack1, i, i + 1, false)) {
							return null;
						}
						flag = true;
						break;
					}
				}
				if (!flag) {
					if (itemStack1.getItem() == OEFItemCore.recipeSheet) {
						// レシピシートならレシピシートスロットに移動。
						if (!this.mergeItemStack(itemStack1, 28, 29, false)) {
							return null;
						}
					} else if (29 <= slotNumber && slotNumber < 56) {
						// インベントリならクックスロットに移動。
						if (!this.mergeItemStack(itemStack1, 56, 65, false)) {
							return null;
						}
					} else if (56 <= slotNumber && slotNumber < 65 && !this.mergeItemStack(itemStack1, 29, 56, false)) {
						// クイックスロットならインベントリに移動。
						return null;
					}
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

	@Override
	public ItemStack slotClick(int slot, int par2, int par3, EntityPlayer player) {
		ItemStack itemStack = super.slotClick(slot, par2, par3, player);
		if (18 <= slot && slot < 27) {
			// 見本レシピのスロットをクリックしたなら、スロットを書き換えてレシピを読み込む。
			ItemStack itemStack1 = player.inventory.getItemStack();
			if (itemStack1 != null) {
				ItemStack itemStack2 = new ItemStack(itemStack1.getItem(), 1, itemStack1.getItemDamage());
				if (itemStack1.hasTagCompound())
					itemStack2.setTagCompound(itemStack1.getTagCompound());
				tileEntity.setInventorySlotContents(slot, itemStack2);
			} else {
				tileEntity.setInventorySlotContents(slot, null);
			}
			InventoryCrafting crafting = new InventoryCrafting(this, 3, 3);
			for (int i = 18; i < 27; i++) {
				crafting.setInventorySlotContents(i - 18, tileEntity.getStackInSlot(i));
			}
			ItemStack itemStack2 = CraftingManager.getInstance().findMatchingRecipe(crafting, tileEntity.getWorldObj());
			tileEntity.setInventorySlotContents(27, itemStack2);
		}
		return itemStack;
	}

}
