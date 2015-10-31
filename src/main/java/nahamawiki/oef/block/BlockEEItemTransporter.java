package nahamawiki.oef.block;

import java.util.Random;

import nahamawiki.oef.tileentity.TileEntityEEItemTransporter;
import nahamawiki.oef.tileentity.TileEntityEEItemTransporter;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockEEItemTransporter extends BlockEEConductor {

	protected Random random = new Random();

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityEEItemTransporter();
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		TileEntityEEItemTransporter tileentity = (TileEntityEEItemTransporter) world.getTileEntity(x, y, z);
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

}
