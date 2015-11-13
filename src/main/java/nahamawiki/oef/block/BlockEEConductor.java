package nahamawiki.oef.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.tileentity.TileEntityEEConductor;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEEConductor extends BlockEEMachineBase {

	protected IIcon[] iicon = new IIcon[4];

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityEEConductor();
	}

	/** 周囲のブロックが更新された時の処理。 */
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity != null && tileEntity instanceof TileEntityEEConductor)
			((TileEntityEEConductor) tileEntity).updateDirection(world, x, y, z);
	}

	/** ブロックが追加された時の処理。 */
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity != null && tileEntity instanceof TileEntityEEConductor)
			((TileEntityEEConductor) tileEntity).updateDirection(world, x, y, z);
	}

	/** ブロックの当たり判定を設定する。 */
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity != null && tileEntity instanceof TileEntityEEConductor) {
			float[] size = new float[6];
			TileEntityEEConductor conductor = (TileEntityEEConductor) tileEntity;
			if (conductor.getConnectingArray() != null) {
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
	}

	/** ブロックの当たり判定を返す。 */
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity != null && tileEntity instanceof TileEntityEEConductor) {
			float[] size = new float[6];
			TileEntityEEConductor conductor = (TileEntityEEConductor) tileEntity;
			if (conductor.getConnectingArray() != null) {
				for (int i = 0; i < 6; i++) {
					if (conductor.getConnectingArray()[i]) {
						size[i] = 0.5F;
					} else {
						size[i] = 0.15F;
					}
				}
				return AxisAlignedBB.getBoundingBox(x + 0.5 - size[4], y + 0.5 - size[0], z + 0.5 - size[2], x + 0.5 + size[5], y + 0.5 + size[1], z + 0.5 + size[3]);
			}
		}
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	/** カーソルを合わせたときの枠線の形を返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		return this.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	/** ブロックのテクスチャを登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		for (int i = 0; i < 4; i++) {
			this.iicon[i] = register.registerIcon(this.getTextureName() + "-" + i);
		}
	}

	/** ブロックのテクスチャを返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return iicon[meta & 3];
	}

}
