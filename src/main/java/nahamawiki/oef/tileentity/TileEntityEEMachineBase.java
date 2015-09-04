package nahamawiki.oef.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityEEMachineBase extends TileEntity {

	protected int holdingEE[] = new int[6];

	/** EEを受け取る処理 */
	public abstract void reciveEE(int amount, int side);

	public TileEntityEEMachineBase() {}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		for (int i = 0; i < 6; i++) {
			nbt.setInteger("holdingEE-" + i, holdingEE[i]);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		for (int i = 0; i < 6; i++) {
			holdingEE[i] = nbt.getInteger("holdingEE-" + i);
		}
	}

}
