package nahamawiki.oef.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityEEMachineBase extends TileEntity {

	protected int holdingEE;

	/** EEを受け取る処理 */
	public abstract void reciveEE(int amount);

	public TileEntityEEMachineBase() {
		holdingEE = 0;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		holdingEE = nbt.getInteger("holdingEE");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("holdingEE", holdingEE);
	}

}
