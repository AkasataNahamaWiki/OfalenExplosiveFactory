package nahamawiki.oef.tileentity;

import nahamawiki.oef.core.OEFConfigCore;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityEEMachineBase extends TileEntity implements ITileEntityEEMachine {

	/** 蓄えているEEの量。 */
	protected int holdingEE;
	/** 蓄えられるEEの上限。 */
	protected int capacity = -1;
	/** 機械のレベル。 */
	protected byte level = -1;
	/** 匠化しているか否か。 */
	protected boolean isCreeper;

	public int tick;

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
	public void setCreeper(boolean flag) {
		this.isCreeper = flag;
		if (!worldObj.isRemote)
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		tick++;
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
		nbt.setInteger("HoldingEE", holdingEE);
		nbt.setByte("Level", level);
		nbt.setBoolean("IsCreeper", isCreeper);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		holdingEE = nbt.getInteger("HoldingEE");
		level = nbt.getByte("Level");
		isCreeper = nbt.getBoolean("IsCreeper");
	}

	/** 送信するパケットを返す。 */
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("IsCreeper", isCreeper);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
	}

	/** パケットを受信した時の処理。 */
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		NBTTagCompound nbt = pkt.func_148857_g();
		isCreeper = nbt.getBoolean("IsCreeper");
	}

}
