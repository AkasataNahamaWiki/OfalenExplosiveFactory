package nahamawiki.oef.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityEEMachineBase extends TileEntity implements ITileEntityEEMachine {

	protected int holdingEE;

	public TileEntityEEMachineBase() {
		super();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("holdingEE", holdingEE);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		holdingEE = nbt.getInteger("holdingEE");
	}
}
