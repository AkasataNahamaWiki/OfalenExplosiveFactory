package nahamawiki.oef.core;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import nahamawiki.oef.entity.EntityEngineCreeper;
import nahamawiki.oef.entity.EntityPoweredArmor;
import nahamawiki.oef.entity.EntityRoboCreeper;
import nahamawiki.oef.render.RenderEngineCreeper;
import nahamawiki.oef.render.RenderPoweredArmor;
import nahamawiki.oef.render.RenderRoboCreeper;
import net.minecraft.entity.EnumCreatureType;
import takumicraft.Takumi.TakumiCraftCore;

public class OEFEntityCore {

	public static void register(Object mod) {
		EntityRegistry.registerModEntity(EntityPoweredArmor.class, "PoweredArmor", 0, mod, 64, 1, false);
		EntityRegistry.registerModEntity(EntityRoboCreeper.class, "EERobo", 1, mod, 64, 2, true);
		EntityRegistry.registerModEntity(EntityEngineCreeper.class, "EngineerCreeper", 2, mod, 64, 2, true);

		EntityRegistry.addSpawn(EntityEngineCreeper.class, 30, 1, 10, EnumCreatureType.monster, TakumiCraftCore.AllBiomeGenBase);

		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			RenderingRegistry.registerEntityRenderingHandler(EntityPoweredArmor.class, new RenderPoweredArmor());
			RenderingRegistry.registerEntityRenderingHandler(EntityRoboCreeper.class, new RenderRoboCreeper());
			RenderingRegistry.registerEntityRenderingHandler(EntityEngineCreeper.class, new RenderEngineCreeper());
		}
	}

}