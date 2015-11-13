package nahamawiki.oef.tileentity;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;

public class TileEntityEEFurnace extends TileEntityEEMachineBase implements ISidedInventory {

	/** 製錬前のアイテムと製錬後のアイテム。 */
	protected ItemStack[] itemStacks = new ItemStack[2];
	/** 製錬した時間。 */
	public short cookTime;
	/** 前のtickで製錬していたか。 */
	protected boolean isCooking;

	@Override
	public String[] getState() {
		return new String[] {
				StatCollector.translateToLocal("info.EEMachineState.name") + StatCollector.translateToLocal(this.getBlockType().getLocalizedName()),
				StatCollector.translateToLocal("info.EEMachineState.level") + level,
				StatCollector.translateToLocal("info.EEMachineState.capacity") + capacity + " EE",
				StatCollector.translateToLocal("info.EEMachineState.holding") + holdingEE + " EE"
		};
	}

	@Override
	public int getCapacity(int level) {
		return 800;
	}

	@Override
	public void updateMachine() {
		if (!this.canSmelt()) {
			// 製錬ができないならカウントをリセットして終了。
			if (cookTime > 0)
				cookTime = 0;
			this.updateIsHoldingEE();
			return;
		}
		if (cookTime < this.getMaxCookTime()) {
			// 製錬可能で、製錬中ならカウントを進める。
			cookTime++;
			this.updateIsHoldingEE();
		}
		if (cookTime != 0 && cookTime == this.getMaxCookTime()) {
			// 製錬完了時間になったら、製錬する。
			this.smeltItem();
			// カウントをリセットし、EEを消費する。
			cookTime = 0;
			holdingEE -= 400;
		}
	}

	@Override
	public void updateCreepered() {
		// 周囲のモブ以外のEntityを炎上させる。
		List list = this.worldObj.loadedEntityList;
		if (list.isEmpty())
			return;
		for (Object object : list) {
			if (!(object instanceof EntityLivingBase) || object instanceof EntityMob)
				continue;
			EntityLivingBase entity = (EntityLivingBase) object;
			if (entity.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 16 * 16 && !entity.isImmuneToFire() && !entity.isBurning()) {
				entity.setFire(200);
			}
		}
	}

	/** 製錬しているかによってメタデータを更新する。 */
	protected void updateIsHoldingEE() {
		// 前のtickと違ったら更新する。
		if (isCooking != cookTime > 0) {
			if (cookTime > 0) {
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, level | 4, 2);
				isCooking = true;
			} else {
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, level & 3, 2);
				isCooking = false;
			}
			this.markDirty();
		}
	}

	/** 製錬完了までにかかる時間を返す。 */
	protected int getMaxCookTime() {
		switch (level) {
		case 0:
			return 80;
		case 1:
			return 40;
		case 2:
			return 20;
		case 3:
			return 10;
		}
		return 0;
	}

	/** 製錬が可能かどうか。 */
	protected boolean canSmelt() {
		// EE不足/製錬するアイテムがないなら製錬不可。
		if (holdingEE < 400 || itemStacks[0] == null)
			return false;
		if (itemStacks[1] != null) {
			// 完成品スロットがいっぱいなら製錬不可。
			if (itemStacks[1].stackSize >= this.getInventoryStackLimit() || itemStacks[1].stackSize >= itemStacks[1].getMaxStackSize())
				return false;
		}
		// バニラのかまどのレシピから、製錬後のアイテムを取得。
		ItemStack itemStack = FurnaceRecipes.smelting().getSmeltingResult(itemStacks[0]);
		if (itemStack == null) {
			// 製錬レシピに登録されていないなら製錬不可。
			if (itemStacks[1] == null) {
				// 完成品スロットが空ならすべて移動。
				itemStacks[1] = itemStacks[0].copy();
				itemStacks[0] = null;
			} else if (itemStacks[0].isItemEqual(itemStacks[1])) {
				// 製錬前スロットと完成品スロットのアイテムが同じならできるだけ移動。
				int result = itemStacks[0].stackSize + itemStacks[1].stackSize;
				if (result > this.getInventoryStackLimit() || result > itemStacks[1].getMaxStackSize()) {
					int limit = Math.min(this.getInventoryStackLimit(), itemStacks[1].getMaxStackSize());
					itemStacks[0].stackSize = result - limit;
					itemStacks[1].stackSize = limit;
				} else {
					itemStacks[1].stackSize += itemStacks[0].stackSize;
					itemStacks[0] = null;
				}
			}
			return false;
		}
		if (itemStacks[1] == null) {
			// 製錬レシピに登録されていて、完成品スロットが空なら製錬可能。
			return true;
		} else if (itemStacks[1].isItemEqual(itemStack)) {
			int result = itemStacks[1].stackSize + itemStack.stackSize;
			if (result <= this.getInventoryStackLimit() && result <= itemStacks[1].getMaxStackSize()) {
				// 完成品スロットに追加可能なら製錬可能。
				return true;
			}
		}
		// 製錬レシピに登録されているが、完成品スロットが埋まっているなら製錬不可。
		return false;
	}

	/** アイテムを製錬して移動する。 */
	protected void smeltItem() {
		// 完成品を取得。
		ItemStack itemStack = FurnaceRecipes.smelting().getSmeltingResult(itemStacks[0]);
		if (itemStacks[1] == null) {
			// 完成品スロットが空なら代入。
			itemStacks[1] = itemStack.copy();
		} else if (itemStacks[1].getItem() == itemStack.getItem()) {
			// 完成品スロットが空でないならスタック数を追加。
			itemStacks[1].stackSize += itemStack.stackSize;
		}
		// 製錬前スロットのスタック数を減らす。
		--itemStacks[0].stackSize;
		if (itemStacks[0].stackSize < 1) {
			// スタック数が0になったら空にする。
			itemStacks[0] = null;
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setShort("CookTime", cookTime);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < itemStacks.length; ++i) {
			if (itemStacks[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				itemStacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		nbt.setTag("Items", nbttaglist);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		cookTime = nbt.getShort("CookTime");
		NBTTagList nbttaglist = nbt.getTagList("Items", 10);
		itemStacks = new ItemStack[this.getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;
			if (j >= 0 && j < itemStacks.length) {
				itemStacks[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public int getHoldingEEScaled(int par1) {
		return holdingEE * par1 / capacity;
	}

	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int par1) {
		return cookTime * par1 / this.getMaxCookTime();
	}

	public int getHoldingEE() {
		return holdingEE;
	}

	public void setHoldingEE(int holdingEE) {
		this.holdingEE = holdingEE;
	}

	@Override
	public int getSizeInventory() {
		return 2;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return itemStacks[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (itemStacks[slot] != null) {
			ItemStack itemstack;
			if (itemStacks[slot].stackSize <= amount) {
				itemstack = itemStacks[slot];
				itemStacks[slot] = null;
				return itemstack;
			} else {
				itemstack = itemStacks[slot].splitStack(amount);
				if (itemStacks[slot].stackSize == 0) {
					itemStacks[slot] = null;
				}
				return itemstack;
			}
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (itemStacks[slot] != null) {
			ItemStack itemstack = itemStacks[slot];
			itemStacks[slot] = null;
			return itemstack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		itemStacks[slot] = itemStack;
		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
			itemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName() {
		return "container.EEFurnace";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false : player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[] { 0, 1 };
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
		return slot == 0;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
		return slot == 1;
	}

}
