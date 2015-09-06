package nahamawiki.oef.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityEEMachineBase extends TileEntity {

	public TileEntityEEMachineBase() {
		super();
	}

	/**
	 * EEを受け取る処理
	 *
	 * @param amount
	 *            受け取るEE
	 * @param side
	 *            受け取る面
	 * @return あまり
	 */
	public abstract int reciveEE(int amount, int side);

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
	}
}
