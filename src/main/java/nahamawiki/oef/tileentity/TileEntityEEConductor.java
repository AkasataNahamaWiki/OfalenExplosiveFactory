package nahamawiki.oef.tileentity;

import static net.minecraft.util.Facing.*;

import java.util.ArrayList;

import nahamawiki.oef.OEFCore;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class TileEntityEEConductor extends TileEntityEEMachineBase {

	protected int capacity = 8000;
	protected int loss = -1;
	protected int[] holdingEEArray = new int[6];
	protected ArrayList<Integer> provider = new ArrayList<Integer>();
	protected ArrayList<Integer> reciver = new ArrayList<Integer>();

	public TileEntityEEConductor() {
		super();
	}

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
				StatCollector.translateToLocal("info.EEMachineState.level") + this.getLevel(this.getBlockMetadata()),
				StatCollector.translateToLocal("info.EEMachineState.capacity") + capacity + " EE",
				StatCollector.translateToLocal("info.EEMachineState.holding") + holdingEE + " EE"
		};
	}

	@Override
	public int getLevel(int meta) {
		return meta & 3;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		for (int i = 0; i < 6; i++) {
			nbt.setInteger("holdingEEArray-" + i, holdingEEArray[i]);
		}

		NBTTagCompound localnbt = new NBTTagCompound();
		for (int i = 0; i < provider.size(); i++) {
			localnbt.setInteger(String.valueOf(i), provider.get(i));
		}
		nbt.setInteger("providerSize", provider.size());
		nbt.setTag("provider", localnbt);

		localnbt = new NBTTagCompound();
		for (int i = 0; i < reciver.size(); i++) {
			localnbt.setInteger(String.valueOf(i), reciver.get(i));
		}
		nbt.setInteger("reciverSize", reciver.size());
		nbt.setTag("reciver", localnbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		for (int i = 0; i < 6; i++) {
			holdingEEArray[i] = nbt.getInteger("holdingEEArray-" + i);
		}

		provider.clear();
		NBTTagCompound localnbt = nbt.getCompoundTag("provider");
		for (int i = 0; i < nbt.getInteger("providerSize"); i++) {
			provider.add(localnbt.getInteger(String.valueOf(i)));
		}

		reciver.clear();
		localnbt = nbt.getCompoundTag("reciver");
		for (int i = 0; i < nbt.getInteger("reciverSize"); i++) {
			reciver.add(localnbt.getInteger(String.valueOf(i)));
		}
	}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote)
			return;
		if (loss < 0) {
			switch (this.getLevel(this.getBlockMetadata())) {
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
				OEFCore.logger.info("Delete " + loss + " EE, Side : " + i);
			}
		}
		if (reciver.size() < 1)
			return;
		for (int i = 0; i < 6; i++) {
			if (holdingEEArray[i] < 1)
				continue;
			int sendingEE;
			int reciverNum = reciver.size();
			if (reciver.contains(i)) {
				if (reciverNum < 2)
					continue;
				sendingEE = holdingEEArray[i] / (reciverNum - 1);
				holdingEEArray[i] %= (reciverNum - 1);
			} else {
				sendingEE = holdingEEArray[i] / reciverNum;
				holdingEEArray[i] %= reciverNum;
			}
			if (sendingEE < 1)
				continue;
			for (int j = 0; j < 6; j++) {
				if (reciver.contains(j) && j != i) {
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

	/** 周囲のブロックを確認する */
	public void updateDirection(World world, int x, int y, int z) {
		provider.clear();
		reciver.clear();
		for (int i = 0; i < 6; i++) {
			TileEntity tileEntity = world.getTileEntity(x + offsetsXForSide[i], y + offsetsYForSide[i], z + offsetsZForSide[i]);
			if (tileEntity != null && tileEntity instanceof ITileEntityEEMachine) {
				ITileEntityEEMachine machine = (ITileEntityEEMachine) tileEntity;
				int type = machine.getMachineType(oppositeSide[i]);
				if ((type & 1) == 1) {
					provider.add(i);
				}
				if ((type & 2) == 2) {
					reciver.add(i);
				}
			}
		}
	}

}
