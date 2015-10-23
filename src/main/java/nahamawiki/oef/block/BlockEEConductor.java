package nahamawiki.oef.block;

import nahamawiki.oef.tileentity.TileEntityEEConductor;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEEConductor extends BlockEEDuctBase {

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityEEConductor();
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityEEConductor)
			((TileEntityEEConductor) tileEntity).updateDirection(world, x, y, z);
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityEEConductor)
			((TileEntityEEConductor) tileEntity).updateDirection(world, x, y, z);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		float[] size = new float[6];
		TileEntityEEConductor conductor = (TileEntityEEConductor) world.getTileEntity(x, y, z);
		if (conductor != null && conductor.getConnectingArray() != null) {
			for (int i = 0; i < 6; i++) {
				if (conductor.getConnectingArray()[i]) {
					size[i] = 0.5F;
				} else {
					size[i] = 0.15F;
				}
			}
			this.setBlockBounds(0.5F - size[4], 0.5F - size[0], 0.5F - size[2], 0.5F + size[5], 0.5F + size[1], 0.5F + size[3]);
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		float[] size = new float[6];
		TileEntityEEConductor conductor = (TileEntityEEConductor) world.getTileEntity(x, y, z);
		if (conductor != null && conductor.getConnectingArray() != null) {
			for (int i = 0; i < 6; i++) {
				if (conductor.getConnectingArray()[i]) {
					size[i] = 0.5F;
				} else {
					size[i] = 0.15F;
				}
			}
			return AxisAlignedBB.getBoundingBox(x + 0.5 - size[4], y + 0.5 - size[0], z + 0.5 - size[2], x + 0.5 + size[5], y + 0.5 + size[1], z + 0.5 + size[3]);
		}
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

}
