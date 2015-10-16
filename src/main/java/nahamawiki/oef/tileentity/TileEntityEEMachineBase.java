package nahamawiki.oef.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityEEMachineBase extends TileEntity implements ITileEntityEEMachine {

	protected int holdingEE;
	protected byte level = -1;

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("holdingEE", holdingEE);
		nbt.setByte("level", level);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		holdingEE = nbt.getInteger("holdingEE");
		level = nbt.getByte("level");
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (level < 0)
			level = this.getLevel(worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
	}

}
