package nahamawiki.oef.tileentity;

import nahamawiki.oef.core.OEFConfigCore;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityEEMachineBase extends TileEntity implements ITileEntityEEMachine {

	/** 蓄えているEEの量。 */
	protected int holdingEE;
	/** 蓄えられるEEの上限。 */
	protected int capacity = -1;
	/** 機械のレベル。 */
	protected byte level = -1;
	/** 匠化しているか否か。 */
	private boolean isCreeper;

	@Override
	public int getMachineType(int side) {
		return 2;
	}

	@Override
	public int recieveEE(int amount, int side) {
		if (this.getCreeper())
			return 0;
		holdingEE += amount;
		if (holdingEE > capacity) {
			int surplus = holdingEE - capacity;
			holdingEE = capacity;
			return surplus;
		}
		return 0;
	}

	@Override
	public byte getLevel(int meta) {
		return (byte) (meta & 3);
	}

	@Override
	public int getTier(int side) {
		return OEFConfigCore.maxTier;
	}

	@Override
	public void setTier(int tier, int side) {}

	@Override
	public boolean getCreeper() {
		return this.isCreeper;
	}

	@Override
	public void setCreeper(boolean flg) {
		this.isCreeper = flg;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (level < 0)
			level = this.getLevel(worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
		if (capacity < 0)
			capacity = this.getCapacity(level);
		if (worldObj.isRemote)
			return;
		if (!this.getCreeper()) {
			this.updateMachine();
		} else {
			this.updateCreepered();
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("holdingEE", holdingEE);
		nbt.setByte("level", level);
		nbt.setBoolean("isCreeper", isCreeper);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		holdingEE = nbt.getInteger("holdingEE");
		level = nbt.getByte("level");
		isCreeper = nbt.getBoolean("isCreeper");
	}

}
