package nahamawiki;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockHopper;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TileEntityHopper extends TileEntity implements IHopper {

	private ItemStack[] itemStacks = new ItemStack[5];
	private String customName;
	private int coolTime = -1;

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		NBTTagList nbttaglist = nbt.getTagList("Items", 10);
		this.itemStacks = new ItemStack[this.getSizeInventory()];

		if (nbt.hasKey("CustomName", 8)) {
			this.customName = nbt.getString("CustomName");
		}

		this.coolTime = nbt.getInteger("TransferCooldown");

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if (b0 >= 0 && b0 < this.itemStacks.length) {
				this.itemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.itemStacks.length; ++i) {
			if (this.itemStacks[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.itemStacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbt.setTag("Items", nbttaglist);
		nbt.setInteger("TransferCooldown", this.coolTime);

		if (this.hasCustomInventoryName()) {
			nbt.setString("CustomName", this.customName);
		}
	}

	/**
	 * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
	 * hasn't changed and skip it.
	 */
	@Override
	public void markDirty() {
		super.markDirty();
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory() {
		return this.itemStacks.length;
	}

	/**
	 * Returns the stack in slot i
	 */
	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.itemStacks[slot];
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
	 * new stack.
	 */
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (this.itemStacks[slot] != null) {
			ItemStack itemstack;

			if (this.itemStacks[slot].stackSize <= amount) {
				itemstack = this.itemStacks[slot];
				this.itemStacks[slot] = null;
				return itemstack;
			} else {
				itemstack = this.itemStacks[slot].splitStack(amount);

				if (this.itemStacks[slot].stackSize == 0) {
					this.itemStacks[slot] = null;
				}

				return itemstack;
			}
		} else {
			return null;
		}
	}

	/**
	 * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
	 * like when you close a workbench GUI.
	 */
	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (this.itemStacks[slot] != null) {
			ItemStack itemstack = this.itemStacks[slot];
			this.itemStacks[slot] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		this.itemStacks[slot] = itemStack;

		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
			itemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	/**
	 * Returns the name of the inventory
	 */
	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.customName : "container.hopper";
	}

	/**
	 * Returns if the inventory is named
	 */
	@Override
	public boolean hasCustomInventoryName() {
		return this.customName != null && this.customName.length() > 0;
	}

	public void func_145886_a(String name) {
		this.customName = name;
	}

	/**
	 * Returns the maximum stack size for a inventory slot.
	 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes with Container
	 */
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
	 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return true;
	}

	@Override
	public void updateEntity() {
		if (this.worldObj != null && !this.worldObj.isRemote) {
			--this.coolTime;

			if (this.coolTime < 1) {
				this.coolTime = 0;
				boolean flag = false;

				if (!this.isEmpty()) {
					flag = this.isCompleteExportItem();
				}

				if (!this.canImportItems()) {
					flag = isImportedItem(this) || flag;
				}

				if (flag) {
					this.setCoolTime(8);
					this.markDirty();
				}
			}
		}
	}

	private boolean isEmpty() {
		ItemStack[] aitemstack = this.itemStacks;
		int i = aitemstack.length;

		for (int j = 0; j < i; ++j) {
			ItemStack itemstack = aitemstack[j];

			if (itemstack != null) {
				return false;
			}
		}

		return true;
	}

	private boolean canImportItems() {
		ItemStack[] aitemstack = this.itemStacks;
		int i = aitemstack.length;

		for (int j = 0; j < i; ++j) {
			ItemStack itemstack = aitemstack[j];

			if (itemstack == null || itemstack.stackSize != itemstack.getMaxStackSize()) {
				return false;
			}
		}

		return true;
	}

	private boolean isCompleteExportItem() {
		IInventory iinventory = this.getDestinationInvenotry();

		if (iinventory == null) {
			return false;
		} else {
			int i = Facing.oppositeSide[BlockHopper.getDirectionFromMetadata(this.getBlockMetadata())];

			if (this.isFullInventoryFromSide(iinventory, i)) {
				return false;
			} else {
				for (int j = 0; j < this.getSizeInventory(); ++j) {
					if (this.getStackInSlot(j) != null) {
						ItemStack itemstack = this.getStackInSlot(j).copy();
						ItemStack itemstack1 = tryInsertItem(iinventory, this.decrStackSize(j, 1), i);

						if (itemstack1 == null || itemstack1.stackSize == 0) {
							iinventory.markDirty();
							return true;
						}

						this.setInventorySlotContents(j, itemstack);
					}
				}

				return false;
			}
		}
	}

	private boolean isFullInventoryFromSide(IInventory iinventory, int side) {
		if (iinventory instanceof ISidedInventory && side > -1) {
			ISidedInventory isidedinventory = (ISidedInventory) iinventory;
			int[] aint = isidedinventory.getAccessibleSlotsFromSide(side);

			for (int l = 0; l < aint.length; ++l) {
				ItemStack itemstack1 = isidedinventory.getStackInSlot(aint[l]);

				if (itemstack1 == null || itemstack1.stackSize != itemstack1.getMaxStackSize()) {
					return false;
				}
			}
		} else {
			int j = iinventory.getSizeInventory();

			for (int k = 0; k < j; ++k) {
				ItemStack itemstack = iinventory.getStackInSlot(k);

				if (itemstack == null || itemstack.stackSize != itemstack.getMaxStackSize()) {
					return false;
				}
			}
		}

		return true;
	}

	private static boolean isEmptyInventoryFromSlot(IInventory iinventory, int side) {
		if (iinventory instanceof ISidedInventory && side > -1) {
			ISidedInventory isidedinventory = (ISidedInventory) iinventory;
			int[] aint = isidedinventory.getAccessibleSlotsFromSide(side);

			for (int l = 0; l < aint.length; ++l) {
				if (isidedinventory.getStackInSlot(aint[l]) != null) {
					return false;
				}
			}
		} else {
			int j = iinventory.getSizeInventory();

			for (int k = 0; k < j; ++k) {
				if (iinventory.getStackInSlot(k) != null) {
					return false;
				}
			}
		}

		return true;
	}

	public static boolean isImportedItem(IHopper hopper) {
		IInventory iinventory = getAdoveInvenotry(hopper);

		if (iinventory != null) {
			byte b0 = 0;

			if (isEmptyInventoryFromSlot(iinventory, b0)) {
				return false;
			}

			if (iinventory instanceof ISidedInventory && b0 > -1) {
				ISidedInventory isidedinventory = (ISidedInventory) iinventory;
				int[] aint = isidedinventory.getAccessibleSlotsFromSide(b0);

				for (int k = 0; k < aint.length; ++k) {
					if (isCompleteImportItem(hopper, iinventory, aint[k], b0)) {
						return true;
					}
				}
			} else {
				int i = iinventory.getSizeInventory();

				for (int j = 0; j < i; ++j) {
					if (isCompleteImportItem(hopper, iinventory, j, b0)) {
						return true;
					}
				}
			}
		} else {
			EntityItem entityitem = func_145897_a(hopper.getWorldObj(), hopper.getXPos(), hopper.getYPos() + 1.0D, hopper.getZPos());

			if (entityitem != null) {
				return func_145898_a(hopper, entityitem);
			}
		}

		return false;
	}

	private static boolean isCompleteImportItem(IHopper hopper, IInventory iinventory, int slot, int side) {
		ItemStack itemstack = iinventory.getStackInSlot(slot);

		if (itemstack != null && canExtractItem(iinventory, itemstack, slot, side)) {
			ItemStack itemstack1 = itemstack.copy();
			ItemStack itemstack2 = tryInsertItem(hopper, iinventory.decrStackSize(slot, 1), -1);

			if (itemstack2 == null || itemstack2.stackSize == 0) {
				iinventory.markDirty();
				return true;
			}

			iinventory.setInventorySlotContents(slot, itemstack1);
		}

		return false;
	}

	public static boolean func_145898_a(IInventory p_145898_0_, EntityItem p_145898_1_) {
		boolean flag = false;

		if (p_145898_1_ == null) {
			return false;
		} else {
			ItemStack itemstack = p_145898_1_.getEntityItem().copy();
			ItemStack itemstack1 = tryInsertItem(p_145898_0_, itemstack, -1);

			if (itemstack1 != null && itemstack1.stackSize != 0) {
				p_145898_1_.setEntityItemStack(itemstack1);
			} else {
				flag = true;
				p_145898_1_.setDead();
			}

			return flag;
		}
	}

	public static ItemStack tryInsertItem(IInventory iinventory, ItemStack itemStack, int side) {
		if (iinventory instanceof ISidedInventory && side > -1) {
			ISidedInventory isidedinventory = (ISidedInventory) iinventory;
			int[] aint = isidedinventory.getAccessibleSlotsFromSide(side);

			for (int l = 0; l < aint.length && itemStack != null && itemStack.stackSize > 0; ++l) {
				itemStack = insertItem(iinventory, itemStack, aint[l], side);
			}
		} else {
			int j = iinventory.getSizeInventory();

			for (int k = 0; k < j && itemStack != null && itemStack.stackSize > 0; ++k) {
				itemStack = insertItem(iinventory, itemStack, k, side);
			}
		}

		if (itemStack != null && itemStack.stackSize == 0) {
			itemStack = null;
		}

		return itemStack;
	}

	private static boolean canInsertItem(IInventory iinventory, ItemStack itemStack, int slot, int side) {
		return !iinventory.isItemValidForSlot(slot, itemStack) ? false : !(iinventory instanceof ISidedInventory) || ((ISidedInventory) iinventory).canInsertItem(slot, itemStack, side);
	}

	private static boolean canExtractItem(IInventory iinventory, ItemStack itemStack, int slot, int meta) {
		return !(iinventory instanceof ISidedInventory) || ((ISidedInventory) iinventory).canExtractItem(slot, itemStack, meta);
	}

	private static ItemStack insertItem(IInventory iinventory, ItemStack itemStack, int slot, int side) {
		ItemStack itemstack1 = iinventory.getStackInSlot(slot);

		if (canInsertItem(iinventory, itemStack, slot, side)) {
			boolean flag = false;

			if (itemstack1 == null) {
				// Forge: BUGFIX: Again, make things respect max stack sizes.
				int max = Math.min(itemStack.getMaxStackSize(), iinventory.getInventoryStackLimit());
				if (max >= itemStack.stackSize) {
					iinventory.setInventorySlotContents(slot, itemStack);
					itemStack = null;
				} else {
					iinventory.setInventorySlotContents(slot, itemStack.splitStack(max));
				}
				flag = true;
			} else if (canStack(itemstack1, itemStack)) {
				// Forge: BUGFIX: Again, make things respect max stack sizes.
				int max = Math.min(itemStack.getMaxStackSize(), iinventory.getInventoryStackLimit());
				if (max > itemstack1.stackSize) {
					int l = Math.min(itemStack.stackSize, max - itemstack1.stackSize);
					itemStack.stackSize -= l;
					itemstack1.stackSize += l;
					flag = l > 0;
				}
			}

			if (flag) {
				if (iinventory instanceof TileEntityHopper) {
					((TileEntityHopper) iinventory).setCoolTime(8);
					iinventory.markDirty();
				}

				iinventory.markDirty();
			}
		}

		return itemStack;
	}

	private IInventory getDestinationInvenotry() {
		int i = BlockHopper.getDirectionFromMetadata(this.getBlockMetadata());
		return getIInventory(this.getWorldObj(), this.xCoord + Facing.offsetsXForSide[i], this.yCoord + Facing.offsetsYForSide[i], this.zCoord + Facing.offsetsZForSide[i]);
	}

	public static IInventory getAdoveInvenotry(IHopper hopper) {
		return getIInventory(hopper.getWorldObj(), hopper.getXPos(), hopper.getYPos() + 1.0D, hopper.getZPos());
	}

	public static EntityItem func_145897_a(World p_145897_0_, double p_145897_1_, double p_145897_3_, double p_145897_5_) {
		List list = p_145897_0_.selectEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(p_145897_1_, p_145897_3_, p_145897_5_, p_145897_1_ + 1.0D, p_145897_3_ + 1.0D, p_145897_5_ + 1.0D), IEntitySelector.selectAnything);
		return list.size() > 0 ? (EntityItem) list.get(0) : null;
	}

	public static IInventory getIInventory(World world, double x, double y, double z) {
		IInventory iinventory = null;
		int i = MathHelper.floor_double(x);
		int j = MathHelper.floor_double(y);
		int k = MathHelper.floor_double(z);
		TileEntity tileentity = world.getTileEntity(i, j, k);

		if (tileentity != null && tileentity instanceof IInventory) {
			iinventory = (IInventory) tileentity;

			if (iinventory instanceof TileEntityChest) {
				Block block = world.getBlock(i, j, k);
				// チェストならラージチェストのインベントリを取得。
				if (block instanceof BlockChest) {
					iinventory = ((BlockChest) block).func_149951_m(world, i, j, k);
				}
			}
		}

		// 搬出先にインベントリ持ちのTileEntityがないなら、インベントリもちトロッコがあるか確認。
		if (iinventory == null) {
			List list = world.getEntitiesWithinAABBExcludingEntity((Entity) null, AxisAlignedBB.getBoundingBox(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D), IEntitySelector.selectInventories);

			if (list != null && list.size() > 0) {
				iinventory = (IInventory) list.get(world.rand.nextInt(list.size()));
			}
		}

		return iinventory;
	}

	private static boolean canStack(ItemStack stackInv, ItemStack stackThis) {
		return stackInv.getItem() != stackThis.getItem() ? false : (stackInv.getItemDamage() != stackThis.getItemDamage() ? false : (stackInv.stackSize > stackInv.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(stackInv, stackThis)));
	}

	/**
	 * Gets the world X position for this hopper entity.
	 */
	@Override
	public double getXPos() {
		return this.xCoord;
	}

	/**
	 * Gets the world Y position for this hopper entity.
	 */
	@Override
	public double getYPos() {
		return this.yCoord;
	}

	/**
	 * Gets the world Z position for this hopper entity.
	 */
	@Override
	public double getZPos() {
		return this.zCoord;
	}

	public void setCoolTime(int coolTime) {
		this.coolTime = coolTime;
	}

	public boolean lackTime() {
		return this.coolTime > 0;
	}
}