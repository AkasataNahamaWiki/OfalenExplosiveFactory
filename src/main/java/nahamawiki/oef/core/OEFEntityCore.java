package nahamawiki.oef.core;

import nahamawiki.oef.entity.EntityPoweredArmor;
import nahamawiki.oef.render.RenderPoweredArmor;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;

public class OEFEntityCore
{
	/**
	 * 
	 * @param mod
	 * OEFCoreからmodにthisを渡してあげてください
	 */
	public static void register(Object mod)
	{
		 EntityRegistry.registerModEntity(EntityPoweredArmor.class, "PoweredArmor", 0, mod, 64, 20, false);


	        if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
	        	RenderingRegistry.registerEntityRenderingHandler(EntityPoweredArmor.class, new RenderPoweredArmor());
	        }

	}
}