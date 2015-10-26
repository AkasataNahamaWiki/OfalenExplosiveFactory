package nahamawiki.oef.tileentity;

import static net.minecraft.util.Facing.*;

import java.util.ArrayList;

import nahamawiki.oef.util.EEUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class TileEntityEEConductor extends TileEntityEEMachineBase {

	protected int capacity = 8000;
	protected int loss = -1;
	protected int tier = Integer.MAX_VALUE;
	protected ArrayList<Integer> reciever = new ArrayList<Integer>();
	protected boolean[] isConnecting = new boolean[6];
	protected boolean isHoldingEE;

	@Override
	public int getMachineType(int side) {
		return 3;
	}

	@Override
	public int recieveEE(int amount, int side) {
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
				StatCollector.translateToLocal("info.EEMachineState.level") + level,
				StatCollector.translateToLocal("info.EEMachineState.capacity") + capacity + " EE",
				StatCollector.translateToLocal("info.EEMachineState.holding") + holdingEE + " EE",
				StatCollector.translateToLocal("info.EEMachineState.tier") + tier,
		};
	}

	@Override
	public byte getLevel(int meta) {
		if (level < 0)
			return (byte) (meta & 3);
		return level;
	}

	@Override
	public int getTier(int side) {
		return tier;
	}

	@Override
	public void setTier(int tier, int side) {
		if (this.getTier(side) > tier) {
			for (int i = 0; i < 6; i++) {
				ITileEntityEEMachine machine = this.getNeighborMachine(side);
				this.tier = tier;
				if (machine != null && machine.getTier(oppositeSide[side]) > tier) {
					machine.setTier(tier + 1, oppositeSide[side]);
				}
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("tier", tier);

		NBTTagCompound localnbt = new NBTTagCompound();
		for (int i = 0; i < reciever.size(); i++) {
			localnbt.setInteger(String.valueOf(i), reciever.get(i));
		}
		nbt.setInteger("reciverSize", reciever.size());
		nbt.setTag("reciver", localnbt);

		for (int i = 0; i < 6; i++) {
			nbt.setBoolean("isConnecting-" + i, isConnecting[i]);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		tier = nbt.getInteger("tier");

		reciever.clear();
		NBTTagCompound localnbt = nbt.getCompoundTag("reciver");
		for (int i = 0; i < nbt.getInteger("reciverSize"); i++) {
			reciever.add(localnbt.getInteger(String.valueOf(i)));
		}

		for (int i = 0; i < 6; i++) {
			isConnecting[i] = nbt.getBoolean("isConnecting-" + i);
		}
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (worldObj.isRemote)
			return;
		this.updateIsHoldingEE();
		this.decreaseEE();
		this.sendEE();
	}

	protected void updateIsHoldingEE() {
		if (isHoldingEE != holdingEE > 0) {
			if (holdingEE > 0) {
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, level | 4, 2);
				isHoldingEE = true;
			} else {
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, level & 3, 2);
				isHoldingEE = false;
			}
			this.markDirty();
		}
	}

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
		holdingEE -= loss;
	}

	protected void sendEE() {
		if (reciever.size() < 1)
			return;
		ArrayList<Integer> list = EEUtil.copyList(reciever);
		while (list.size() > 0 && holdingEE / list.size() > 0) {
			int sendingEE = holdingEE / list.size();
			holdingEE %= list.size();
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
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		for (int i = 0; i < 6; i++) {
			nbt.setBoolean("isConnecting-" + i, isConnecting[i]);
		}
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		NBTTagCompound nbt = pkt.func_148857_g();
		for (int i = 0; i < 6; i++) {
			isConnecting[i] = nbt.getBoolean("isConnecting-" + i);
		}
	}

	/** 周囲のブロックを確認する */
	public void updateDirection(World world, int x, int y, int z) {
		reciever.clear();
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
		}
		for (int i = 0; i < 6; i++) {
			ITileEntityEEMachine machine = this.getNeighborMachine(i);
			if (machine != null && machine.getTier(oppositeSide[i]) > tier)
				machine.setTier(tier + 1, oppositeSide[i]);
		}
		world.markBlockForUpdate(x, y, z);
	}

	protected ITileEntityEEMachine getNeighborMachine(int side) {
		TileEntity tileEntity = worldObj.getTileEntity(xCoord + offsetsXForSide[side], yCoord + offsetsYForSide[side], zCoord + offsetsZForSide[side]);
		if (tileEntity != null && tileEntity instanceof ITileEntityEEMachine)
			return (ITileEntityEEMachine) tileEntity;
		return null;
	}

	public boolean[] getConnectingArray() {
		return isConnecting;
	}

	public boolean isHoldingEE() {
		return (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & 4) != 0;
	}

}
