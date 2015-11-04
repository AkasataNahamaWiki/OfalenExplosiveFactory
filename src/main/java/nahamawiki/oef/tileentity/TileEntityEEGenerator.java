package nahamawiki.oef.tileentity;

import static net.minecraft.util.Facing.*;

import java.util.ArrayList;

import nahamawiki.oef.util.EEUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class TileEntityEEGenerator extends TileEntityEEMachineBase {

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
				StatCollector.translateToLocal("info.EEMachineState.name") + StatCollector.translateToLocal(this.getBlockType().getLocalizedName()),
				StatCollector.translateToLocal("info.EEMachineState.level") + level,
				StatCollector.translateToLocal("info.EEMachineState.generating") + sendingEE + " EE"
		};
	}

	@Override
	public int getTier(int side) {
		return 0;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		NBTTagCompound localnbt = new NBTTagCompound();
		for (int i = 0; i < reciever.size(); i++) {
			localnbt.setInteger(String.valueOf(i), reciever.get(i));
		}
		nbt.setInteger("reciverSize", reciever.size());
		nbt.setTag("reciver", localnbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		reciever.clear();
		NBTTagCompound localnbt = nbt.getCompoundTag("reciver");
		for (int i = 0; i < nbt.getInteger("reciverSize"); i++) {
			reciever.add(localnbt.getInteger(String.valueOf(i)));
		}
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (worldObj.isRemote)
			return;
		this.sendEE();
	}

	/** 隣接する機械にEEを送信する。 */
	protected void sendEE() {
		// 送信先がないなら終了。
		if (reciever.size() < 1)
			return;
		// 送信先リストをコピー。
		ArrayList<Integer> list = EEUtil.copyList(reciever);
		// メタデータに応じてEEを生成する。
		switch (worldObj.getBlockMetadata(xCoord, yCoord, zCoord)) {
		case 4:
			holdingEE = 100;
			break;
		case 5:
			holdingEE = 200;
			break;
		case 6:
			holdingEE = 400;
			break;
		case 7:
			holdingEE = 800;
			break;
		default:
			holdingEE = 0;
		}
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
