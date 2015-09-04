package nahamawiki.oef.tileentity;

import static net.minecraft.util.Facing.*;

import java.util.ArrayList;

import nahamawiki.oef.block.BlockEEMachineBase;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class TileEntityEEConductor extends TileEntityEEMachineBase {

	protected ArrayList<Integer> reciver = new ArrayList<Integer>();
	protected ArrayList<Integer> provider = new ArrayList<Integer>();
	protected ArrayList<Integer> sender = new ArrayList<Integer>();

	public TileEntityEEConductor() {
		super();
	}

	@Override
	public void reciveEE(int amount, int side) {
		holdingEE[side] += amount;
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

		localnbt = new NBTTagCompound();
		for (int i = 0; i < provider.size(); i++) {
			localnbt.setInteger(String.valueOf(i), provider.get(i));
		}
		nbt.setInteger("providerSize", provider.size());
		nbt.setTag("provider", localnbt);

		localnbt = new NBTTagCompound();
		for (int i = 0; i < sender.size(); i++) {
			localnbt.setInteger(String.valueOf(i), sender.get(i));
		}
		nbt.setInteger("senderSize", sender.size());
		nbt.setTag("sender", localnbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		reciver.clear();
		NBTTagCompound localnbt = nbt.getCompoundTag("reciver");
		for (int i = 0; i < nbt.getInteger("reciverSize"); i++) {
			reciver.add(localnbt.getInteger(String.valueOf(i)));
		}

		provider.clear();
		localnbt = nbt.getCompoundTag("provider");
		for (int i = 0; i < nbt.getInteger("providerSize"); i++) {
			provider.add(localnbt.getInteger(String.valueOf(i)));
		}

		sender.clear();
		localnbt = nbt.getCompoundTag("sender");
		for (int i = 0; i < nbt.getInteger("senderSize"); i++) {
			sender.add(localnbt.getInteger(String.valueOf(i)));
		}
	}

	@Override
	public void updateEntity() {
		for (int i = 0; i < sender.size(); i++) {
			BlockEEMachineBase machine = (BlockEEMachineBase) worldObj.getBlock(xCoord + offsetsXForSide[sender.get(i)], yCoord + offsetsYForSide[sender.get(i)], zCoord + offsetsZForSide[sender.get(i)]);
			this.reciveEE(machine.providingEE(), sender.get(i));
		}
		if (reciver.size() < 1)
			return;
		for (int i = 0; i < 6; i++) {
			if (holdingEE[i] < 1)
				return;
			int sendEE;
			if (reciver.contains(i)) {
				if (reciver.size() < 2)
					continue;
				sendEE = holdingEE[i] / (reciver.size() - 1);
				holdingEE[i] %= (reciver.size() - 1);
			} else {
				sendEE = holdingEE[i] / reciver.size();
				holdingEE[i] %= reciver.size();
			}
			if (sendEE < 1)
				continue;
			for (int j = 0; j < 6; j++) {
				if (reciver.contains(j) && j != i) {
					TileEntityEEMachineBase machine = (TileEntityEEMachineBase) worldObj.getTileEntity(xCoord + offsetsXForSide[j], yCoord + offsetsYForSide[j], zCoord + offsetsZForSide[j]);
					machine.reciveEE(sendEE, oppositeSide[j]);
				}
			}
		}
	}

	/** 周囲のブロックを確認する */
	public void updateDirection(World world, int x, int y, int z) {
		reciver.clear();
		provider.clear();
		sender.clear();
		for (int i = 0; i < 6; i++) {
			Block block = world.getBlock(x + offsetsXForSide[i], y + offsetsYForSide[i], z + offsetsZForSide[i]);
			if (block instanceof BlockEEMachineBase) {
				BlockEEMachineBase machine = (BlockEEMachineBase) block;
				if (machine.canReciveEE()) {
					reciver.add(i);
				}
				if (machine.canProvideEE()) {
					provider.add(i);
					if (!machine.hasTileEntity(world.getBlockMetadata(x, y, z))) {
						sender.add(i);
					}
				}
			}
		}
	}

}
