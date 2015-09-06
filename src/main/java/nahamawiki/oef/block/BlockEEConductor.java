package nahamawiki.oef.block;

import nahamawiki.oef.tileentity.TileEntityEEConductor;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockEEConductor extends BlockEEMachineBase implements ITileEntityProvider {

	@Override
	public boolean canProvideEE() {
		return true;
	}

	@Override
	public boolean canReciveEE() {
		return true;
	}

	@Override
	public int providingEE(int meta) {
		return 0;
	}

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
		this.onNeighborBlockChange(world, x, y, z, this);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		super.breakBlock(world, x, y, z, block, meta);
		world.removeTileEntity(x, y, z);
	}

	@Override
	public boolean onBlockEventReceived(World world, int x, int y, int z, int eventID, int eventParameter) {
		super.onBlockEventReceived(world, x, y, z, eventID, eventParameter);
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity != null) {
			return tileEntity.receiveClientEvent(eventID, eventParameter);
		}
		return false;
	}

}
