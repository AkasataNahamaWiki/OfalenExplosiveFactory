package nahamawiki.oef.core;

import nahamawiki.oef.gui.GuiEECharger;
import nahamawiki.oef.gui.GuiEETool;
import nahamawiki.oef.inventory.ContainerEECharger;
import nahamawiki.oef.inventory.ContainerEETool;
import nahamawiki.oef.tileentity.TileEntityEECharger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class OEFGuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (!world.blockExists(x, y, z))
			return null;
		if(ID == 99)
		{
			return new ContainerEETool(player.inventory);
		}
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
		if(ID == 99)
		{
			return new GuiEETool(player.inventory);
		}
		TileEntity tileentity = world.getTileEntity(x, y, z);
		if (tileentity instanceof TileEntityEECharger) {
			return new GuiEECharger(player, (TileEntityEECharger) tileentity);
		}
		return null;
	}

}
