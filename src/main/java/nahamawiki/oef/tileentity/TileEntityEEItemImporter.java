package nahamawiki.oef.tileentity;

import static net.minecraft.util.Facing.*;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.world.World;

public class TileEntityEEItemImporter extends TileEntityEEItemTransporter {

	/** インベントリもちのTileEntityがある方向のリスト。(伝導管以外) */
	protected ArrayList<Integer> inventory = new ArrayList<Integer>();

	@Override
	public void updateMachine() {
		super.updateMachine();
		this.importItems();
	}

	@Override
	protected IInventory getIInventory(int side) {
		// 逆流防止のためアイテム伝導管でなければnullを返すように上書き。伝導管の搬出処理で使用される。
		IInventory iinventory = null;
		TileEntity tileEntity = worldObj.getTileEntity(xCoord + offsetsXForSide[side], yCoord + offsetsYForSide[side], zCoord + offsetsZForSide[side]);
		if (tileEntity != null && tileEntity instanceof TileEntityEEItemTransporter) {
			iinventory = (IInventory) tileEntity;
		}
		return iinventory;
	}

	/** アイテムを隣接インベントリから搬入する。 */
	protected void importItems() {
		// EEが足りないなら終了。
		if (holdingEE < 4)
			return;
		for (int i = 0; i < 6; i++) {
			// 搬入するスロットがいっぱいなら次の方向へ。
			if (itemStacks[i] != null && itemStacks[i].stackSize >= itemStacks[i].getMaxStackSize())
				continue;
			// 上書きしているから伝導管のものを呼び出し。
			IInventory iinventory = super.getIInventory(i);
			if (iinventory == null)
				continue;
			if (iinventory instanceof ISidedInventory) {
				// ISidedInventoryなら取り出し可能なスロットを絞る。
				ISidedInventory isidedinventory = (ISidedInventory) iinventory;
				int[] aint = isidedinventory.getAccessibleSlotsFromSide(oppositeSide[i]);
				for (int slot = 0; slot < aint.length; slot++) {
					ItemStack itemStack = isidedinventory.getStackInSlot(slot);
					if (!isidedinventory.canExtractItem(slot, itemStack, oppositeSide[i]))
						continue;
					this.importSlotContents(iinventory, slot, i);
					// EEが足りなくなったら終了。
					if (holdingEE < 4)
						return;
				}
			} else {
				for (int slot = 0; slot < iinventory.getSizeInventory(); slot++) {
					this.importSlotContents(iinventory, slot, i);
					// EEが足りなくなったら終了。
					if (holdingEE < 4)
						return;
				}
			}
		}
	}

	/** 指定されたスロットのアイテムを搬入する。 */
	protected void importSlotContents(IInventory iinventory, int slot, int side) {
		// 空なら終了。
		ItemStack itemStack = iinventory.getStackInSlot(slot);
		if (itemStack == null)
			return;
		// 空でない限り続ける。
		while (iinventory.getStackInSlot(slot) != null) {
			// もとの状態をコピーしておく。
			ItemStack itemStack1 = iinventory.getStackInSlot(slot).copy();
			// ホッパーのメソッドを利用してアイテムを移動する。あまりが代入される。
			ItemStack itemStack2 = TileEntityHopper.func_145889_a(this, iinventory.decrStackSize(slot, 1), side);
			if (itemStack2 == null || itemStack2.stackSize < 1) {
				// 移動に成功したなら、EEを消費する。
				holdingEE -= 4;
				// EEが足りなくなったら終了。
				if (holdingEE < 4)
					break;
			} else {
				// 移動に失敗したら、搬入元のスロットをもとの状態に戻して終了。
				iinventory.setInventorySlotContents(slot, itemStack1);
				break;
			}
		}
		iinventory.markDirty();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		NBTTagCompound localnbt = new NBTTagCompound();
		for (int i = 0; i < inventory.size(); i++) {
			localnbt.setInteger(String.valueOf(i), inventory.get(i));
		}
		nbt.setByte("inventorySize", (byte) recieverI.size());
		nbt.setTag("inventory", localnbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		inventory.clear();
		NBTTagCompound localnbt = nbt.getCompoundTag("inventory");
		for (int i = 0; i < nbt.getByte("inventorySize"); i++) {
			inventory.add(localnbt.getInteger(String.valueOf(i)));
		}
	}

	@Override
	public void updateDirection(World world, int x, int y, int z) {
		if (!isUpdated) {
			reciever.clear();
			recieverI.clear();
			inventory.clear();
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
					// インベントリもちで、伝導管以外なら搬入元リストに登録。
					if (!(tileEntity instanceof TileEntityEEItemTransporter)) {
						inventory.add(i);
					}
					recieverI.add(i);
					isConnecting[i] = true;
				}
			}
			isUpdated = true;
			world.markBlockForUpdate(x, y, z);
		}
		this.updateTier();
	}

	@Override
	public String getInventoryName() {
		return "container.EEItemImporter";
	}
	
	@Override
	public void updateCreepered() {
		Random rand = new Random();
		if(this.worldObj.loadedEntityList != null && rand.nextInt(20) == 0)
		{
			try
			{
				for(Object entity : this.worldObj.loadedEntityList)
				{
					if(entity instanceof EntityMob)
					{
						EntityMob mob = (EntityMob)entity;
						mob.getMoveHelper().setMoveTo(xCoord, yCoord, zCoord, mob.getMoveHelper().getSpeed());
						mob.getNavigator().tryMoveToXYZ(xCoord, yCoord, zCoord, mob.getMoveHelper().getSpeed());
					}
				}
			}
			catch(Exception e){}
		}
	}

}
