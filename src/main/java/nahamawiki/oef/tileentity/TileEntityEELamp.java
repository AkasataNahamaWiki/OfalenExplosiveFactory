package nahamawiki.oef.tileentity;

import net.minecraft.util.StatCollector;

public class TileEntityEELamp extends TileEntityEEMachineBase {

	protected boolean isShining;
	protected int remainingTime;

	@Override
	public int getMachineType(int side) {
		return 2;
	}

	@Override
	public int recieveEE(int amount, int side) {
		isShining = true;
		remainingTime = 5;
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 2);
		return amount;
	}

	@Override
	public String[] getState() {
		return new String[] {
				StatCollector.translateToLocal("info.EEMachineState.name") + StatCollector.translateToLocal(this.getBlockType().getLocalizedName()),
				StatCollector.translateToLocal("info.EEMachineState.level") + this.getLevel(this.getBlockMetadata()),
		};
	}

	@Override
	public int getLevel(int meta) {
		return 0;
	}

	@Override
	public void updateEntity() {
		if (remainingTime > 0) {
			remainingTime--;
		} else if (isShining) {
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 2);
			isShining = false;
		}
	}

}
