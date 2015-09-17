package nahamawiki.oef.block;

import nahamawiki.oef.tileentity.TileEntityEEConductor;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockEEConductor extends BlockEEMachineBase implements ITileEntityProvider {

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

}
