package nahamawiki.oef.core;

import cpw.mods.fml.common.network.IGuiHandler;
import nahamawiki.oef.gui.GuiEECharger;
import nahamawiki.oef.inventory.ContainerEECharger;
import nahamawiki.oef.tileentity.TileEntityEECharger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class OEFGuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (!world.blockExists(x, y, z))
			return null;

		TileEntity tileentity = world.getTileEntity(x, y, z);
		if (tileentity instanceof TileEntityEECharger) {
			return new ContainerEECharger(player, (TileEntityEECharger) tileentity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (!world.blockExists(x, y, z))
			return null;

		TileEntity tileentity = world.getTileEntity(x, y, z);
		if (tileentity instanceof TileEntityEECharger) {
			return new GuiEECharger(player, (TileEntityEECharger) tileentity);
		}
		return null;
	}

}
