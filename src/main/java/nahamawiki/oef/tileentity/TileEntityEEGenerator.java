package nahamawiki.oef.tileentity;

import static net.minecraft.util.Facing.*;

import java.util.ArrayList;
import java.util.Random;

import nahamawiki.oef.OEFCore;
import nahamawiki.oef.util.EEUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class TileEntityEEGenerator extends TileEntityEEMachineBase {

	protected ArrayList<Integer> reciever = new ArrayList<Integer>();
	protected byte duration;
	protected boolean isSending;

	@Override
	public int getMachineType(int side) {
		return 1;
	}

	@Override
	public int recieveEE(int amount, int side) {
		return amount;
	}

	@Override
	public String[] getState() {
		return new String[] {
				StatCollector.translateToLocal("info.EEMachineState.name") + StatCollector.translateToLocal(this.getBlockType().getLocalizedName()),
				StatCollector.translateToLocal("info.EEMachineState.level") + level,
				StatCollector.translateToLocal("info.EEMachineState.generating") + this.getSendEE() + " EE"
		};
	}

	@Override
	public int getTier(int side) {
		return 0;
	}

	@Override
	public int getCapacity(int level) {
		return 0;
	}

	@Override
	public void updateMachine() {
		this.sendEE();
		if (duration > 0)
			duration--;
		if (duration > 0 != isSending) {
			if (duration > 0) {
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, level + 4, 2);
				isSending = true;
			} else {
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, level, 2);
				isSending = false;
			}
		}
	}

	@Override
	public void updateCreepered() {
		// ランダムで爆発を起こす。
		Random random = new Random();
		if (random.nextInt(1200) == 0) {
			worldObj.createExplosion(null, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, random.nextFloat() * 10, true);
		}
		// 送信先の機械を匠化する。
		if (reciever.size() < 1)
			return;
		for (int i = 0; i < 6; i++) {
			if (!reciever.contains(i))
				continue;
			ITileEntityEEMachine machine = this.getNeighborMachine(i);
			if (machine == null || machine.getCreeper())
				continue;
			machine.setCreeper(true);
		}
	}

	/** 隣接する機械にEEを送信する。 */
	protected void sendEE() {
		// 送信中でないか、送信先がないなら終了。
		if (duration < 1 || reciever.size() < 1)
			return;
		// 送信先リストをコピー。
		ArrayList<Integer> list = EEUtil.copyList(reciever);
		// 送信する量を記録。
		holdingEE = this.getSendEE();
		OEFCore.logger.info("Set holdingEE to " + holdingEE);
		// 送信先があるなら、EEが足りる限りループする。
		while (list.size() > 0 && holdingEE / list.size() > 0) {
			// 蓄えているEEを送信先の数で割って代入。
			int sendingEE = holdingEE / list.size();
			// holdingEEをあまりの量にする。
			holdingEE %= list.size();
			for (int i = 0; i < 6; i++) {
				// 送信先リストに登録されていないなら次へ。
				if (!list.contains(i))
					continue;
				ITileEntityEEMachine machine = this.getNeighborMachine(i);
				if (machine == null) {
					// 機械が存在しないか、tierがこの伝導管より小さいならリストから削除。
					list.remove(list.indexOf(i));
					holdingEE += sendingEE;
					continue;
				}
				// EEを渡して、あまりを取得。
				int surplus = machine.recieveEE(sendingEE, oppositeSide[i]);
				// あまりがないなら次へ。
				if (surplus < 1)
					continue;
				// 余ったなら回収して、リストから削除。
				holdingEE += surplus;
				list.remove(list.indexOf(i));
			}
		}
	}

	/** 送信するEEの量を返す。 */
	protected int getSendEE() {
		if (duration < 1)
			return 0;
		switch (level) {
		case 0:
			return 100;
		case 1:
			return 200;
		case 2:
			return 400;
		case 3:
			return 800;
		}
		return 0;
	}

	/** 指定された方向の機械を取得する。 */
	protected ITileEntityEEMachine getNeighborMachine(int side) {
		TileEntity tileEntity = worldObj.getTileEntity(xCoord + offsetsXForSide[side], yCoord + offsetsYForSide[side], zCoord + offsetsZForSide[side]);
		if (tileEntity != null && tileEntity instanceof ITileEntityEEMachine)
			return (ITileEntityEEMachine) tileEntity;
		return null;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setByte("Duration", duration);

		NBTTagCompound localnbt = new NBTTagCompound();
		for (int i = 0; i < reciever.size(); i++) {
			localnbt.setInteger(String.valueOf(i), reciever.get(i));
		}
		nbt.setInteger("RecieverSize", reciever.size());
		nbt.setTag("Reciever", localnbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		duration = nbt.getByte("Duration");

		reciever.clear();
		NBTTagCompound localnbt = nbt.getCompoundTag("Reciever");
		for (int i = 0; i < nbt.getInteger("RecieverSize"); i++) {
			reciever.add(localnbt.getInteger(String.valueOf(i)));
		}
	}

	/** 周囲のブロックを確認する */
	public void updateDirection(World world, int x, int y, int z) {
		// リストをリセット。
		reciever.clear();
		for (int i = 0; i < 6; i++) {
			ITileEntityEEMachine machine = this.getNeighborMachine(i);
			if (machine != null) {
				int type = machine.getMachineType(oppositeSide[i]);
				if ((type & 2) == 2) {
					// EE機械があり、受信可能なら登録。
					reciever.add(i);
				}
			}
		}
	}

	public void onExploded() {
		if (worldObj.isRemote)
			return;
		duration = 40;
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, level + 4, 2);
		isSending = true;
		OEFCore.logger.info("onExploded");
	}

}
