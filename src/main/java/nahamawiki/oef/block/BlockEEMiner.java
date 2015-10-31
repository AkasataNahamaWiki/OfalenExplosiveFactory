package nahamawiki.oef.block;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.OEFCore;
import nahamawiki.oef.core.OEFItemCore;
import nahamawiki.oef.tileentity.TileEntityEEMiner;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockEEMiner extends BlockEEMachineBase {

	private Random random = new Random();
	private IIcon[] iicon = new IIcon[8];

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityEEMiner();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (player.getHeldItem() != null && player.getHeldItem().isItemEqual(OEFItemCore.EEPliers)) {
			if (world.isRemote)
				return true;
			TileEntityEEMiner miner = (TileEntityEEMiner) world.getTileEntity(x, y, z);
			miner.onAdjusted(player);
			return true;
		}
		player.openGui(OEFCore.instance, 1, world, x, y, z);
		return true;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		TileEntityEEMiner tileentity = (TileEntityEEMiner) world.getTileEntity(x, y, z);
		if (tileentity != null) {
			for (int i = 0; i < tileentity.getSizeInventory(); ++i) {
				ItemStack itemstack = tileentity.getStackInSlot(i);

				if (itemstack != null) {
					float f = this.random.nextFloat() * 0.6F + 0.1F;
					float f1 = this.random.nextFloat() * 0.6F + 0.1F;
					float f2 = this.random.nextFloat() * 0.6F + 0.1F;

					while (itemstack.stackSize > 0) {
						int j = this.random.nextInt(21) + 10;

						if (j > itemstack.stackSize) {
							j = itemstack.stackSize;
						}

						itemstack.stackSize -= j;
						EntityItem entityitem = new EntityItem(world, x + f, y + f1, z + f2, new ItemStack(itemstack.getItem(), j, itemstack.getItemDamage()));

						if (itemstack.hasTagCompound()) {
							entityitem.getEntityItem().setTagCompound(((NBTTagCompound) itemstack.getTagCompound().copy()));
						}

						float f3 = 0.025F;
						entityitem.motionX = (float) this.random.nextGaussian() * f3;
						entityitem.motionY = (float) this.random.nextGaussian() * f3 + 0.1F;
						entityitem.motionZ = (float) this.random.nextGaussian() * f3;
						world.spawnEntityInWorld(entityitem);
					}
				}
			}
			world.func_147453_f(x, y, z, block);
		}
		super.breakBlock(world, x, y, z, block, meta);
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
