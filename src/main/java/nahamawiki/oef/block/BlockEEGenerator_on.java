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

	@Override
	public boolean canDropFromExplosion(Explosion explosion) {
		return false;
	}

	@Override
	public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
		world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
		super.onBlockDestroyedByExplosion(world, x, y, z, explosion);
	}

	@Override
	public int getLightValue(IBlockAccess iBlockAccess, int x, int y, int z) {
		return 9;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		int l = 0;
		if (entity != null) {
			l = MathHelper.floor_double(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
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

	@Override
	public void updateTick(World world, int x, int y, int z, Random r) {
		world.setBlock(x, y, z, OEFBlockCore.EEGenerator);
		world.notifyBlocksOfNeighborChange(x, y, z, OEFBlockCore.EEGenerator);
	}

	@Override
	public int tickRate(World world) {
		return 30;
	}

	@Override
	public int providingEE() {
		return 30;
	}

}
