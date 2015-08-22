package nahamawiki.oef.block;

import java.util.Random;

import nahamawiki.oef.core.OEFBlockCore;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockEEGenerator extends BlockEEMachineBase {

	private IIcon[] iicon = new IIcon[4];

	public BlockEEGenerator() {
		super();
		this.setHardness(7.5F);
		this.setResistance(0.0F);
		this.setStepSound(soundTypeMetal);
		this.setTickRandomly(true);
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
    	world.setBlock(x, y, z, OEFBlockCore.EEGenerator_on ,0, 2);
    	world.scheduleBlockUpdate(x, y, z, OEFBlockCore.EEGenerator_on, this.tickRate(world));
    	world.notifyBlocksOfNeighborChange(x, y , z, OEFBlockCore.EEGenerator_on);
    	super.onBlockDestroyedByExplosion(world, x, y, z, explosion);
    }


	/**EE発生中は光源になるようにする*/
	@Override
	public int getLightValue(IBlockAccess iBlockAccess, int x, int y, int z) {
		if (iBlockAccess.getBlockMetadata(x, y, z) >= 8) {
			return 13;
		} else {
			return 0;
		}
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
    	if(world.getBlockMetadata(x, y, z) >= 8)
    	{
    		world.setBlockMetadataWithNotify(x, y, z, 0, 2);
    		this.onBlockPlacedBy(world, x, y, z, null, null);
    	}
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
		return 0;
	}
}
