package nahamawiki.oef.tileentity;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.OEFCore;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

public class TileEntityEECraftingTable extends TileEntityEEMachineBase implements ISidedInventory {

	/** 最後にGUIを開いたプレイヤー。 */
	protected EntityPlayer owner;
	/** ownerの名前。 */
	protected String ownerName;
	/** 材料スロット。 */
	protected ItemStack[] materials = new ItemStack[9];
	/** 完成品スロット。 */
	protected ItemStack[] results = new ItemStack[9];
	// ここから下はGUIからのみ操作可能。
	/** 見本レシピの材料。 */
	protected ItemStack[] sampleMaterials = new ItemStack[9];
	/** 見本レシピの完成品。 */
	protected ItemStack sampleResult = null;
	/** レシピシートのスロット。 */
	protected ItemStack sheet = null;

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
		// EEが足りないなら終了。
		if (holdingEE < this.getEECost())
			return;
		// クラフトできないなら終了。
		if (!this.canCraft())
			return;
		// クラフトして、EEを消費する。
		this.craftItem();
		holdingEE -= this.getEECost();
	}

	@Override
	public void updateCreepered() {
		// ランダムで爆発を起こす。
		Random random = new Random();
		if (random.nextInt(1200) == 0) {
			worldObj.createExplosion(null, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, random.nextFloat() * 10, true);
		}
	}

	/** 1回のクラフトで消費するEEの量を返す。 */
	protected int getEECost() {
		switch (level) {
		case 0:
			return 200;
		case 1:
			return 100;
		case 2:
			return 50;
		case 3:
			return 25;
		}
		return 0;
	}

	/** クラフトできるかどうか。 */
	protected boolean canCraft() {
		// 見本レシピができていないなら不可。
		if (sampleResult == null)
			return false;
		// materialsをコピー。
		ItemStack[] itemStacks = new ItemStack[materials.length];
		for (int i = 0; i < materials.length; i++) {
			if (materials[i] != null)
				itemStacks[i] = materials[i].copy();
		}
		for (int i = 0; i < 9; i++) {
			// 見本レシピで材料がないスロットなら次へ。
			if (sampleMaterials[i] == null)
				continue;
			boolean flag = false;
			for (int j = 0; j < 9; j++) {
				// 空か、材料と一致していないスロットなら次へ。
				if (itemStacks[j] == null || !itemStacks[j].isItemEqual(sampleMaterials[i]))
					continue;
				flag = true;
				// レシピに同じアイテムが複数回使われていた時の為に、カウントを減らす。
				itemStacks[j].stackSize--;
				if (itemStacks[j].stackSize < 1)
					itemStacks[j] = null;
				break;
			}
			// 材料が足りないなら不可。
			if (!flag)
				return false;
		}
		int amount = sampleResult.stackSize;
		for (int i = 0; i < 9; i++) {
			// 完成品スロットに空きがあるなら可能。
			if (results[i] == null) {
				return true;
			}
			if (!results[i].isItemEqual(sampleResult) || sampleResult.hasTagCompound() != results[i].hasTagCompound())
				continue;
			if (sampleResult.hasTagCompound() && sampleResult.getTagCompound().equals(results[i].getTagCompound()))
				continue;
			// 完成品スロットのアイテムが一致したらどれだけ入るかを記録。
			int limit = Math.min(this.getInventoryStackLimit(), results[i].getMaxStackSize());
			if (results[i].stackSize < limit)
				amount -= (limit - results[i].stackSize);
		}
		// すべて入れられるなら可能。
		if (amount < 1)
			return true;
		// 完成品スロットに空きがないなら不可。
		return false;
	}

	/** クラフトをしてアイテムを移動する処理。 */
	protected void craftItem() {
		// 見本レシピの完成品をコピー。
		ItemStack result = sampleResult.copy();
		// アイテムの作成時の処理を行う。
		if (this.getOwner() != null)
			result.onCrafting(worldObj, owner, result.stackSize);
		boolean flag = false;
		// 材料を消費する。
		for (int i = 0; i < 9; i++) {
			if (sampleMaterials[i] == null)
				continue;
			for (int j = 0; j < 9; j++) {
				if (materials[j] == null)
					continue;
				if (!materials[j].isItemEqual(sampleMaterials[i]))
					continue;
				materials[j].stackSize--;
				if (materials[j].stackSize < 1)
					materials[j] = null;
				flag = true;
				break;
			}
			if (!flag) {
				// 材料が足りなかったならエラーを出力。
				OEFCore.logger.error("Crafting materials is lacking!");
			} else {
				flag = false;
			}
		}
		// クラフト結果を完成品スロットに追加する。
		for (int i = 0; i < 9; i++) {
			if (results[i] == null) {
				// 空のスロットがあればそこに代入する。
				results[i] = result;
				flag = true;
				break;
			}
			if (!results[i].isItemEqual(result) || result.hasTagCompound() != results[i].hasTagCompound())
				continue;
			if (result.hasTagCompound() && result.getTagCompound().equals(results[i].getTagCompound()))
				continue;
			// アイテムが一致したならスタック数を増やす。
			int limit = Math.min(this.getInventoryStackLimit(), results[i].getMaxStackSize());
			if (results[i].stackSize >= limit)
				continue;
			int size = result.stackSize;
			result.stackSize -= (limit - results[i].stackSize);
			if (result.stackSize < 0)
				result.stackSize = 0;
			results[i].stackSize += (size - result.stackSize);
			if (result.stackSize < 1) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			// クラフト結果が入りきらなかったらドロップしてエラーを出力。
			this.spawnItemStackAsEntity(result);
			OEFCore.logger.error("Crafting result is overflowing!");
		} else {
			flag = false;
		}
		// 水バケツなどの場合、アイテムを返還する。
		for (int i = 0; i < 9; ++i) {
			// 返還する必要がなければ次へ。
			if (sampleMaterials[i] == null || !sampleMaterials[i].getItem().hasContainerItem(sampleMaterials[i]))
				continue;
			ItemStack itemStack = sampleMaterials[i].getItem().getContainerItem(sampleMaterials[i]);
			if (itemStack != null && itemStack.isItemStackDamageable() && itemStack.getItemDamage() > itemStack.getMaxDamage()) {
				if (owner != null)
					MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(owner, itemStack));
				continue;
			}
			if (sampleMaterials[i].getItem().doesContainerItemLeaveCraftingGrid(sampleMaterials[i])) {
				// 返還するアイテムがクラフト枠に残らないなら、完成品スロットに追加。
				for (int j = 0; j < 9; j++) {
					if (results[j] == null) {
						results[j] = itemStack;
						flag = true;
						break;
					}
					if (!results[j].isItemEqual(itemStack))
						continue;
					int limit = Math.min(this.getInventoryStackLimit(), results[j].getMaxStackSize());
					if (results[j].stackSize >= limit)
						continue;
					itemStack.stackSize -= (limit - results[j].stackSize);
					results[j].stackSize += itemStack.stackSize;
					if (itemStack.stackSize < 1) {
						flag = true;
						break;
					}
				}
				if (!flag) {
					this.spawnItemStackAsEntity(itemStack);
					OEFCore.logger.error("Container Item is overflowing!");
				} else {
					flag = false;
				}
			} else {
				// 返還するアイテムがクラフト枠に残るなら、材料スロットに追加。
				for (int j = 0; j < 9; j++) {
					if (materials[j] == null) {
						materials[j] = itemStack;
						flag = true;
						break;
					}
					if (!materials[j].isItemEqual(itemStack))
						continue;
					int limit = Math.min(this.getInventoryStackLimit(), materials[j].getMaxStackSize());
					if (materials[j].stackSize >= limit)
						continue;
					itemStack.stackSize -= (limit - materials[j].stackSize);
					materials[j].stackSize += itemStack.stackSize;
					if (itemStack.stackSize < 1) {
						flag = true;
						break;
					}
				}
				if (!flag) {
					this.spawnItemStackAsEntity(itemStack);
					OEFCore.logger.error("Container Item is overflowing!");
				} else {
					flag = false;
				}
			}
		}
		this.markDirty();
	}

	/** このブロックの真上にアイテムをドロップする処理。 */
	public void spawnItemStackAsEntity(ItemStack itemStack) {
		EntityItem entity = new EntityItem(worldObj, xCoord + 0.5, yCoord + 1, zCoord + 0.5, itemStack);
		if (itemStack.hasTagCompound()) {
			entity.getEntityItem().setTagCompound(((NBTTagCompound) itemStack.getTagCompound().copy()));
		}
		worldObj.spawnEntityInWorld(entity);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < materials.length; ++i) {
			if (materials[i] != null) {
				NBTTagCompound nbt1 = new NBTTagCompound();
				nbt1.setByte("Slot", (byte) i);
				materials[i].writeToNBT(nbt1);
				nbttaglist.appendTag(nbt1);
			}
		}
		nbt.setTag("Materials", nbttaglist);

		nbttaglist = new NBTTagList();
		for (int i = 0; i < results.length; ++i) {
			if (results[i] != null) {
				NBTTagCompound nbt1 = new NBTTagCompound();
				nbt1.setByte("Slot", (byte) i);
				results[i].writeToNBT(nbt1);
				nbttaglist.appendTag(nbt1);
			}
		}
		nbt.setTag("Results", nbttaglist);

		nbttaglist = new NBTTagList();
		for (int i = 0; i < sampleMaterials.length; ++i) {
			if (sampleMaterials[i] != null) {
				NBTTagCompound nbt1 = new NBTTagCompound();
				nbt1.setByte("Slot", (byte) i);
				sampleMaterials[i].writeToNBT(nbt1);
				nbttaglist.appendTag(nbt1);
			}
		}
		nbt.setTag("SampleMaterials", nbttaglist);

		if (sampleResult != null) {
			NBTTagCompound nbt1 = new NBTTagCompound();
			sampleResult.writeToNBT(nbt1);
			nbt.setTag("SampleResult", nbt1);
		}

		if (sheet != null) {
			NBTTagCompound nbt1 = new NBTTagCompound();
			sheet.writeToNBT(nbt1);
			nbt.setTag("Sample", nbt1);
		}

		if ((ownerName == null || ownerName.length() == 0) && owner != null) {
			ownerName = owner.getCommandSenderName();
		}
		nbt.setString("OwnerName", ownerName == null ? "" : ownerName);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		NBTTagList nbttaglist = nbt.getTagList("Materials", 10);
		materials = new ItemStack[9];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbt1 = nbttaglist.getCompoundTagAt(i);
			int j = nbt1.getByte("Slot") & 255;
			if (j >= 0 && j < materials.length) {
				materials[j] = ItemStack.loadItemStackFromNBT(nbt1);
			}
		}

		nbttaglist = nbt.getTagList("Results", 10);
		results = new ItemStack[9];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbt1 = nbttaglist.getCompoundTagAt(i);
			int j = nbt1.getByte("Slot") & 255;
			if (j >= 0 && j < results.length) {
				results[j] = ItemStack.loadItemStackFromNBT(nbt1);
			}
		}

		nbttaglist = nbt.getTagList("SampleMaterials", 10);
		sampleMaterials = new ItemStack[9];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbt1 = nbttaglist.getCompoundTagAt(i);
			int j = nbt1.getByte("Slot") & 255;
			if (j >= 0 && j < sampleMaterials.length) {
				sampleMaterials[j] = ItemStack.loadItemStackFromNBT(nbt1);
			}
		}

		if (nbt.hasKey("SampleResult")) {
			NBTTagCompound nbt1 = (NBTTagCompound) nbt.getTag("SampleResult");
			sampleResult = ItemStack.loadItemStackFromNBT(nbt1);
		}

		if (nbt.hasKey("Sample")) {
			NBTTagCompound nbt1 = (NBTTagCompound) nbt.getTag("Sample");
			sheet = ItemStack.loadItemStackFromNBT(nbt1);
		}

		ownerName = nbt.getString("OwnerName");
		if (ownerName != null && ownerName.length() == 0) {
			ownerName = null;
		}
	}

	/** 最後にGUIを開いたプレイヤーを返す。 */
	public EntityPlayer getOwner() {
		if (owner == null && ownerName != null && ownerName.length() > 0) {
			owner = worldObj.getPlayerEntityByName(ownerName);
		}
		return owner;
	}

	public void setOwner(EntityPlayer player) {
		owner = player;
	}

	@SideOnly(Side.CLIENT)
	public int getHoldingEEScaled(int par1) {
		return holdingEE * par1 / capacity;
	}

	public int getHoldingEE() {
		return holdingEE;
	}

	public void setHoldingEE(int holdingEE) {
		this.holdingEE = holdingEE;
	}

	public void writeToRecipeSheet(NBTTagCompound nbt) {
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < sampleMaterials.length; ++i) {
			if (sampleMaterials[i] != null) {
				NBTTagCompound nbt1 = new NBTTagCompound();
				nbt1.setByte("Slot", (byte) i);
				sampleMaterials[i].writeToNBT(nbt1);
				nbttaglist.appendTag(nbt1);
			}
		}
		nbt.setTag("SampleMaterials", nbttaglist);

		if (sampleResult != null) {
			NBTTagCompound nbt1 = new NBTTagCompound();
			sampleResult.writeToNBT(nbt1);
			nbt.setTag("SampleResult", nbt1);
		}
	}

	public void readFromRecipeSheet(NBTTagCompound nbt) {
		NBTTagList nbttaglist = nbt.getTagList("SampleMaterials", 10);
		sampleMaterials = new ItemStack[9];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbt1 = nbttaglist.getCompoundTagAt(i);
			int j = nbt1.getByte("Slot") & 255;
			if (j >= 0 && j < sampleMaterials.length) {
				sampleMaterials[j] = ItemStack.loadItemStackFromNBT(nbt1);
			}
		}

		if (nbt.hasKey("SampleResult")) {
			NBTTagCompound nbt1 = (NBTTagCompound) nbt.getTag("SampleResult");
			sampleResult = ItemStack.loadItemStackFromNBT(nbt1);
		}
	}

	@Override
	public int getSizeInventory() {
		return 29;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot < 9) {
			return materials[slot];
		} else if (slot < 18) {
			return results[slot - 9];
		} else if (slot < 27) {
			return sampleMaterials[slot - 18];
		} else if (slot == 27) {
			return sampleResult;
		} else if (slot == 28) {
			return sheet;
		}
		return null;
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (slot < 9) {
			if (materials[slot] != null) {
				ItemStack itemStack;
				if (materials[slot].stackSize <= amount) {
					itemStack = materials[slot];
					materials[slot] = null;
					return itemStack;
				} else {
					itemStack = materials[slot].splitStack(amount);
					if (materials[slot].stackSize == 0) {
						materials[slot] = null;
					}
					return itemStack;
				}
			}
		} else if (slot < 18) {
			int slot1 = slot - 9;
			if (results[slot1] != null) {
				ItemStack itemStack;
				if (results[slot1].stackSize <= amount) {
					itemStack = results[slot1];
					results[slot1] = null;
					return itemStack;
				} else {
					itemStack = results[slot1].splitStack(amount);
					if (results[slot1].stackSize == 0) {
						results[slot1] = null;
					}
					return itemStack;
				}
			}
		} else if (slot == 28) {
			ItemStack itemStack;
			if (sheet != null) {
				if (sheet.stackSize <= amount) {
					itemStack = sheet;
					sheet = null;
					return itemStack;
				} else {
					itemStack = sheet.splitStack(amount);
					if (sheet.stackSize == 0) {
						sheet = null;
					}
					return itemStack;
				}
			}
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (slot < 9) {
			if (materials[slot] != null) {
				ItemStack itemstack = materials[slot];
				materials[slot] = null;
				return itemstack;
			}
		} else if (slot < 18) {
			int slot1 = slot - 9;
			if (results[slot1] != null) {
				ItemStack itemstack = results[slot1];
				results[slot1] = null;
				return itemstack;
			}
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		if (slot < 9) {
			materials[slot] = itemStack;
			if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
				itemStack.stackSize = this.getInventoryStackLimit();
			}
		} else if (slot < 18) {
			int slot1 = slot - 9;
			results[slot1] = itemStack;
			if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
				itemStack.stackSize = this.getInventoryStackLimit();
			}
		} else if (slot < 27) {
			int slot1 = slot - 18;
			sampleMaterials[slot1] = itemStack;
			if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
				itemStack.stackSize = this.getInventoryStackLimit();
			}
		} else if (slot == 27) {
			sampleResult = itemStack;
			if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
				itemStack.stackSize = this.getInventoryStackLimit();
			}
		} else if (slot == 28) {
			sheet = itemStack;
			if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
				itemStack.stackSize = this.getInventoryStackLimit();
			}
		}
	}

	@Override
	public String getInventoryName() {
		return "container.EECraftingTable";
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
		return slot < 18;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		int[] array = new int[18];
		for (int i = 0; i < 18; i++) {
			array[i] = i;
		}
		return array;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
		return slot < 9;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
		return slot > 8;
	}

}
