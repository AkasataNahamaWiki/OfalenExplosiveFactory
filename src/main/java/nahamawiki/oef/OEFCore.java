package nahamawiki.oef;

import nahamawiki.oef.core.OEFBlockCore;
import nahamawiki.oef.core.OEFInfoCore;
import nahamawiki.oef.creativetab.OEFCreativeTab;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/** @author Tom Kate & Akasatanahama */
@Mod(modid = OEFCore.MODID, name = OEFCore.MODNAME, version = OEFCore.VERSION, dependencies = "required-after:OfalenMod;required-after:TakumiMod")
public class OEFCore {

	public static final String MODID = "OEF";
	public static final String MODNAME = "Ofalen Explosive Factory";
	public static final String VERSION = "1.0.0";
	public static final String DOMEINNAME = "oef:";

	@Instance(MODID)
	public static OEFCore instance;

	@Metadata(MODID)
	public static ModMetadata meta;

	public static Logger logger = LogManager.getLogger(MODID);
	public static final CreativeTabs tabOEF = new OEFCreativeTab("OEFTab");
	public static final Material materialOEF = new Material(MapColor.diamondColor);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		OEFInfoCore.loadInfo(meta);
		OEFBlockCore.registerBlock();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {}

}
