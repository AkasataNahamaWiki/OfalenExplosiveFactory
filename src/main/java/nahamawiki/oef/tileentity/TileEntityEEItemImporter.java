package nahamawiki.oef.tileentity;

import static net.minecraft.util.Facing.*;

import java.util.ArrayList;

import nahamawiki.oef.OEFCore;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.world.World;

public class TileEntityEEItemImporter extends TileEntityEEItemTransporter {

	protected ArrayList<Integer> inventory = new ArrayList<Integer>();

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		NBTTagCompound localnbt = new NBTTagCompound();
		for (int i = 0; i < inventory.size(); i++) {
			localnbt.setInteger(String.valueOf(i), inventory.get(i));
		}
		nbt.setByte("inventorySize", (byte) recieverI.size());
		nbt.setTag("inventory", localnbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		inventory.clear();
		NBTTagCompound localnbt = nbt.getCompoundTag("inventory");
		for (int i = 0; i < nbt.getByte("inventorySize"); i++) {
			inventory.add(localnbt.getInteger(String.valueOf(i)));
		}
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (worldObj.isRemote)
			return;
		this.importItems();
	}

	protected void importItems() {
		if (holdingEE < 4)
			return;
		OEFCore.logger.info("Start importItems");
		for (int i = 0; i < 6; i++) {
			OEFCore.logger.info("Start for loop. side : " + i);
			if (itemStacks[i] != null && itemStacks[i].stackSize >= itemStacks[i].getMaxStackSize())
				continue;
			IInventory iinventory = super.getIInventory(i);
			if (iinventory == null)
				continue;
			OEFCore.logger.info("Get IInventory");
			if (iinventory instanceof ISidedInventory) {
				ISidedInventory isidedinventory = (ISidedInventory) iinventory;
				int[] aint = isidedinventory.getAccessibleSlotsFromSide(oppositeSide[i]);
				for (int slot = 0; slot < aint.length; slot++) {
					OEFCore.logger.info("Start for loop. slot : " + slot);
					ItemStack itemStack = isidedinventory.getStackInSlot(slot);
					if (!isidedinventory.canExtractItem(slot, itemStack, oppositeSide[i]))
						continue;
					this.importSlotContents(iinventory, slot, i);
					if (holdingEE < 4)
						return;
				}
			} else {
				for (int slot = 0; slot < iinventory.getSizeInventory(); slot++) {
					OEFCore.logger.info("Start for loop. slot : " + slot);
					this.importSlotContents(iinventory, slot, i);
					if (holdingEE < 4)
						return;
				}
			}
		}
	}

	@Override
	protected IInventory getIInventory(int side) {
		IInventory iinventory = null;
		TileEntity tileEntity = worldObj.getTileEntity(xCoord + offsetsXForSide[side], yCoord + offsetsYForSide[side], zCoord + offsetsZForSide[side]);
		if (tileEntity != null && tileEntity instanceof TileEntityEEItemTransporter) {
			iinventory = (IInventory) tileEntity;
		}
		return iinventory;
	}

	protected void importSlotContents(IInventory iinventory, int slot, int side) {
		OEFCore.logger.info("Start importSlotContents");
		ItemStack itemStack = iinventory.getStackInSlot(slot);
		if (itemStack == null) {
			OEFCore.logger.info("itemStack == null");
			return;
		}
		while (iinventory.getStackInSlot(slot) != null) {
			ItemStack itemStack1 = iinventory.getStackInSlot(slot).copy();
			ItemStack itemStack2 = TileEntityHopper.func_145889_a(this, iinventory.decrStackSize(slot, 1), side);
			if (itemStack2 == null || itemStack2.stackSize < 1) {
				iinventory.markDirty();
				holdingEE -= 4;
				OEFCore.logger.info("Succeed importSlotContents. size : " + this.itemStacks[side].stackSize);
				if (holdingEE < 4)
					return;
			}
			iinventory.setInventorySlotContents(slot, itemStack1);
			OEFCore.logger.info("Fail importSlotContents. holdingEE : " + holdingEE);
			break;
		}
	}

	@Override
	public void updateDirection(World world, int x, int y, int z) {
		reciever.clear();
		recieverI.clear();
		inventory.clear();
		for (int i = 0; i < 6; i++) {
			isConnecting[i] = false;
			TileEntity tileEntity = world.getTileEntity(x + offsetsXForSide[i], y + offsetsYForSide[i], z + offsetsZForSide[i]);
			if (tileEntity != null && tileEntity instanceof ITileEntityEEMachine) {
				ITileEntityEEMachine machine = (ITileEntityEEMachine) tileEntity;
				int type = machine.getMachineType(oppositeSide[i]);
				if ((type & 2) == 2) {
					reciever.add(i);
				}
				isConnecting[i] = (type != 0);
			}
			if (tileEntity != null && tileEntity instanceof IInventory) {
				if (!(tileEntity instanceof TileEntityEEItemTransporter)) {
					inventory.add(i);
				}
				recieverI.add(i);
				isConnecting[i] = true;
			}
		}
		world.markBlockForUpdate(x, y, z);
	}

	@Override
	public String getInventoryName() {
		return "container.EEItemImporter";
	}

}
