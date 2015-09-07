package nahamawiki.oef.block;

import java.util.List;
import java.util.Random;

import nahamawiki.oef.core.OEFBlockCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockEEGenerator extends BlockEEMachineBase {

	private IIcon[] iicon = new IIcon[8];

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
		switch (meta - 4) {
		case 0:
			return 100;
		case 1:
			return 200;
		case 2:
			return 400;
		case 3:
			return 800;
		}
		return 0;
	}

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

	@Override
	public int getLightValue(IBlockAccess iBlockAccess, int x, int y, int z) {
		if (iBlockAccess.getBlockMetadata(x, y, z) > 3) {
			return 15;
		} else {
			return 0;
		}
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) & 3, 2);
		world.notifyBlocksOfNeighborChange(x, y, z, OEFBlockCore.EEGenerator);
	}

	@Override
	public int tickRate(World world) {
		return 40;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		for (int i = 0; i < 8; i++) {
			this.iicon[i] = register.registerIcon(this.getTextureName() + "-" + i);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return iicon[meta & 7];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < 4; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public int damageDropped(int meta) {
		return meta & 3;
	}

}
