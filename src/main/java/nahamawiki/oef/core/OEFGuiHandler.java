package nahamawiki.oef.core;

import cpw.mods.fml.common.network.IGuiHandler;
import nahamawiki.oef.gui.GuiEECapacitor;
import nahamawiki.oef.gui.GuiEECharger;
import nahamawiki.oef.gui.GuiEEFurnace;
import nahamawiki.oef.gui.GuiEEMiner;
import nahamawiki.oef.gui.GuiEETool;
import nahamawiki.oef.inventory.ContainerEECapacitor;
import nahamawiki.oef.inventory.ContainerEECharger;
import nahamawiki.oef.inventory.ContainerEEFurnace;
import nahamawiki.oef.inventory.ContainerEEMiner;
import nahamawiki.oef.inventory.ContainerEETool;
import nahamawiki.oef.tileentity.TileEntityEECapacitor;
import nahamawiki.oef.tileentity.TileEntityEECharger;
import nahamawiki.oef.tileentity.TileEntityEEFurnace;
import nahamawiki.oef.tileentity.TileEntityEEMiner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class OEFGuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 99) {
			return new ContainerEETool(player.inventory);
		}
		if (!world.blockExists(x, y, z))
			return null;
		TileEntity tileentity = world.getTileEntity(x, y, z);
		if (tileentity instanceof TileEntityEECharger) {
			return new ContainerEECharger(player, (TileEntityEECharger) tileentity);
		} else if (tileentity instanceof TileEntityEEMiner) {
			return new ContainerEEMiner(player, (TileEntityEEMiner) tileentity);
		} else if (tileentity instanceof TileEntityEECapacitor) {
			return new ContainerEECapacitor(player, (TileEntityEECapacitor) tileentity);
		} else if (tileentity instanceof TileEntityEEFurnace) {
			return new ContainerEEFurnace(player, (TileEntityEEFurnace) tileentity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 99) {
			return new GuiEETool(player.inventory);
		}
		if (!world.blockExists(x, y, z))
			return null;
		TileEntity tileentity = world.getTileEntity(x, y, z);
		if (tileentity instanceof TileEntityEECharger) {
			return new GuiEECharger(player, (TileEntityEECharger) tileentity);
		} else if (tileentity instanceof TileEntityEEMiner) {
			return new GuiEEMiner(player, (TileEntityEEMiner) tileentity);
		} else if (tileentity instanceof TileEntityEECapacitor) {
			return new GuiEECapacitor(player, (TileEntityEECapacitor) tileentity);
		} else if (tileentity instanceof TileEntityEEFurnace) {
			return new GuiEEFurnace(player, (TileEntityEEFurnace) tileentity);
		}
		return null;
	}

}
