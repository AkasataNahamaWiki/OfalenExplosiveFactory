package nahamawiki.oef.tileentity;

import net.minecraft.util.StatCollector;

public class TileEntityEELamp extends TileEntityEEMachineBase {

	protected boolean isShining;
	protected int remainingTime;

	@Override
	public int recieveEE(int amount, int side) {
		if (this.getCreeper())
			return 0;
		isShining = true;
		remainingTime = 5;
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 2);
		return amount;
	}

	@Override
	public String[] getState() {
		return new String[] {
				StatCollector.translateToLocal("info.EEMachineState.name") + StatCollector.translateToLocal(this.getBlockType().getLocalizedName()),
		};
	}

	@Override
	public byte getLevel(int meta) {
		return 0;
	}

	@Override
	public int getCapacity(int level) {
		return 0;
	}

	@Override
	public void updateMachine() {
		if (remainingTime > 0) {
			remainingTime--;
		} else if (isShining) {
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 2);
			isShining = false;
		}
	}

	@Override
	public void updateCreepered() {}

}
