package nahamawiki.oef.tileentity;

import static net.minecraft.util.Facing.*;

import java.util.ArrayList;

import nahamawiki.oef.util.OEFUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class TileEntityEESupplier extends TileEntityEEMachineBase {

	protected ArrayList<Integer> reciever = new ArrayList<Integer>();

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
		};
	}

	@Override
	public byte getLevel(int meta) {
		return 0;
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
	}

	@Override
	public void updateCreepered() {
		this.sendEE();
	}

	/** 隣接する機械にEEを送信する。 */
	protected void sendEE() {
		// 送信中でないか、送信先がないなら終了。
		if (reciever.size() < 1)
			return;
		// 送信先リストをコピー。
		ArrayList<Integer> list = OEFUtil.copyList(reciever);
		// 送信する量を記録。
		holdingEE = 128000;
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

}
