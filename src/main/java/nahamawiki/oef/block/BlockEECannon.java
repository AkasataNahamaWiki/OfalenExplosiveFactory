package nahamawiki.oef.block;

import java.util.Random;

import nahama.ofalenmod.core.OfalenModItemCore;
import nahamawiki.oef.tileentity.TileEntityEECannon;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockEECannon extends BlockEEMachineBase {

	private Random random = new Random();

	public BlockEECannon() {
		super();
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityEECannon();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float posX, float posY, float posZ) {
		if (!world.isRemote && player.getHeldItem() != null && world.getTileEntity(x, y, z) != null) {
			TileEntityEECannon tile = (TileEntityEECannon) world.getTileEntity(x, y, z);
			tile.setOwnPlayer(player.getDisplayName());
			ItemStack item = player.getHeldItem();
			String color = "";

			if (item.getItem() == OfalenModItemCore.magazineLaserRed) {
				color = "Red";
			} else if (item.getItem() == OfalenModItemCore.magazineLaserGreen) {
				color = "Green";
			} else if (item.getItem() == OfalenModItemCore.magazineLaserBlue) {
				color = "Blue";
			} else if (item.getItem() == OfalenModItemCore.magazineLaserWhite) {
				color = "White";
			}

			if (color != "" && (tile.getColor() == "" || tile.getColor() == color)) {
				tile.setColor(color);
				tile.size += 32;
				if (!player.capabilities.isCreativeMode) {
					--player.getHeldItem().stackSize;
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		TileEntityEECannon machine = (TileEntityEECannon) world.getTileEntity(x, y, z);
		if (machine != null) {
			ItemStack itemStack = null;
			if (machine.getColor() == "Red") {
				itemStack = new ItemStack(OfalenModItemCore.magazineLaserRed, machine.size / 32);
			} else if (machine.getColor() == "Green") {
				itemStack = new ItemStack(OfalenModItemCore.magazineLaserGreen, machine.size / 32);
			} else if (machine.getColor() == "Blue") {
				itemStack = new ItemStack(OfalenModItemCore.magazineLaserBlue, machine.size / 32);
			} else if (machine.getColor() == "White") {
				itemStack = new ItemStack(OfalenModItemCore.magazineLaserWhite, machine.size / 32);
			}
			if (itemStack != null) {
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setBoolean("CanRepair", false);
				itemStack.setTagCompound(nbt);
				float f = this.random.nextFloat() * 0.6F + 0.1F;
				float f1 = this.random.nextFloat() * 0.6F + 0.1F;
				float f2 = this.random.nextFloat() * 0.6F + 0.1F;

				while (itemStack.stackSize > 0) {
					int j = this.random.nextInt(21) + 10;

					if (j > itemStack.stackSize) {
						j = itemStack.stackSize;
					}

					itemStack.stackSize -= j;
					EntityItem entityitem = new EntityItem(world, x + f, y + f1, z + f2, new ItemStack(itemStack.getItem(), j, itemStack.getItemDamage()));

					if (itemStack.hasTagCompound()) {
						entityitem.getEntityItem().setTagCompound(((NBTTagCompound) itemStack.getTagCompound().copy()));
					}

					float f3 = 0.025F;
					entityitem.motionX = (float) this.random.nextGaussian() * f3;
					entityitem.motionY = (float) this.random.nextGaussian() * f3 + 0.1F;
					entityitem.motionZ = (float) this.random.nextGaussian() * f3;
					world.spawnEntityInWorld(entityitem);
				}
			}
			world.func_147453_f(x, y, z, block);
		}
		super.breakBlock(world, x, y, z, block, meta);
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

}
