package nahamawiki.oef.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.core.OEFBlockCore;
import nahamawiki.oef.tileentity.TileEntityEEGenerator;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockEEGenerator extends BlockEEMachineBase {

	private IIcon[] iicon = new IIcon[8];

	public BlockEEGenerator() {
		super();
		this.setResistance(0.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityEEGenerator();
	}

	/** 周囲のブロックが更新された時の処理。 */
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityEEGenerator)
			((TileEntityEEGenerator) tileEntity).updateDirection(world, x, y, z);
	}

	/** ブロックが追加された時の処理。 */
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityEEGenerator)
			((TileEntityEEGenerator) tileEntity).updateDirection(world, x, y, z);
	}

	/** 爆破時にドロップするかどうかを返す。 */
	@Override
	public boolean canDropFromExplosion(Explosion explosion) {
		return false;
	}

	@Override
	public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
		world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) | 4, 2);
		world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
		this.onBlockDestroyedByExplosion(world, x, y, z, explosion);
	}

	/** ブロックが更新された時の処理。 */
	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		// 爆破から40tick後に呼ばれる。
		world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) & 3, 2);
		world.notifyBlocksOfNeighborChange(x, y, z, OEFBlockCore.EEGenerator);
	}

	@Override
	public int tickRate(World world) {
		return 41;
	}

	/** ブロックのテクスチャを登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		for (int i = 0; i < 8; i++) {
			this.iicon[i] = register.registerIcon(this.getTextureName() + "-" + i);
		}
	}

	/** ブロックのテクスチャを返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return iicon[meta & 7];
	}

}
