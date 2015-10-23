package nahamawiki.oef.tileentity;

import static net.minecraft.util.Facing.*;

import java.util.ArrayList;

import nahamawiki.oef.util.EEUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.world.World;

public class TileEntityEEItemTransporter extends TileEntityEEConductor implements ISidedInventory {

	protected byte[] nextSide = new byte[6];
	protected int[] coolTime = new int[6];
	protected ArrayList<Integer> recieverI = new ArrayList<Integer>();
	protected ItemStack[] itemStacks = new ItemStack[6];

	public TileEntityEEItemTransporter() {
		super();
		capacity = 8256;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setByteArray("nextSide", nextSide);
		nbt.setIntArray("coolTime", coolTime);

		NBTTagCompound localnbt = new NBTTagCompound();
		for (int i = 0; i < recieverI.size(); i++) {
			localnbt.setInteger(String.valueOf(i), recieverI.get(i));
		}
		nbt.setByte("recieverISize", (byte) recieverI.size());
		nbt.setTag("recieverI", localnbt);

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
		nextSide = nbt.getByteArray("nextSide");
		coolTime = nbt.getIntArray("coolTime");

		recieverI.clear();
		NBTTagCompound localnbt = nbt.getCompoundTag("recieverI");
		for (int i = 0; i < nbt.getByte("recieverISize"); i++) {
			recieverI.add(localnbt.getInteger(String.valueOf(i)));
		}

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

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (worldObj.isRemote)
			return;
		this.sendItems();
	}

	protected void sendItems() {
		if (holdingEE < 4)
			return;
		boolean flag;
		for (int i = 0; i < 6; i++) {
			if (coolTime[i] > 0)
				coolTime[i]--;
			if (coolTime[i] > 0)
				continue;
			flag = false;
			ArrayList<Integer> list = EEUtil.copyList(recieverI);
			if (list.contains(i))
				list.remove(list.indexOf(i));
			while (itemStacks[i] != null && list.size() > 0 && holdingEE > 3) {
				for (int j = 0; j < 6; j++) {
					if (itemStacks[i] == null)
						break;
					if (!list.contains(j) || j < nextSide[i]) {
						if (j >= 5)
							nextSide[i] = 0;
						continue;
					}
					IInventory iinventory = this.getIInventory(j);
					if (iinventory == null || this.isFullInventoryFromSide(iinventory, oppositeSide[j])) {
						list.remove(list.indexOf(j));
						continue;
					}
					ItemStack itemStack = itemStacks[i].copy();
					ItemStack itemStack1 = TileEntityHopper.func_145889_a(iinventory, itemStacks[i].splitStack(1), oppositeSide[j]);
					if (itemStacks[i].stackSize < 1)
						itemStacks[i] = null;
					if (itemStack1 == null || itemStack1.stackSize == 0) {
						iinventory.markDirty();
						flag = true;
						nextSide[i] = (byte) (j + 1);
						if (nextSide[i] > 5)
							nextSide[i] = 0;
						holdingEE -= 4;
						if (holdingEE < 4)
							break;
						continue;
					}
					itemStacks[i] = itemStack;
					list.remove(list.indexOf(j));
					if (j >= 5)
						nextSide[i] = 0;
				}
			}
			if (flag) {
				switch (level) {
				case 0:
					coolTime[i] = 20;
					break;
				case 1:
					coolTime[i] = 10;
					break;
				case 2:
					coolTime[i] = 5;
					break;
				}
			}
		}
	}

	protected IInventory getIInventory(int side) {
		IInventory iinventory = null;
		TileEntity tileEntity = worldObj.getTileEntity(xCoord + offsetsXForSide[side], yCoord + offsetsYForSide[side], zCoord + offsetsZForSide[side]);
		if (tileEntity != null && tileEntity instanceof IInventory) {
			iinventory = (IInventory) tileEntity;
			if (iinventory instanceof TileEntityChest) {
				Block block = worldObj.getBlock(xCoord + offsetsXForSide[side], yCoord + offsetsYForSide[side], zCoord + offsetsZForSide[side]);
				// チェストならラージチェストのインベントリを取得。
				if (block instanceof BlockChest) {
					iinventory = ((BlockChest) block).func_149951_m(worldObj, xCoord + offsetsXForSide[side], yCoord + offsetsYForSide[side], zCoord + offsetsZForSide[side]);
				}
			}
		}
		return iinventory;
	}

	protected boolean isFullInventoryFromSide(IInventory iinventory, int side) {
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

	@Override
	protected void decreaseEE() {
		if (loss < 0) {
			switch (level) {
			case 0:
				loss = 8;
				break;
			case 1:
				loss = 4;
				break;
			case 2:
				loss = 2;
				break;
			case 3:
				loss = 0;
				break;
			}
		}
		if (loss > 0) {
			if (holdingEE >= 256 + loss)
				holdingEE -= loss;
		}
	}

	@Override
	protected void sendEE() {
		if (reciever.size() < 1)
			return;
		ArrayList<Integer> list = EEUtil.copyList(reciever);
		while (list.size() > 0 && (holdingEE - 256) / list.size() > 0) {
			int sendingEE = (holdingEE - 256) / list.size();
			holdingEE = (holdingEE - 256) % list.size();
			holdingEE += 256;
			for (int i = 0; i < 6; i++) {
				if (!list.contains(i))
					continue;
				ITileEntityEEMachine machine = this.getNeighborMachine(i);
				if (machine == null || machine.getTier(oppositeSide[i]) < tier) {
					list.remove(list.indexOf(i));
					holdingEE += sendingEE;
					continue;
				}
				int surplus = machine.recieveEE(sendingEE, oppositeSide[i]);
				if (surplus < 1)
					continue;
				holdingEE += surplus;
				list.remove(list.indexOf(i));
			}
		}
	}

	@Override
	public void updateDirection(World world, int x, int y, int z) {
		reciever.clear();
		recieverI.clear();
		for (int i = 0; i < 6; i++) {
			isConnecting[i] = false;
			ITileEntityEEMachine machine = this.getNeighborMachine(i);
			if (machine != null) {
				int type = machine.getMachineType(oppositeSide[i]);
				if ((type & 2) == 2) {
					reciever.add(i);
				}
				isConnecting[i] = (type != 0);
				if (tier > machine.getTier(oppositeSide[i]))
					this.setTier(machine.getTier(oppositeSide[i]) + 1, i);
			}
			TileEntity tileEntity = world.getTileEntity(x + offsetsXForSide[i], y + offsetsYForSide[i], z + offsetsZForSide[i]);
			if (tileEntity != null && tileEntity instanceof IInventory) {
				recieverI.add(i);
				isConnecting[i] = true;
			}
		}
		for (int i = 0; i < 6; i++) {
			ITileEntityEEMachine machine = this.getNeighborMachine(i);
			if (machine != null && machine.getTier(oppositeSide[i]) > tier)
				machine.setTier(tier + 1, oppositeSide[i]);
		}
		world.markBlockForUpdate(x, y, z);
	}

	@Override
	public int getSizeInventory() {
		return 6;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return itemStacks[slot];
	}

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

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (this.itemStacks[slot] != null) {
			ItemStack itemstack = this.itemStacks[slot];
			this.itemStacks[slot] = null;
			return itemstack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		this.itemStacks[slot] = itemStack;

		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
			itemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName() {
		return "container.EEItemTransporter";
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
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
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
		return new int[] { side };
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
		return true;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
		return false;
	}

}
