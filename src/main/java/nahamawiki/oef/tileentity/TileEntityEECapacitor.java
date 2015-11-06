package nahamawiki.oef.tileentity;

import static net.minecraft.util.Facing.*;

import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.core.OEFConfigCore;
import nahamawiki.oef.util.EEUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class TileEntityEECapacitor extends TileEntityEEMachineBase {

	/** 毎tick発生するロスの量。 */
	protected int loss = -1;
	/** EEを受け取れる方向のリスト。 */
	protected ArrayList<Integer> reciever = new ArrayList<Integer>();
	/** その方向が接続されているか。 */
	protected boolean[] isConnecting = new boolean[6];
	/** 前のtickでEEを蓄えていたか。 */
	protected boolean isHoldingEE;
	/** その方向で送受信が可能か。 */
	protected byte[] sideType = new byte[6];

	@Override
	public int getMachineType(int side) {
		return sideType[side];
	}

	@Override
	public int recieveEE(int amount, int side) {
		if (sideType[side] != 2)
			return amount;
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
		};
	}

	@Override
	public int getTier(int side) {
		if (sideType[side] == 1)
			return 0;
		return OEFConfigCore.maxTier;
	}

	@Override
	public int getCapacity(int level) {
		return EEUtil.getBaseCapacity(level) * 4;
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

		for (int i = 0; i < 6; i++) {
			nbt.setBoolean("isConnecting-" + i, isConnecting[i]);
		}

		nbt.setByteArray("sideType", sideType);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		reciever.clear();
		NBTTagCompound localnbt = nbt.getCompoundTag("reciver");
		for (int i = 0; i < nbt.getInteger("reciverSize"); i++) {
			reciever.add(localnbt.getInteger(String.valueOf(i)));
		}

		for (int i = 0; i < 6; i++) {
			isConnecting[i] = nbt.getBoolean("isConnecting-" + i);
		}

		sideType = nbt.getByteArray("sideType");
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (worldObj.isRemote)
			return;
		this.updateIsHoldingEE();
		this.sendEE();
	}

	/** EEを蓄えているかによってメタデータを更新する。 */
	protected void updateIsHoldingEE() {
		// 前のtickと違ったら更新する。
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

	protected void sendEE() {
		// 送信先がないなら終了。
		if (reciever.size() < 1)
			return;
		// 送信先リストをコピー。
		ArrayList<Integer> list = EEUtil.copyList(reciever);
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
				if (sideType[i] != 1 || machine == null) {
					// 送信不可能な方向か、機械が存在しないならリストから削除。
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

	/** 送信するパケットを返す。 */
	@Override
	public Packet getDescriptionPacket() {
		// WorldクラスのmarkBlockForUpdateをすると呼ばれる。
		NBTTagCompound nbt = new NBTTagCompound();
		for (int i = 0; i < 6; i++) {
			nbt.setBoolean("isConnecting-" + i, isConnecting[i]);
		}
		nbt.setByteArray("sideType", sideType);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
	}

	/** パケットを受信した時の処理。 */
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		NBTTagCompound nbt = pkt.func_148857_g();
		for (int i = 0; i < 6; i++) {
			isConnecting[i] = nbt.getBoolean("isConnecting-" + i);
		}
		sideType = nbt.getByteArray("sideType");
	}

	/** 周囲のブロックを確認する */
	public void updateDirection(World world, int x, int y, int z) {
		// リストをリセット。
		reciever.clear();
		for (int i = 0; i < 6; i++) {
			// isConnectingもリセット。
			isConnecting[i] = false;
			ITileEntityEEMachine machine = this.getNeighborMachine(i);
			if (sideType[i] != 0 && machine != null) {
				// 接続可能で機械が存在するなら、機械のtypeを取得。
				int type = machine.getMachineType(oppositeSide[i]);
				if ((type & 2) == 2) {
					// 受信可能なら登録。
					reciever.add(i);
				}
				// 送信/受信が可能ならisConnectingに登録。
				isConnecting[i] = (type != 0);
			}
		}
		world.markBlockForUpdate(x, y, z);
	}

	/** 指定された方向の機械を取得する。 */
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

	public byte[] getSideTypes() {
		return sideType;
	}

	public void setSideType(int side, int type) {
		sideType[side] = (byte) type;
		worldObj.notifyBlockChange(xCoord, yCoord, zCoord, getBlockType());
		this.updateDirection(worldObj, xCoord, yCoord, zCoord);
	}

	@SideOnly(Side.CLIENT)
	public int getHoldingEEScaled(int par1) {
		return holdingEE * par1 / capacity;
	}

	public int getCapacity() {
		return capacity;
	}

	public int getHoldingEE() {
		return holdingEE;
	}

	public void setHoldingEE(int holdingEE) {
		this.holdingEE = holdingEE;
	}

}
