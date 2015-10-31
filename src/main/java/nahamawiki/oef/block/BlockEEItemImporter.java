package nahamawiki.oef.block;

import nahamawiki.oef.tileentity.TileEntityEEItemImporter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockEEItemImporter extends BlockEEItemTransporter {

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityEEItemImporter();
	}

}
