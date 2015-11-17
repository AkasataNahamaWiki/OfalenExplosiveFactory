package nahamawiki.oef.block;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.tileentity.TileEntityEESupplier;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockEESupplier extends BlockEEMachineBase {

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityEESupplier();
	}

	/** 周囲のブロックが更新された時の処理。 */
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityEESupplier)
			((TileEntityEESupplier) tileEntity).updateDirection(world, x, y, z);
	}

	/** ブロックが追加された時の処理。 */
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityEESupplier)
			((TileEntityEESupplier) tileEntity).updateDirection(world, x, y, z);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
		list.add(new ItemStack(item, 1, 0));
	}

	@Override
	public int damageDropped(int meta) {
		return 0;
	}

}
