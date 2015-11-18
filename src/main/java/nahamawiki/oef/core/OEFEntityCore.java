package nahamawiki.oef.core;

import nahamawiki.oef.entity.EntityEngineCreeper;
import nahamawiki.oef.entity.EntityPoweredArmor;
import nahamawiki.oef.entity.EntityRoboCreeper;
import net.minecraft.entity.EnumCreatureType;
import takumicraft.Takumi.TakumiCraftCore;
import cpw.mods.fml.common.registry.EntityRegistry;

public class OEFEntityCore {

	public static void register(Object mod) {
		EntityRegistry.registerModEntity(EntityPoweredArmor.class, "PoweredArmor", 0, mod, 64, 1, false);
		EntityRegistry.registerModEntity(EntityRoboCreeper.class, "EERobo", 1, mod, 64, 2, true);
		EntityRegistry.registerModEntity(EntityEngineCreeper.class, "EngineerCreeper", 2, mod, 64, 2, true);

		EntityRegistry.addSpawn(EntityEngineCreeper.class, 30, 1, 10, EnumCreatureType.monster, TakumiCraftCore.AllBiomeGenBase);
	}

}