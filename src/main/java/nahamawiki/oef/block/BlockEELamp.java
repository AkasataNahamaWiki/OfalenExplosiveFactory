package nahamawiki.oef.block;

import java.util.Random;

import nahamawiki.oef.core.OEFBlockCore;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockEELamp extends BlockEEMachineBase {

	private final boolean flag;
	private static final String __OBFID = "CL_00000297";

	public BlockEELamp(boolean flag) {
		super();
		this.setResistance(1000000F);
		this.flag = flag;

		if (flag) {
			this.setLightLevel(1.0F);
		}
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		/*
		 * if (!world.isRemote)
		 * {
		 * if (this.flag && !(world.getBlock(x, y, z) instanceof
		 * BlockEEMachineBase) || (world.getBlock(x, y, z) instanceof
		 * BlockEEMachineBase && !((BlockEEMachineBase) world.getBlock(x, y,
		 * z)).canProvideEE()))
		 * {
		 * world.setBlock(x, y, z, OEFBlockCore.EELamp, 0, 2);
		 * }
		 * else if (!this.flag && (world.getBlock(x, y, z) instanceof
		 * BlockEEMachineBase && ((BlockEEMachineBase) world.getBlock(x, y,
		 * z)).canProvideEE()))
		 * {
		 * world.setBlock(x, y, z, OEFBlockCore.EELamp_on, 0, 2);
		 * }
		 * }
		 */
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		if (!world.isRemote) {
			/*
			 * if (this.flag && !(block instanceof BlockEEMachineBase))
			 * {
			 * world.setBlock(x, y, z, OEFBlockCore.EELamp, 0, 2);
			 * }
			 * else
			 */if (block instanceof BlockEEMachineBase && ((BlockEEMachineBase) block).canProvideEE()) {
				if (((BlockEEMachineBase) block).providingEE() > 0) {
					world.setBlock(x, y, z, OEFBlockCore.EELamp_on, 0, 2);
				} else {
					world.setBlock(x, y, z, OEFBlockCore.EELamp, 0, 2);
				}
			}
		}
	}

	@Override
	public Item getItemDropped(int meta, Random random, int fortune) {
		return Item.getItemFromBlock(OEFBlockCore.EELamp);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z) {
		return Item.getItemFromBlock(OEFBlockCore.EELamp);
	}

	@Override
	protected ItemStack createStackedBlock(int meta) {
		return new ItemStack(OEFBlockCore.EELamp);
	}

	@Override
	public int getLightValue(IBlockAccess iBlockAccess, int x, int y, int z) {
		if (this.flag) {
			return 15;
		}
		return 0;
	}

	@Override
	public boolean canProvideEE() {
		return false;
	}

	@Override
	public boolean canReciveEE() {
		return true;
	}

	@Override
	public int providingEE() {
		return 0;
	}

}
