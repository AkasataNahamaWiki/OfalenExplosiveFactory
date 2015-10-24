package nahamawiki.oef.core;

import nahamawiki.oef.tileentity.TileEntityEECannon;
import nahamawiki.oef.tileentity.TileEntityEEMachineBase;
import cpw.mods.fml.common.registry.GameRegistry;

public class OEFTileEntityCore 
{
	public static void register()
	{
		GameRegistry.registerTileEntity(TileEntityEEMachineBase.class, "OEFTile");
		GameRegistry.registerTileEntity(TileEntityEECannon.class, "EECannonTile");
	}
}