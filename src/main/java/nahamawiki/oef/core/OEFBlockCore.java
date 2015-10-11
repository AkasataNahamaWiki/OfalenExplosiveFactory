package nahamawiki.oef.core;

import cpw.mods.fml.common.registry.GameRegistry;
import nahamawiki.oef.OEFCore;
import nahamawiki.oef.block.BlockEECannon;
import nahamawiki.oef.block.BlockEECharger;
import nahamawiki.oef.block.BlockEEConductor;
import nahamawiki.oef.block.BlockEEGenerator;
import nahamawiki.oef.block.BlockEELamp;
import nahamawiki.oef.itemblock.ItemBlockOEF;
import nahamawiki.oef.tileentity.TileEntityEECannon;
import nahamawiki.oef.tileentity.TileEntityEECharger;
import nahamawiki.oef.tileentity.TileEntityEEConductor;
import nahamawiki.oef.tileentity.TileEntityEEGenerator;
import nahamawiki.oef.tileentity.TileEntityEELamp;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import takumicraft.Takumi.Block.CreeperWall_B;

public class OEFBlockCore {

	public static Block EEGenerator;
	public static Block EECharger;
	public static Block EEConductor;
	public static Block EELamp;
	public static Block EESwordWall;
	public static Block EECannon;

	/** ブロックを追加・登録する */
	public static void registerBlocks() {
		EEGenerator = new BlockEEGenerator()
				.setBlockName("EEGenerator")
				.setBlockTextureName(OEFCore.DOMEINNAME + "EEGenerator");
		GameRegistry.registerBlock(EEGenerator, ItemBlockOEF.class, "EEGenerator");
		GameRegistry.registerTileEntity(TileEntityEEGenerator.class, "TileEntityEEGenerator");

		EECharger = new BlockEECharger()
				.setBlockName("EECharger")
				.setBlockTextureName(OEFCore.DOMEINNAME + "EECharger");
		GameRegistry.registerBlock(EECharger, ItemBlockOEF.class, "EECharger");
		GameRegistry.registerTileEntity(TileEntityEECharger.class, "TileEntityEECharger");

		EEConductor = new BlockEEConductor()
				.setBlockName("EEConductor")
				.setBlockTextureName(OEFCore.DOMEINNAME + "EEConductor");
		GameRegistry.registerBlock(EEConductor, ItemBlockOEF.class, "EEConductor");
		GameRegistry.registerTileEntity(TileEntityEEConductor.class, "TileEntityEEConductor");

		EELamp = new BlockEELamp()
				.setBlockName("EELamp")
				.setBlockTextureName(OEFCore.DOMEINNAME + "EELamp");
		GameRegistry.registerBlock(EELamp, EELamp.getUnlocalizedName());
		GameRegistry.registerTileEntity(TileEntityEELamp.class, "TileEntityEELamp");

		EESwordWall = new CreeperWall_B(Material.rock)
				.setBlockName("EESwordWall")
				.setBlockTextureName(OEFCore.DOMEINNAME + "EESwordWall");
		GameRegistry.registerBlock(EESwordWall, "EESwordWall");

		EECannon = new BlockEECannon()
				.setBlockName("EECannon")
				.setBlockTextureName(OEFCore.DOMEINNAME + "EECannon");
		GameRegistry.registerBlock(EECannon, "EECannon");
		GameRegistry.registerTileEntity(TileEntityEECannon.class, "TileEntityEECannon");
	}

}
