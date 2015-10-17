package nahamawiki.oef.tileentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.block.BlockEESurveyor;
import nahamawiki.oef.core.OEFBlockCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.StatCollector;

public class TileEntityEEMiner extends TileEntityEEMachineBase implements IInventory {

	private final int capacity = 8000;
	protected boolean isChecked;
	protected boolean isMining;
	protected boolean isFinished;
	protected int coolTime;
	protected int[] miningCoord = new int[3];
	protected int[] miningRange = new int[2];
	protected ItemStack[] itemStacks = new ItemStack[54];
	protected boolean isSpawning;

	@Override
	public int getMachineType(int side) {
		return 2;
	}

	@Override
	public int recieveEE(int amount, int side) {
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
		if (!isMining && !isFinished)
			this.setMiningArea();
		isSpawning = !isSpawning;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);;
		return new String[] {
				StatCollector.translateToLocal("info.EEMachineState.name") + StatCollector.translateToLocal(this.getBlockType().getLocalizedName()),
				StatCollector.translateToLocal("info.EEMachineState.level") + level,
				StatCollector.translateToLocal("info.EEMachineState.capacity") + capacity + " EE",
				StatCollector.translateToLocal("info.EEMachineState.holding") + holdingEE + " EE",
				StatCollector.translateToLocal("info.EEMachineState.mining") + isMining,
		};
	}

	@Override
	public byte getLevel(int meta) {
		return (byte) (meta & 3);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (worldObj.isRemote) {
			if (coolTime > 0)
				coolTime--;
			if (isSpawning && coolTime < 1) {
				this.spawnParticles();
				coolTime = 20;
			}
			return;
		}
		// 置かれた時に範囲を設定する。
		if (!isChecked) {
			this.setMiningArea();
			isChecked = true;
		}
		// 次の採掘までの残り時間を減らす。
		if (coolTime > 0)
			coolTime--;
		// 採掘ができる条件になっていないなら終了。
		if (coolTime > 0 || holdingEE < 1600 || !isMining || !this.canMine())
			return;
		if (this.getNextBlock()) {
			// 次に採掘するブロックの取得に成功したら、採掘する。
			this.mineBlock();
			// EEを消費し、次の採掘までの時間を設定する。
			holdingEE -= 1600;
			switch (level) {
			case 0:
				coolTime = 80;
				break;
			case 1:
				coolTime = 40;
				break;
			case 2:
				coolTime = 20;
				break;
			case 3:
				coolTime = 10;
			}
		} else {
			// 採掘するブロックがなくなったら、停止する。
			isMining = false;
			isFinished = true;
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, level, 2);
		}
	}

	/** 測量機を探して、採掘範囲を設定する。 */
	private void setMiningArea() {
		boolean flagx = false;
		boolean flagz = false;
		for (int ix = 1; ix < 256; ix++) {
			Block block = worldObj.getBlock(xCoord + ix, yCoord, zCoord);
			if (block instanceof BlockEESurveyor) {
				int meta = worldObj.getBlockMetadata(xCoord + ix, yCoord, zCoord);
				if (meta == 4) {
					miningRange[0] = ix;
					flagx = true;
					break;
				}
			}
			block = worldObj.getBlock(xCoord - ix, yCoord, zCoord);
			if (block instanceof BlockEESurveyor) {
				int meta = worldObj.getBlockMetadata(xCoord - ix, yCoord, zCoord);
				if (meta == 5) {
					miningRange[0] = -ix;
					flagx = true;
					break;
				}
			}
		}
		for (int iz = 1; iz < 256; iz++) {
			Block block = worldObj.getBlock(xCoord, yCoord, zCoord + iz);
			if (block instanceof BlockEESurveyor) {
				int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord + iz);
				if (meta == 2) {
					miningRange[1] = iz;
					flagz = true;
					break;
				}
			}
			block = worldObj.getBlock(xCoord, yCoord, zCoord - iz);
			if (block instanceof BlockEESurveyor) {
				int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord - iz);
				if (meta == 3) {
					miningRange[1] = -iz;
					flagz = true;
					break;
				}
			}
		}
		if (flagx && flagz) {
			miningCoord[0] = miningRange[0] < 0 ? xCoord : xCoord + miningRange[0];
			miningCoord[1] = yCoord + 1;
			miningCoord[2] = miningRange[1] < 0 ? zCoord - 1 : zCoord + miningRange[1] - 1;
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, level | 4, 2);
			this.fillLiquid();
			isMining = true;
		}
	}

	/** 次に採掘するブロックを決める。 */
	private boolean getNextBlock() {
		Block block = null;
		while (block == null || block == Blocks.air || block.getBlockHardness(worldObj, miningCoord[0], miningCoord[1], miningCoord[2]) < 0) {
			miningCoord[0]--;
			if (!this.isInArea()) {
				miningCoord[0] = miningRange[0] < 0 ? xCoord - 1 : xCoord + miningRange[0] - 1;
				miningCoord[2]--;
				if (!this.isInArea()) {
					miningCoord[2] = miningRange[1] < 0 ? zCoord - 1 : zCoord + miningRange[1] - 1;
					miningCoord[1]--;
					this.fillLiquid();
					if (!this.isInArea()) {
						miningCoord[1] = yCoord + 1;
						return false;
					}
				}
			}
			block = worldObj.getBlock(miningCoord[0], miningCoord[1], miningCoord[2]);
			if (block instanceof BlockLiquid) {
				worldObj.setBlockToAir(miningCoord[0], miningCoord[1], miningCoord[2]);
				block = null;
			}
		}
		return true;
	}

	/** y座標が移るときに液体を削除する。 */
	private void fillLiquid() {
		boolean flagx = miningRange[0] < 0;
		boolean flagz = miningRange[1] < 0;
		for (int ix = 0; ix <= Math.abs(miningRange[0]); ix++) {
			for (int iz = 0; iz <= Math.abs(miningRange[1]); iz++) {
				int jx = xCoord + (flagx ? -ix : ix);
				int jz = zCoord + (flagz ? -iz : iz);
				if (worldObj.getBlock(jx, miningCoord[1], jz) instanceof BlockLiquid) {
					if (this.isInArea(jx, miningCoord[1], jz)) {
						worldObj.setBlockToAir(jx, miningCoord[1], jz);
					} else {
						worldObj.setBlock(jx, miningCoord[1], jz, OEFBlockCore.EESwordWall);
					}
				}
			}
		}
	}

	/** インベントリがいっぱいならfalseを返す。 */
	private boolean canMine() {
		boolean flag = false;
		for (int i = 0; i < 54; i++) {
			if (itemStacks[i] == null) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/** miningCoordの座標のブロックを採掘する処理。 */
	private void mineBlock() {
		Block block = worldObj.getBlock(miningCoord[0], miningCoord[1], miningCoord[2]);
		int meta = worldObj.getBlockMetadata(miningCoord[0], miningCoord[1], miningCoord[2]);
		ArrayList<ItemStack> drops = block.getDrops(worldObj, miningCoord[0], miningCoord[1], miningCoord[2], meta, 0);
		Iterator<ItemStack> iterator = drops.iterator();
		while (iterator.hasNext()) {
			this.storeItemStack(iterator.next());
		}
		block.breakBlock(worldObj, miningCoord[0], miningCoord[1], miningCoord[2], block, meta);
		worldObj.setBlockToAir(miningCoord[0], miningCoord[1], miningCoord[2]);

		List list = worldObj.loadedEntityList;
		if (list.isEmpty()) {
			return;
		}
		for (Object entity : list) {
			if (!(entity instanceof EntityItem)) {
				continue;
			}
			EntityItem item = (EntityItem) entity;
			if (this.isInArea(item.posX, item.posY, item.posZ)) {
				this.storeItemStack(item.getEntityItem());
				item.setDead();
			}
		}
		worldObj.createExplosion(null, miningCoord[0], miningCoord[1], miningCoord[2], 3.0F, false);
		// OEFCore.logger.info("complete mineBlock");
	}

	/**
	 * 引数をインベントリに収納する。
	 *
	 * @return 成功したか
	 */
	private boolean storeItemStack(ItemStack itemStack) {
		for (int i = 0; i < 54 && itemStack != null; i++) {
			if (itemStacks[i] == null) {
				itemStacks[i] = itemStack;
				itemStack = null;
			} else if (itemStacks[i].isItemEqual(itemStack) && itemStacks[i].stackSize < itemStacks[i].getMaxStackSize()) {
				itemStacks[i].stackSize += itemStack.stackSize;
				if (itemStacks[i].stackSize > itemStacks[i].getMaxStackSize()) {
					itemStack.stackSize = itemStacks[i].stackSize - itemStacks[i].getMaxStackSize();
					itemStacks[i].stackSize = itemStacks[i].getMaxStackSize();
				} else {
					itemStack = null;
				}
			}
		}
		if (itemStack != null) {
			this.spawnEntityItem(itemStack);
			return false;
		}
		return true;
	}

	/** 引数をこのブロックの真上にEntityItemとしてスポーンさせる。 */
	private void spawnEntityItem(ItemStack itemStack) {
		EntityItem entity = new EntityItem(worldObj, xCoord - 0.5, yCoord + 1, zCoord - 0.5, itemStack);
		if (itemStack.hasTagCompound()) {
			entity.getEntityItem().setTagCompound(((NBTTagCompound) itemStack.getTagCompound().copy()));
		}
	}

	/** miningCoordが採掘範囲に含まれているか。 */
	private boolean isInArea() {
		return this.isInArea(miningCoord[0], miningCoord[1], miningCoord[2]);
	}

	private boolean isInArea(int x, int y, int z) {
		return this.isInArea(x + 0.5, y, z + 0.5);
	}

	/** 引数の座標が採掘範囲に含まれているか。 */
	private boolean isInArea(double x, double y, double z) {
		if (!isMining)
			return false;
		x -= 0.5;
		z -= 0.5;
		if ((xCoord < x && x < xCoord + miningRange[0]) || (xCoord + miningRange[0] < x && x < xCoord)) {
			if ((zCoord < z && z < zCoord + miningRange[1]) || (zCoord + miningRange[1] < z && z < zCoord)) {
				if (0 < y && y <= yCoord) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("isChecked", isChecked);
		nbt.setBoolean("isMining", isMining);
		nbt.setBoolean("isFinished", isFinished);
		nbt.setInteger("coolTime", coolTime);
		nbt.setIntArray("miningCoord", miningCoord);
		nbt.setIntArray("miningRange", miningRange);
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
		isChecked = nbt.getBoolean("isChecked");
		isMining = nbt.getBoolean("isMining");
		isFinished = nbt.getBoolean("isFinished");
		coolTime = nbt.getInteger("coolTime");
		miningCoord = nbt.getIntArray("miningCoord");
		miningRange = nbt.getIntArray("miningRange");
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

	@SideOnly(Side.CLIENT)
	public int getHoldingEEScaled(int par1) {
		return holdingEE * par1 / capacity;
	}

	public int getHoldingEE() {
		return holdingEE;
	}

	public void setHoldingEE(int holdingEE) {
		this.holdingEE = holdingEE;
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("isSpawning", isSpawning);
		nbt.setBoolean("isMining", isMining);
		nbt.setIntArray("miningRange", miningRange);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		NBTTagCompound nbt = pkt.func_148857_g();
		isSpawning = nbt.getBoolean("isSpawning");
		isMining = nbt.getBoolean("isMining");
		miningRange = nbt.getIntArray("miningRange");
		this.spawnParticles();
	}

	public void spawnParticles() {
		// OEFCore.logger.info("Start spawnParticles");
		if (isMining) {
			// OEFCore.logger.info("isMining = true");
			boolean flagx = miningRange[0] < 0;
			boolean flagz = miningRange[1] < 0;
			for (int ix = 0; ix <= Math.abs(miningRange[0]); ix++) {
				int jx = xCoord + (flagx ? -ix : ix);
				worldObj.spawnParticle("reddust", jx + 0.5, yCoord + 0.5, zCoord + 0.5, 0.125D, 1.0D, 0.25D);
			}
			for (int iz = 0; iz <= Math.abs(miningRange[1]); iz++) {
				int jz = zCoord + (flagz ? -iz : iz);
				worldObj.spawnParticle("reddust", xCoord + 0.5, yCoord + 0.5, jz + 0.5, 0.125D, 1.0D, 0.25D);
			}
		} else {
			for (int ix = 1; ix < 256; ix++) {
				worldObj.spawnParticle("reddust", xCoord + ix + 0.5, yCoord + 0.5, zCoord + 0.5, 0.125D, 1.0D, 0.25D);
				worldObj.spawnParticle("reddust", xCoord - ix + 0.5, yCoord + 0.5, zCoord + 0.5, 0.125D, 1.0D, 0.25D);
			}
			for (int iz = 1; iz < 256; iz++) {
				worldObj.spawnParticle("reddust", xCoord + 0.5, yCoord + 0.5, zCoord + iz + 0.5, 0.125D, 1.0D, 0.25D);
				worldObj.spawnParticle("reddust", xCoord + 0.5, yCoord + 0.5, zCoord - iz + 0.5, 0.125D, 1.0D, 0.25D);
			}
		}
	}

	@Override
	public int getSizeInventory() {
		return 54;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot < 54 && itemStacks[slot] != null)
			return itemStacks[slot];
		return null;
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (slot < 54 && itemStacks[slot] != null) {
			ItemStack itemStack;
			if (itemStacks[slot].stackSize <= amount) {
				itemStack = itemStacks[slot];
				itemStacks[slot] = null;
				return itemStack;
			}
			itemStack = itemStacks[slot].splitStack(amount);
			if (itemStacks[slot].stackSize == 0) {
				itemStacks[slot] = null;
			}
			return itemStack;
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (slot < 54 && itemStacks[slot] != null) {
			ItemStack itemStack = itemStacks[slot];
			itemStacks[slot] = null;
			return itemStack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		if (slot < 54) {
			itemStacks[slot] = itemStack;
			if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
				itemStack.stackSize = this.getInventoryStackLimit();
			}
		}
	}

	@Override
	public String getInventoryName() {
		return "container.EEMiner";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

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

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		if (slot < 54)
			return true;
		return false;
	}

}
