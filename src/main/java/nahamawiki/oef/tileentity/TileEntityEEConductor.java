package nahamawiki.oef.tileentity;

import nahamawiki.oef.block.BlockEEMachineBase;
import net.minecraft.block.Block;
import net.minecraft.util.Facing;
import net.minecraft.world.World;

public class TileEntityEEConductor extends TileEntityEEMachineBase {

	protected int[] conectingDirection;
	protected int providerNum;
	protected int reciverNum;

	public TileEntityEEConductor() {
		super();
		conectingDirection = new int[6];
		providerNum = 0;
		reciverNum = 0;
	}

	@Override
	public void reciveEE(int amount) {
		holdingEE += amount;
	}

	@Override
	public void updateEntity() {
		int sendEE = holdingEE / reciverNum;
		holdingEE %= reciverNum;
		for (int i = 0; i <= 6; i++) {
			if (conectingDirection[i] == 1) {
				TileEntityEEMachineBase machine = (TileEntityEEMachineBase) worldObj.getTileEntity(xCoord + Facing.offsetsXForSide[i], yCoord + Facing.offsetsYForSide[i], zCoord + Facing.offsetsZForSide[i]);
				machine.reciveEE(sendEE);
			}
		}
	}

	/** 周囲のブロックを確認する */
	public void updateDirection(World world, int x, int y, int z) {
		providerNum = 0;
		reciverNum = 0;
		for (int i = 0; i <= 6; i++) {
			Block block = world.getBlock(x + Facing.offsetsXForSide[i], y + Facing.offsetsYForSide[i], z + Facing.offsetsZForSide[i]);
			if (block instanceof BlockEEMachineBase) {
				BlockEEMachineBase machine = (BlockEEMachineBase) block;
				if (machine.canReciveEE()) {
					conectingDirection[i] = 1;
					reciverNum += 1;
				} else if (machine.canProvideEE()) {
					conectingDirection[i] = 2;
					providerNum += 1;
				} else {
					conectingDirection[i] = 0;
				}
			} else {
				conectingDirection[i] = 0;
			}
		}
	}

}
