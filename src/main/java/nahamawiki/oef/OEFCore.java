package nahamawiki.oef;

import nahamawiki.oef.core.OEFBlockCore;
import nahamawiki.oef.core.OEFConfigCore;
import nahamawiki.oef.core.OEFEventCore;
import nahamawiki.oef.core.OEFInfoCore;
import nahamawiki.oef.core.OEFItemCore;
import nahamawiki.oef.core.OEFOreDicCore;
import nahamawiki.oef.core.OEFRecipeCore;
import nahamawiki.oef.core.UpdateCheckCore;
import nahamawiki.oef.creativetab.OEFCreativeTab;
import nahamawiki.oef.material.OEFMaterial;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;

/**
 * @author Tom Kate & Akasata Nahama
 */
@Mod(modid = OEFCore.MODID, name = OEFCore.MODNAME, version = OEFCore.VERSION, dependencies = "required-after:OfalenMod;required-after:TakumiMod")
public class OEFCore {

	public static final String MODID = "OEF";
	public static final String MODNAME = "Ofalen Explosive Factory";
	public static final String VERSION = "1.0.0-1.7.10";
	public static final String DOMEINNAME = "oef:";

	@Instance(MODID)
	public static OEFCore instance;

	@Metadata(MODID)
	public static ModMetadata meta;

	public static Logger logger = LogManager.getLogger(MODID);

	public static final CreativeTabs tabOEF = new OEFCreativeTab("OEFTab");
	public static final Material materialOEF = new OEFMaterial(MapColor.diamondColor);

    public static UpdateCheckCore update = null;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		OEFInfoCore.registerInfo(meta);
		OEFConfigCore.loadConfig(event);
		update = new UpdateCheckCore();
		update.checkUpdate();
		update.showBalloon();
		MinecraftForge.EVENT_BUS.register(new OEFEventCore());
		FMLCommonHandler.instance().bus().register(new OEFEventCore());
		OEFItemCore.registerItems();
		OEFBlockCore.registerBlocks();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		OEFRecipeCore.registerRecipes();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		OEFOreDicCore.getOres();
	}

	@Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        if (update != null && event.getSide() == Side.SERVER)
        {
            update.notifyUpdate(event.getServer(), event.getSide());
        }
    }

}
