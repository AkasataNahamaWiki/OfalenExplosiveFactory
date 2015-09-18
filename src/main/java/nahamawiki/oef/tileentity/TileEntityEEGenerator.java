package nahamawiki.oef.tileentity;

import static net.minecraft.util.Facing.*;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class TileEntityEEGenerator extends TileEntityEEMachineBase {

	protected ArrayList<Integer> reciver = new ArrayList<Integer>();

	@Override
	public int getMachineType(int side) {
		return 1;
	}

	@Override
	public int reciveEE(int amount, int side) {
		return amount;
	}

	@Override
	public String[] getState(EntityPlayer player) {
		blockMetadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		int sendingEE = 0;
		switch (blockMetadata) {
		case 4:
			sendingEE = 100;
			break;
		case 5:
			sendingEE = 200;
			break;
		case 6:
			sendingEE = 400;
			break;
		case 7:
			sendingEE = 800;
			break;
		}
		return new String[] {
				StatCollector.translateToLocal("info.EEMachineState.name") + this.getBlockType().getLocalizedName(),
				StatCollector.translateToLocal("info.EEMachineState.level") + this.getLevel(this.getBlockMetadata()),
				StatCollector.translateToLocal("info.EEMachineState.meta") + this.getBlockMetadata(),
				StatCollector.translateToLocal("info.EEMachineState.providing") + sendingEE
		};
	}

	@Override
	public int getLevel(int meta) {
		return meta & 3;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		NBTTagCompound localnbt = new NBTTagCompound();
		for (int i = 0; i < reciver.size(); i++) {
			localnbt.setInteger(String.valueOf(i), reciver.get(i));
		}
		nbt.setInteger("reciverSize", reciver.size());
		nbt.setTag("reciver", localnbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		reciver.clear();
		NBTTagCompound localnbt = nbt.getCompoundTag("reciver");
		for (int i = 0; i < nbt.getInteger("reciverSize"); i++) {
			reciver.add(localnbt.getInteger(String.valueOf(i)));
		}
	}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote || reciver.size() < 1)
			return;
		int sendingEE = 0;
		switch (worldObj.getBlockMetadata(xCoord, yCoord, zCoord)) {
		case 4:
			sendingEE = 100;
			break;
		case 5:
			sendingEE = 200;
			break;
		case 6:
			sendingEE = 400;
			break;
		case 7:
			sendingEE = 800;
			break;
		}
		if (sendingEE < 1)
			return;
		sendingEE /= reciver.size();
		int reciverNum = reciver.size();
		for (int i = 0; i < reciver.size(); i++) {
			reciverNum--;
			int side = reciver.get(i);
			ITileEntityEEMachine machine = (ITileEntityEEMachine) worldObj.getTileEntity(xCoord + offsetsXForSide[side], yCoord + offsetsYForSide[side], zCoord + offsetsZForSide[side]);
			if (machine == null)
				continue;
			int surplus = machine.reciveEE(sendingEE, oppositeSide[side]);
			if (surplus < 1 || reciverNum < 1)
				continue;
			sendingEE += surplus / reciverNum;
		}
	}

	/** 周囲のブロックを確認する */
	public void updateDirection(World world, int x, int y, int z) {
		for (int i = 0; i < 6; i++) {
			TileEntity tileEntity = world.getTileEntity(x + offsetsXForSide[i], y + offsetsYForSide[i], z + offsetsZForSide[i]);
			if (tileEntity != null && tileEntity instanceof ITileEntityEEMachine) {
				ITileEntityEEMachine machine = (ITileEntityEEMachine) tileEntity;
				int type = machine.getMachineType(oppositeSide[i]);
				if ((type & 2) == 2) {
					reciver.add(i);
				}
			}
		}
	}

}
