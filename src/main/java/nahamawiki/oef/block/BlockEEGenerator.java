package nahamawiki.oef.block;

import java.util.Random;

import nahamawiki.oef.core.OEFBlockCore;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEEGenerator extends BlockEEMachineBase {

	public BlockEEGenerator() {
		super();
		this.setResistance(0.0F);
		this.setStepSound(soundTypeMetal);
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
	public int providingEE(int meta) {
		if (meta > 3)
			return 100;
		return 0;
	}

	@Override
	public boolean canDropFromExplosion(Explosion explosion) {
		return false;
	}

	@Override
	public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
		world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
		this.onBlockDestroyedByExplosion(world, x, y, z, explosion);
	}

	@Override
	public int getLightValue(IBlockAccess iBlockAccess, int x, int y, int z) {
		if (iBlockAccess.getBlockMetadata(x, y, z) > 3) {
			return 15;
		} else {
			return 0;
		}
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random r) {
		world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		world.notifyBlocksOfNeighborChange(x, y, z, OEFBlockCore.EEGenerator);
	}

	@Override
	public int tickRate(World world) {
		return 40;
	}

}
