package nahamawiki.oef.tileentity;

import static net.minecraft.util.Facing.*;

import java.util.ArrayList;
import java.util.Random;

import nahamawiki.oef.util.OEFUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.world.World;

public class TileEntityEEItemTransporter extends TileEntityEEConductor implements ISidedInventory {

	/** 対応したスロットのアイテムを次に搬出する方向。 */
	protected byte[] nextSide = new byte[6];
	/** 対応したスロットのアイテムを次に搬出するまでの時間。 */
	protected int[] coolTime = new int[6];
	/** インベントリもちのTileEntityがある方向のリスト。 */
	protected ArrayList<Integer> recieverI = new ArrayList<Integer>();
	/** インベントリ内のアイテム。 */
	protected ItemStack[] itemStacks = new ItemStack[6];

	@Override
	public int getCapacity(int level) {
		return 8256;
	}

	@Override
	public void updateMachine() {
		super.updateMachine();
		this.sendItems();
	}

	@Override
	public void updateCreepered() {
		super.updateCreepered();
		Random random = new Random();
		if (this.getCanSpeedUp() && worldObj.loadedEntityList != null && random.nextInt(20) == 0) {
			try {
				for (Object entity : this.worldObj.loadedEntityList) {
					if (entity instanceof EntityMob) {
						if (((EntityMob) entity).getDistanceSq(xCoord, yCoord, zCoord) < 16 * 16) {
							if (!((EntityMob) entity).isPotionActive(Potion.moveSpeed)) {
								((EntityMob) entity).addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 600, 3));
							}
						}
					}
				}
			} catch (Exception e) {}
		}
	}

	/** アイテムを隣接インベントリへ搬出する。 */
	protected void sendItems() {
		// EEが足りないなら終了。
		if (holdingEE < 4)
			return;
		boolean flag;
		for (int i = 0; i < 6; i++) {
			if (coolTime[i] > 0)
				coolTime[i]--;
			if (coolTime[i] > 0)
				continue;
			flag = false;
			// recieverIをコピー。
			ArrayList<Integer> list = OEFUtil.copyList(recieverI);
			// 搬出先リストから受け取った方向を除外。
			if (list.contains(i))
				list.remove(list.indexOf(i));
			while (itemStacks[i] != null && list.size() > 0 && holdingEE > 3) {
				// 該当スロットが空でなく、搬出先があり、EEが足りているならループする。
				for (int j = 0; j < 6; j++) {
					// 該当スロットが空になっていたら終了。
					if (itemStacks[i] == null)
						break;
					// 搬出先リストに登録されていないか、次に搬出する方向でないなら、
					if (!list.contains(j) || j < nextSide[i]) {
						// 次に搬出する方向に搬出できなかったらリセットし、
						if (j >= 5)
							nextSide[i] = 0;
						// 次の方向へ。
						continue;
					}
					// 搬出先のインベントリを取得。
					IInventory iinventory = this.getIInventory(j);
					if (iinventory == null || this.isFullInventoryFromSide(iinventory, oppositeSide[j])) {
						// インベントリが存在しないか、いっぱいならリストから除外し、次の方向へ。
						list.remove(list.indexOf(j));
						continue;
					}
					// もとの状態をコピーしておく。
					ItemStack itemStack = itemStacks[i].copy();
					// ホッパーのメソッドを利用してアイテムを移動する。あまりが代入される。
					ItemStack itemStack1 = TileEntityHopper.func_145889_a(iinventory, itemStacks[i].splitStack(1), oppositeSide[j]);
					// スタック数が1未満になったらnullにする。
					if (itemStacks[i].stackSize < 1)
						itemStacks[i] = null;
					if (itemStack1 == null || itemStack1.stackSize == 0) {
						// 移動に成功したら、次の方向を設定し、EEを消費する。
						iinventory.markDirty();
						flag = true;
						nextSide[i] = (byte) (j + 1);
						if (nextSide[i] > 5)
							nextSide[i] = 0;
						holdingEE -= 4;
						if (holdingEE < 4)
							break;
						continue;
					}
					// 失敗したら、スロットをもとに戻し、リストから除外する。
					itemStacks[i] = itemStack;
					list.remove(list.indexOf(j));
					if (j >= 5)
						nextSide[i] = 0;
				}
			}
			// 搬出に成功したら、レベルに応じて間隔を設定する。
			if (flag) {
				switch (level) {
				case 0:
					coolTime[i] = 20;
					break;
				case 1:
					coolTime[i] = 10;
					break;
				case 2:
					coolTime[i] = 5;
					break;
				}
			}
		}
	}

	/** 指定された方向のインベントリを取得する。 */
	protected IInventory getIInventory(int side) {
		IInventory iinventory = null;
		TileEntity tileEntity = worldObj.getTileEntity(xCoord + offsetsXForSide[side], yCoord + offsetsYForSide[side], zCoord + offsetsZForSide[side]);
		if (tileEntity != null && tileEntity instanceof IInventory) {
			iinventory = (IInventory) tileEntity;
			if (iinventory instanceof TileEntityChest) {
				Block block = worldObj.getBlock(xCoord + offsetsXForSide[side], yCoord + offsetsYForSide[side], zCoord + offsetsZForSide[side]);
				// チェストならラージチェストのインベントリを取得。
				if (block instanceof BlockChest) {
					iinventory = ((BlockChest) block).func_149951_m(worldObj, xCoord + offsetsXForSide[side], yCoord + offsetsYForSide[side], zCoord + offsetsZForSide[side]);
				}
			}
		}
		return iinventory;
	}

	/** インベントリがいっぱいかどうか。 */
	protected boolean isFullInventoryFromSide(IInventory iinventory, int side) {
		if (iinventory instanceof ISidedInventory && side > -1) {
			ISidedInventory isidedinventory = (ISidedInventory) iinventory;
			int[] aint = isidedinventory.getAccessibleSlotsFromSide(side);
			for (int l = 0; l < aint.length; ++l) {
				ItemStack itemstack1 = isidedinventory.getStackInSlot(aint[l]);
				if (itemstack1 == null || itemstack1.stackSize != itemstack1.getMaxStackSize()) {
					return false;
				}
			}
		} else {
			int j = iinventory.getSizeInventory();
			for (int k = 0; k < j; ++k) {
				ItemStack itemstack = iinventory.getStackInSlot(k);
				if (itemstack == null || itemstack.stackSize != itemstack.getMaxStackSize()) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
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
		// ロスで256未満にならないようにする。
		if (loss > 0) {
			if (holdingEE >= 256 + loss)
				holdingEE -= loss;
		}
	}

	@Override
	protected void sendEE() {
		// 256 EEを残すようにオーバーライドする。
		if (reciever.size() < 1)
			return;
		ArrayList<Integer> list = OEFUtil.copyList(reciever);
		while (list.size() > 0 && (holdingEE - 256) / list.size() > 0) {
			int sendingEE = (holdingEE - 256) / list.size();
			holdingEE = (holdingEE - 256) % list.size();
			holdingEE += 256;
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

	protected boolean getCanSpeedUp() {
		return true;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setByteArray("NextSide", nextSide);
		nbt.setIntArray("CoolTime", coolTime);

		NBTTagCompound localnbt = new NBTTagCompound();
		for (int i = 0; i < recieverI.size(); i++) {
			localnbt.setInteger(String.valueOf(i), recieverI.get(i));
		}
		nbt.setByte("RecieverISize", (byte) recieverI.size());
		nbt.setTag("RecieverI", localnbt);

		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < itemStacks.length; ++i) {
			if (itemStacks[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				itemStacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		nbt.setTag("Items", nbttaglist);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		nextSide = nbt.getByteArray("NextSide");
		coolTime = nbt.getIntArray("CoolTime");

		recieverI.clear();
		NBTTagCompound localnbt = nbt.getCompoundTag("RecieverI");
		for (int i = 0; i < nbt.getByte("RecieverISize"); i++) {
			recieverI.add(localnbt.getInteger(String.valueOf(i)));
		}

		NBTTagList nbttaglist = nbt.getTagList("Items", 10);
		itemStacks = new ItemStack[this.getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;
			if (j >= 0 && j < itemStacks.length) {
				itemStacks[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@Override
	public void updateDirection(World world, int x, int y, int z) {
		if (!isUpdated) {
			reciever.clear();
			recieverI.clear();
			for (int i = 0; i < 6; i++) {
				isConnecting[i] = false;
				ITileEntityEEMachine machine = this.getNeighborMachine(i);
				if (machine != null) {
					int type = machine.getMachineType(oppositeSide[i]);
					if ((type & 2) == 2) {
						reciever.add(i);
					}
					isConnecting[i] = (type != 0);
				}
				TileEntity tileEntity = world.getTileEntity(x + offsetsXForSide[i], y + offsetsYForSide[i], z + offsetsZForSide[i]);
				if (tileEntity != null && tileEntity instanceof IInventory) {
					// インベントリもちならリストに登録。
					recieverI.add(i);
					isConnecting[i] = true;
				}
			}
			isUpdated = true;
			world.markBlockForUpdate(x, y, z);
		}
		this.updateTier();
	}

	/** インベントリのスロット数を返す。 */
	@Override
	public int getSizeInventory() {
		return 6;
	}

	/** 引数のスロットに入っているアイテムを返す。 */
	@Override
	public ItemStack getStackInSlot(int slot) {
		return itemStacks[slot];
	}

	/** 第一引数のスロットのアイテムを第二引数の数だけ減らす。 */
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (this.itemStacks[slot] != null) {
			ItemStack itemstack;

			if (this.itemStacks[slot].stackSize <= amount) {
				itemstack = this.itemStacks[slot];
				this.itemStacks[slot] = null;
				return itemstack;
			} else {
				itemstack = this.itemStacks[slot].splitStack(amount);

				if (this.itemStacks[slot].stackSize == 0) {
					this.itemStacks[slot] = null;
				}

				return itemstack;
			}
		} else {
			return null;
		}
	}

	/** 引数のスロットのアイテムを空にして返す。 */
	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (this.itemStacks[slot] != null) {
			ItemStack itemstack = this.itemStacks[slot];
			this.itemStacks[slot] = null;
			return itemstack;
		}
		return null;
	}

	/** 第一引数のスロットを第二引数のアイテムに設定する。 */
	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		this.itemStacks[slot] = itemStack;

		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
			itemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	/** インベントリ名を返す。 */
	@Override
	public String getInventoryName() {
		return "container.EEItemTransporter";
	}

	/** 名づけされた名前を持つかどうか。 */
	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	/** インベントリ内のアイテムのスタック数の上限を返す。 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	/** 第一引数のスロットに第二引数のアイテムが有効かどうか。 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return true;
	}

	/** 引数の方向から関与できるスロットの配列を返す。 */
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[] { side };
	}

	/** 搬入できるか。 */
	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
		return true;
	}

	/** 搬出できるか。 */
	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
		return false;
	}

}
