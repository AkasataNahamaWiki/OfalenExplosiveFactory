package nahamawiki.oef.block;

import java.util.Random;

import nahamawiki.oef.core.OEFBlockCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class BlockEEGenerator_on extends BlockEEMachineBase {

	private IIcon[] iicon = new IIcon[4];

	public BlockEEGenerator_on() {
		super();
		this.setHardness(7.5F);
		this.setResistance(0.0F);
		this.setStepSound(soundTypeMetal);
		this.setTickRandomly(true);
		this.setCreativeTab(null);
	}

	@Override
	public boolean canProvideEE() {
		return true;
	}

	@Override
	public boolean canReciveEE() {
		return false;
	}

    /**
     * Return whether this block can drop from an explosion.
     */
    public boolean canDropFromExplosion(Explosion p_149659_1_)
    {
        return false;
    }

    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion)
    {
    	world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
    	super.onBlockDestroyedByExplosion(world, x, y, z, explosion);
    }

    /**メタデータにより返すIIconを変える*/
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if (meta < 8) {
			return side == meta ? this.iicon[2] : this.iicon[0];
		} else {
			return side == (meta & 7) ? this.iicon[3] : this.iicon[1];
		}
	}

	/**メタデータ違いのテクスチャを登録する*/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iicon) {
		for (int i = 0; i  < 4; i ++) {
			this.iicon[i] = iicon.registerIcon(this.getTextureName() + "-" + i);
		}

		/**イカテスト用*/
		{
			this.iicon[2] = iicon.registerIcon("creepermod:creeperbomb");
			this.iicon[0] = iicon.registerIcon("creepermod:creeperbomb");
		}
	}

	/**EE発生中は光源になるようにする*/
	@Override
	public int getLightValue(IBlockAccess iBlockAccess, int x, int y, int z) {
		return 0;
	}

	/**ブロックが設置された時の処理*/
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		int l = 0;
		if(entity != null)
		{
			l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		}
		if (l == 0) {
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		}

		if (l == 1) {
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		}

		if (l == 2) {
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		}

		if (l == 3) {
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		}
	}

	   /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World world, int x, int y, int z, Random r)
    {
    	world.setBlock(x, y, z, OEFBlockCore.EEGenerator);
    	world.notifyBlocksOfNeighborChange(x, y, z, OEFBlockCore.EEGenerator);
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World p_149738_1_)
    {
        return 30;
    }

	@Override
	public int providingEE() {
		return 30;
	}
}
