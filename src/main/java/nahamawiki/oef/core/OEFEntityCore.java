package nahamawiki.oef.core;

import nahamawiki.oef.entity.EntityPoweredArmor;
import nahamawiki.oef.entity.EntityRoboCreeper;
import nahamawiki.oef.render.RenderPoweredArmor;
import nahamawiki.oef.render.RenderRoboCreeper;
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
		 EntityRegistry.registerModEntity(EntityPoweredArmor.class, "PoweredArmor", 0, mod, 64, 1, false);
		 EntityRegistry.registerModEntity(EntityRoboCreeper.class, "EERobo", 1, mod, 64, 2, true);


	        if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
	        	RenderingRegistry.registerEntityRenderingHandler(EntityPoweredArmor.class, new RenderPoweredArmor());
	        	RenderingRegistry.registerEntityRenderingHandler(EntityRoboCreeper.class, new RenderRoboCreeper());
	        }

	}
}