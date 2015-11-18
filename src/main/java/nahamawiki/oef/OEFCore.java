package nahamawiki.oef;

import nahama.ofalenmod.model.ModelLaser;
import nahamawiki.oef.core.OEFBlockCore;
import nahamawiki.oef.core.OEFConfigCore;
import nahamawiki.oef.core.OEFEntityCore;
import nahamawiki.oef.core.OEFEventCore;
import nahamawiki.oef.core.OEFGuiHandler;
import nahamawiki.oef.core.OEFInfoCore;
import nahamawiki.oef.core.OEFItemCore;
import nahamawiki.oef.core.OEFOreDicCore;
import nahamawiki.oef.core.OEFRecipeCore;
import nahamawiki.oef.core.UpdateCheckCore;
import nahamawiki.oef.creativetab.OEFCreativeTab;
import nahamawiki.oef.entity.EntityCannonBlueLaser;
import nahamawiki.oef.entity.EntityCannonBoltLaser;
import nahamawiki.oef.entity.EntityCannonEPLaser;
import nahamawiki.oef.entity.EntityCannonGreenLaser;
import nahamawiki.oef.entity.EntityCannonRedLaser;
import nahamawiki.oef.entity.EntityCannonWhiteLaser;
import nahamawiki.oef.entity.EntityEngineCreeper;
import nahamawiki.oef.entity.EntityPoweredArmor;
import nahamawiki.oef.entity.EntityRoboCreeper;
import nahamawiki.oef.material.OEFMaterial;
import nahamawiki.oef.render.RenderCannonLaser;
import nahamawiki.oef.render.RenderEECannon;
import nahamawiki.oef.render.RenderEECapacitor;
import nahamawiki.oef.render.RenderEEConductor;
import nahamawiki.oef.render.RenderEngineCreeper;
import nahamawiki.oef.render.RenderPowered;
import nahamawiki.oef.render.RenderPoweredArmor;
import nahamawiki.oef.render.RenderRoboCreeper;
import nahamawiki.oef.tileentity.TileEntityEECannon;
import nahamawiki.oef.tileentity.TileEntityEECapacitor;
import nahamawiki.oef.tileentity.TileEntityEEConductor;
import nahamawiki.oef.tileentity.TileEntityEEMachineBase;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
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
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
		// MODの情報を登録する。
		OEFInfoCore.registerInfo(meta);
		// Configからデータを読み込む。
		OEFConfigCore.loadConfig(event);
		// 最新版かどうかチェックする。
		update = new UpdateCheckCore();
		update.checkUpdate();
		update.showBalloon();
		MinecraftForge.EVENT_BUS.register(new OEFEventCore());
		// Event処理を登録する。
		FMLCommonHandler.instance().bus().register(new OEFEventCore());
		// 追加するアイテム・ブロックを登録する。
		OEFItemCore.registerItems();
		OEFBlockCore.registerBlocks();
		// GUIを登録する。
		NetworkRegistry.INSTANCE.registerGuiHandler(this.instance, new OEFGuiHandler());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// 追加レシピを登録する。
		OEFRecipeCore.registerRecipes();
		// 追加Entityを登録する。
		OEFEntityCore.register(this);
		// クライアントなら、TileEntityとレーザーのレンダラーを登録する。
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			this.registRenderer();
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// 鉱石辞書からアイテム・ブロックを取得する。
		OEFOreDicCore.getOres();
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		// サーバーの起動時にアップデートの通知をする。
		if (update != null && event.getSide() == Side.SERVER) {
			update.notifyUpdate(event.getServer(), event.getSide());
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void registRenderer()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEEConductor.class, new RenderEEConductor());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEECapacitor.class, new RenderEECapacitor());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEECannon.class, new RenderEECannon());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEEMachineBase.class, new RenderPowered());
		RenderingRegistry.registerEntityRenderingHandler(EntityCannonRedLaser.class, new RenderCannonLaser(new ModelLaser(), "red"));
		RenderingRegistry.registerEntityRenderingHandler(EntityCannonGreenLaser.class, new RenderCannonLaser(new ModelLaser(), "green"));
		RenderingRegistry.registerEntityRenderingHandler(EntityCannonBlueLaser.class, new RenderCannonLaser(new ModelLaser(), "blue"));
		RenderingRegistry.registerEntityRenderingHandler(EntityCannonWhiteLaser.class, new RenderCannonLaser(new ModelLaser(), "white"));
		RenderingRegistry.registerEntityRenderingHandler(EntityCannonEPLaser.class, new RenderCannonLaser(new ModelLaser(), "EP"));
		RenderingRegistry.registerEntityRenderingHandler(EntityCannonBoltLaser.class, new RenderCannonLaser(new ModelLaser(), "BO"));
		
		RenderingRegistry.registerEntityRenderingHandler(EntityPoweredArmor.class, new RenderPoweredArmor());
		RenderingRegistry.registerEntityRenderingHandler(EntityRoboCreeper.class, new RenderRoboCreeper());
		RenderingRegistry.registerEntityRenderingHandler(EntityEngineCreeper.class, new RenderEngineCreeper());
	}

}
