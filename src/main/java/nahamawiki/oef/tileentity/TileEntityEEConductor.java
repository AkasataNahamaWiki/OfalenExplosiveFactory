package nahamawiki.oef.tileentity;

import static net.minecraft.util.Facing.*;

import java.util.ArrayList;
import java.util.Random;

import nahamawiki.oef.core.OEFConfigCore;
import nahamawiki.oef.util.OEFUtil;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class TileEntityEEConductor extends TileEntityEEMachineBase {

	/** 毎tick発生するロスの量。 */
	protected int loss = -1;
	/** EEの供給元からの距離。 */
	protected int tier = OEFConfigCore.maxTier;
	/** EEを受け取れる方向のリスト。 */
	protected ArrayList<Integer> reciever = new ArrayList<Integer>();
	/** その方向が接続されているか。 */
	protected boolean[] isConnecting = new boolean[6];
	/** 前のtickでEEを蓄えていたか。 */
	protected boolean isHoldingEE;
	/** そのtickでupdateDirectionを行ったかどうか */
	protected boolean isUpdated;

	@Override
	public int getMachineType(int side) {
		// 入出力可能。
		return 3;
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
	public int getTier(int side) {
		return tier;
	}

	@Override
	public void setTier(int tier, int side) {
		// 引数のtierが現時点でのtierより小さかったら代入。
		if (this.getTier(side) > tier)
			this.tier = tier;
	}

	@Override
	public int getCapacity(int level) {
		return 8000;
	}

	@Override
	public void updateMachine() {
		if (isUpdated)
			isUpdated = false;
		if (tier > OEFConfigCore.maxTier)
			tier = OEFConfigCore.maxTier;
		this.updateIsHoldingEE();
		this.decreaseEE();
		this.sendEE();
	}

	@Override
	public void updateCreepered() {
		if (isUpdated)
			isUpdated = false;
		Random random = new Random();
		if (random.nextInt(1200) == 0) {
			// 雷を落とす。
			Random x = new Random();
			int sx = MathHelper.getRandomIntegerInRange(x, -5, 5);
			Random y = new Random();
			int sy = 0;
			Random z = new Random();
			int sz = MathHelper.getRandomIntegerInRange(z, -5, 5);

			EntityLightningBolt bolt = new EntityLightningBolt(worldObj, xCoord + sx, yCoord + sy, zCoord + sz);
			worldObj.addWeatherEffect(bolt);
			worldObj.spawnEntityInWorld(bolt);
		}
		// 送信先の機械を匠化する。
		if (reciever.size() < 1)
			return;
		for (int i = 0; i < 6; i++) {
			if (!reciever.contains(i))
				continue;
			ITileEntityEEMachine machine = this.getNeighborMachine(i);
			if (machine == null || machine.getTier(oppositeSide[i]) < tier || machine.getCreeper())
				continue;
			machine.setCreeper(true);
		}
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

	/** EEをロスの分減らす。 */
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
		if (holdingEE == 0)
			return;
		holdingEE -= loss;
		if (holdingEE < 0)
			holdingEE = 0;
	}

	/** 隣接する機械にEEを送信する。 */
	protected void sendEE() {
		// 送信先がないなら終了。
		if (reciever.size() < 1)
			return;
		// 送信先リストをコピー。
		ArrayList<Integer> list = OEFUtil.copyList(reciever);
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
				if (machine == null || machine.getTier(oppositeSide[i]) < tier) {
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

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("Tier", tier);

		NBTTagCompound localnbt = new NBTTagCompound();
		for (int i = 0; i < reciever.size(); i++) {
			localnbt.setInteger(String.valueOf(i), reciever.get(i));
		}
		nbt.setInteger("RecieverSize", reciever.size());
		nbt.setTag("Reciever", localnbt);

		for (int i = 0; i < 6; i++) {
			nbt.setBoolean("IsConnecting-" + i, isConnecting[i]);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		tier = nbt.getInteger("Tier");

		reciever.clear();
		NBTTagCompound localnbt = nbt.getCompoundTag("Reciever");
		for (int i = 0; i < nbt.getInteger("RecieverSize"); i++) {
			reciever.add(localnbt.getInteger(String.valueOf(i)));
		}

		for (int i = 0; i < 6; i++) {
			isConnecting[i] = nbt.getBoolean("IsConnecting-" + i);
		}
	}

	@Override
	public Packet getDescriptionPacket() {
		// WorldクラスのmarkBlockForUpdateをすると呼ばれる。
		NBTTagCompound nbt = new NBTTagCompound();
		for (int i = 0; i < 6; i++) {
			nbt.setBoolean("IsConnecting-" + i, isConnecting[i]);
		}
		nbt.setBoolean("IsCreeper", isCreeper);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
	}

	/** パケットを受信した時の処理。 */
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		NBTTagCompound nbt = pkt.func_148857_g();
		for (int i = 0; i < 6; i++) {
			isConnecting[i] = nbt.getBoolean("IsConnecting-" + i);
		}
		isCreeper = nbt.getBoolean("IsCreeper");
	}

	/** 周囲のブロックを確認する */
	public void updateDirection(World world, int x, int y, int z) {
		if (!isUpdated) {
			// リストをリセット。
			reciever.clear();
			for (int i = 0; i < 6; i++) {
				// isConnectingもリセット。
				isConnecting[i] = false;
				ITileEntityEEMachine machine = this.getNeighborMachine(i);
				if (machine != null) {
					int type = machine.getMachineType(oppositeSide[i]);
					if ((type & 2) == 2) {
						// EE機械があり、受信可能なら登録。
						reciever.add(i);
					}
					// 送信/受信が可能ならisConnectingに登録。
					isConnecting[i] = (type != 0);
				}
			}
			isUpdated = true;
			world.markBlockForUpdate(x, y, z);
		}
		this.updateTier();
	}

	protected void updateTier() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		// もとのtierを保存。
		int lastTier = tier;
		// tierをリセット。
		tier = OEFConfigCore.maxTier;
		for (int i = 0; i < 6; i++) {
			ITileEntityEEMachine machine = this.getNeighborMachine(i);
			if (machine != null) {
				// EE機械があれば、tierを更新。
				this.setTier(machine.getTier(oppositeSide[i]) + 1, i);
			}
		}
		// tierが更新されていれば、周囲のブロックを更新。
		if (tier != lastTier) {
			this.notifyUpdateTier();
		}
	}

	protected void notifyUpdateTier() {
		for (int i = 0; i < 6; i++) {
			ITileEntityEEMachine machine = this.getNeighborMachine(i);
			if (machine != null && machine instanceof TileEntityEEConductor) {
				((TileEntityEEConductor) machine).updateTier();
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

	public boolean[] getConnectingArray() {
		return isConnecting;
	}

	public boolean isHoldingEE() {
		return (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & 4) != 0;
	}

}
