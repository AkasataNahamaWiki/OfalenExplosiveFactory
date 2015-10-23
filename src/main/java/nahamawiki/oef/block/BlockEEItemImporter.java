package nahamawiki.oef.block;

import java.util.Random;

import nahamawiki.oef.tileentity.TileEntityEEItemImporter;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEEItemImporter extends BlockEEDuctBase {

	protected Random random = new Random();

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityEEItemImporter();
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityEEItemImporter)
			((TileEntityEEItemImporter) tileEntity).updateDirection(world, x, y, z);
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityEEItemImporter)
			((TileEntityEEItemImporter) tileEntity).updateDirection(world, x, y, z);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		TileEntityEEItemImporter tileentity = (TileEntityEEItemImporter) world.getTileEntity(x, y, z);
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
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		float[] size = new float[6];
		TileEntityEEItemImporter conductor = (TileEntityEEItemImporter) world.getTileEntity(x, y, z);
		if (conductor != null && conductor.getConnectingArray() != null) {
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

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		float[] size = new float[6];
		TileEntityEEItemImporter conductor = (TileEntityEEItemImporter) world.getTileEntity(x, y, z);
		if (conductor != null && conductor.getConnectingArray() != null) {
			for (int i = 0; i < 6; i++) {
				if (conductor.getConnectingArray()[i]) {
					size[i] = 0.5F;
				} else {
					size[i] = 0.15F;
				}
			}
			return AxisAlignedBB.getBoundingBox(x + 0.5 - size[4], y + 0.5 - size[0], z + 0.5 - size[2], x + 0.5 + size[5], y + 0.5 + size[1], z + 0.5 + size[3]);
		}
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

}
