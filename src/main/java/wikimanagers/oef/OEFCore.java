package wikimanagers.oef;

import wikimanagers.oef.core.OEFInfoCore;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = OEFCore.MODID, name = OEFCore.MODNAME, version = OEFCore.VERSION)
public class OEFCore {

	public static final String MODID = "OEF";
	public static final String MODNAME = "Ofalen Explosive Factory";
	public static final String VERSION = "1.0.0";

	@Instance(MODID)
	public static OEFCore instance;

	@Metadata(MODID)
	public static ModMetadata meta;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		OEFInfoCore.loadInfo(meta);
	}

}
