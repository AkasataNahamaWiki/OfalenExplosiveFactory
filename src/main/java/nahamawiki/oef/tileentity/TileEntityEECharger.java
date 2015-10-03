package nahamawiki.oef.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.OEFCore;
import nahamawiki.oef.util.EEUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

public class TileEntityEECharger extends TileEntityEEMachineBase implements ISidedInventory {

	public int holdingEE;
	public int coolTime;
	protected int capacity;
	protected ItemStack battery;

	@Override
	public int getMachineType(int side) {
		return 2;
	}

	@Override
	public int recieveEE(int amount, int side) {
		if (capacity == 0)
			setCapacity();
		holdingEE += amount;
		if (holdingEE > capacity) {
			int surplus = holdingEE - capacity;
			holdingEE = capacity;
			return surplus;
		}
		return 0;
	}

	@Override
	public String[] getState() {
		return new String[] {
				StatCollector.translateToLocal("info.EEMachineState.name") + StatCollector.translateToLocal(this.getBlockType().getLocalizedName()),
				StatCollector.translateToLocal("info.EEMachineState.level") + this.getLevel(this.getBlockMetadata()),
				StatCollector.translateToLocal("info.EEMachineState.capacity") + capacity + " EE",
				StatCollector.translateToLocal("info.EEMachineState.holding") + holdingEE + " EE"
		};
	}

	@Override
	public int getLevel(int meta) {
		return meta & 3;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("holdingEE", holdingEE);
		nbt.setInteger("capacity", capacity);
		nbt.setInteger("coolTime", coolTime);
		if (battery != null)
			nbt.setTag("battery", battery.writeToNBT(new NBTTagCompound()));
		OEFCore.logger.info("Complete writeToNBT");
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		holdingEE = nbt.getInteger("holdingEE");
		capacity = nbt.getInteger("capacity");
		coolTime = nbt.getInteger("coolTime");
		if (nbt.getTag("battery") != null && nbt.getTag("battery") instanceof NBTTagCompound)
			battery = ItemStack.loadItemStackFromNBT((NBTTagCompound) nbt.getTag("battery"));
	}

	protected int setCapacity() {
		return capacity = EEUtil.getBaseCapacity(this.getLevel(worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord)));
	}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote)
			return;
		if (coolTime > 0)
			coolTime--;
		if (battery == null || holdingEE <= 0 || coolTime > 0 || battery.getTagCompound().getInteger("holdingEE") > EEUtil.getBaseCapacity(battery.getItemDamage()))
			return;
		battery.getTagCompound().setInteger("holdingEE", battery.getTagCompound().getInteger("holdingEE") + 1);
		holdingEE--;
		switch (this.getLevel(this.getBlockMetadata())) {
		case 0:
			coolTime = 16;
			break;
		case 1:
			coolTime = 8;
			break;
		case 2:
			coolTime = 4;
			break;
		case 3:
			coolTime = 2;
		}
		markDirty();
	}

	public boolean isCharging() {
		return coolTime > 0;
	}

	@SideOnly(Side.CLIENT)
	public int getHoldingEEScaled(int par1) {
		if (capacity < 1)
			setCapacity();
		return holdingEE * par1 / capacity;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot == 0 && battery != null)
			return battery;
		return null;
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (slot == 0 && battery != null) {
			ItemStack itemStack;
			if (battery.stackSize <= amount) {
				itemStack = battery;
				battery = null;
				return itemStack;
			}
			itemStack = battery.splitStack(amount);
			if (battery.stackSize == 0) {
				battery = null;
			}
			return itemStack;
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (slot == 0 && battery != null) {
			ItemStack itemStack = battery;
			battery = null;
			return itemStack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		if (slot == 0) {
			battery = itemStack;
			if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
				itemStack.stackSize = this.getInventoryStackLimit();
			}
		}
	}

	@Override
	public String getInventoryName() {
		return "container.EECharger";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		if (slot == 0)
			return true;
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[] { 0 };
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
		if (slot == 0 && battery == null && itemStack.stackSize == 1) {
			if (itemStack.hasTagCompound() && itemStack.getTagCompound().getBoolean("canChargeEE")) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
		if (slot == 0 && battery != null) {
			if (!battery.hasTagCompound() || !battery.getTagCompound().getBoolean("canChargeEE"))
				return true;
			if (battery.getTagCompound().getInteger("holdingEE") == EEUtil.getBaseCapacity(battery.getItemDamage()))
				return true;
		}
		return false;
	}

}
