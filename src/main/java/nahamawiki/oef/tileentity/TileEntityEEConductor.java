package nahamawiki.oef.tileentity;

import static net.minecraft.util.Facing.*;

import java.util.ArrayList;

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
	protected int[] holdingEEArray = new int[6];
	protected ArrayList<Integer> provider = new ArrayList<Integer>();
	protected ArrayList<Integer> reciever = new ArrayList<Integer>();
	protected boolean[] isConnecting = new boolean[6];
	protected boolean isHoldingEE;
	protected boolean lastIsHoldingEE;

	@Override
	public int getMachineType(int side) {
		return 3;
	}

	@Override
	public int recieveEE(int amount, int side) {
		holdingEEArray[side] += amount;
		if (holdingEEArray[side] > capacity) {
			int surplus = holdingEEArray[side] - capacity;
			holdingEEArray[side] = capacity;
			return surplus;
		}
		return 0;
	}

	@Override
	public String[] getState() {
		holdingEE = 0;
		for (int i = 0; i < 6; i++) {
			holdingEE += holdingEEArray[i];
		}
		return new String[] {
				StatCollector.translateToLocal("info.EEMachineState.name") + StatCollector.translateToLocal(this.getBlockType().getLocalizedName()),
				StatCollector.translateToLocal("info.EEMachineState.level") + level,
				StatCollector.translateToLocal("info.EEMachineState.capacity") + capacity + " EE",
				StatCollector.translateToLocal("info.EEMachineState.holding") + holdingEE + " EE"
		};
	}

	@Override
	public byte getLevel(int meta) {
		if (level < 0)
			return (byte) (meta & 3);
		return level;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		nbt.setIntArray("holdingEEArray", holdingEEArray);

		NBTTagCompound localnbt = new NBTTagCompound();
		for (int i = 0; i < provider.size(); i++) {
			localnbt.setInteger(String.valueOf(i), provider.get(i));
		}
		nbt.setInteger("providerSize", provider.size());
		nbt.setTag("provider", localnbt);

		localnbt = new NBTTagCompound();
		for (int i = 0; i < reciever.size(); i++) {
			localnbt.setInteger(String.valueOf(i), reciever.get(i));
		}
		nbt.setInteger("reciverSize", reciever.size());
		nbt.setTag("reciver", localnbt);

		for (int i = 0; i < 6; i++) {
			nbt.setBoolean("isConnecting-" + i, isConnecting[i]);
		}
		// OEFCore.logger.info("Complete writeToNBT");
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		holdingEEArray = nbt.getIntArray("holdingEEArray");

		provider.clear();
		NBTTagCompound localnbt = nbt.getCompoundTag("provider");
		for (int i = 0; i < nbt.getInteger("providerSize"); i++) {
			provider.add(localnbt.getInteger(String.valueOf(i)));
		}

		reciever.clear();
		localnbt = nbt.getCompoundTag("reciver");
		for (int i = 0; i < nbt.getInteger("reciverSize"); i++) {
			reciever.add(localnbt.getInteger(String.valueOf(i)));
		}

		for (int i = 0; i < 6; i++) {
			isConnecting[i] = nbt.getBoolean("isConnecting-" + i);
			// OEFCore.logger.info("isConnecting[" + i + "] : " + isConnecting[i] + " by readFromNBT");
		}
		// OEFCore.logger.info("Complete readFromNBT");
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (worldObj.isRemote)
			return;
		isHoldingEE = false;
		for (int i = 0; i < 6; i++) {
			if (holdingEEArray[i] > 0) {
				isHoldingEE = true;
				break;
			}
		}
		if (isHoldingEE != lastIsHoldingEE) {
			if (isHoldingEE) {
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, level | 4, 2);
				lastIsHoldingEE = true;
			} else {
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, level & 3, 2);
				lastIsHoldingEE = false;
			}
			this.markDirty();
		}
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
			for (int i = 0; i < 6; i++) {
				if (holdingEEArray[i] < 1)
					continue;
				holdingEEArray[i] -= loss;
				if (holdingEEArray[i] < 0)
					holdingEEArray[i] = 0;
			}
		}
		if (reciever.size() < 1)
			return;
		for (int i = 0; i < 6; i++) {
			if (holdingEEArray[i] < 1)
				continue;
			int sendingEE;
			int reciverNum = reciever.size();
			if (reciever.contains(i)) {
				reciverNum--;
				if (reciverNum < 1)
					continue;
			}
			sendingEE = holdingEEArray[i] / reciverNum;
			holdingEEArray[i] %= reciverNum;
			if (sendingEE < 1)
				continue;
			for (int j = 0; j < 6; j++) {
				if (reciever.contains(j) && j != i) {
					reciverNum--;
					ITileEntityEEMachine machine = (ITileEntityEEMachine) worldObj.getTileEntity(xCoord + offsetsXForSide[j], yCoord + offsetsYForSide[j], zCoord + offsetsZForSide[j]);
					int surplus = machine.recieveEE(sendingEE, oppositeSide[j]);
					if (surplus < 1)
						continue;
					if (reciverNum < 1) {
						holdingEEArray[i] += surplus;
						continue;
					}
					sendingEE += surplus / reciverNum;
					holdingEEArray[i] += surplus % reciverNum;
				}
			}
		}
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		for (int i = 0; i < 6; i++) {
			nbt.setBoolean("isConnecting-" + i, isConnecting[i]);
		}
		// OEFCore.logger.info("Finish getDescriptionPacket");
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		NBTTagCompound nbt = pkt.func_148857_g();
		for (int i = 0; i < 6; i++) {
			isConnecting[i] = nbt.getBoolean("isConnecting-" + i);
			// OEFCore.logger.info("isConnecting[" + i + "] : " + isConnecting[i] + " by onDataPacket");
		}
		// OEFCore.logger.info("Finish onDataPacket");
	}

	/** 周囲のブロックを確認する */
	public void updateDirection(World world, int x, int y, int z) {
		provider.clear();
		reciever.clear();
		for (int i = 0; i < 6; i++) {
			TileEntity tileEntity = world.getTileEntity(x + offsetsXForSide[i], y + offsetsYForSide[i], z + offsetsZForSide[i]);
			if (tileEntity != null && tileEntity instanceof ITileEntityEEMachine) {
				ITileEntityEEMachine machine = (ITileEntityEEMachine) tileEntity;
				int type = machine.getMachineType(oppositeSide[i]);
				if ((type & 1) == 1) {
					provider.add(i);
				}
				if ((type & 2) == 2) {
					reciever.add(i);
				}
				isConnecting[i] = (type != 0);
			} else {
				isConnecting[i] = false;
			}
		}
		world.markBlockForUpdate(x, y, z);
	}

	public boolean[] getConnectingArray() {
		return isConnecting;
	}

	public boolean isHoldingEE() {
		return (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & 4) != 0;
	}

}
