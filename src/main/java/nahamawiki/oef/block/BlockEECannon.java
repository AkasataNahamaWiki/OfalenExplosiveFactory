package nahamawiki.oef.block;

import nahama.ofalenmod.core.OfalenModItemCore;
import nahamawiki.oef.tileentity.TileEntityEECannon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockEECannon extends BlockEEMachineBase {

	public BlockEECannon() {
		super();
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityEECannon();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float posX, float posY, float posZ) {
		if (player.getHeldItem() != null && world.getTileEntity(x, y, z) != null) {
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
