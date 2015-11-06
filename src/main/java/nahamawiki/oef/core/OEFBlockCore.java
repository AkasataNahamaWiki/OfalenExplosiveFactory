package nahamawiki.oef.core;

import cpw.mods.fml.common.registry.GameRegistry;
import nahamawiki.oef.OEFCore;
import nahamawiki.oef.block.BlockEEBomb;
import nahamawiki.oef.block.BlockEECannon;
import nahamawiki.oef.block.BlockEECapacitor;
import nahamawiki.oef.block.BlockEECharger;
import nahamawiki.oef.block.BlockEEConductor;
import nahamawiki.oef.block.BlockEECraftingTable;
import nahamawiki.oef.block.BlockEEFurnace;
import nahamawiki.oef.block.BlockEEGenerator;
import nahamawiki.oef.block.BlockEEItemImporter;
import nahamawiki.oef.block.BlockEEItemTransporter;
import nahamawiki.oef.block.BlockEELamp;
import nahamawiki.oef.block.BlockEEMiner;
import nahamawiki.oef.block.BlockEERobo;
import nahamawiki.oef.block.BlockEESurveyor;
import nahamawiki.oef.itemblock.ItemBlockOEF;
import nahamawiki.oef.tileentity.TileEntityEECannon;
import nahamawiki.oef.tileentity.TileEntityEECapacitor;
import nahamawiki.oef.tileentity.TileEntityEECharger;
import nahamawiki.oef.tileentity.TileEntityEEConductor;
import nahamawiki.oef.tileentity.TileEntityEECraftingTable;
import nahamawiki.oef.tileentity.TileEntityEEFurnace;
import nahamawiki.oef.tileentity.TileEntityEEGenerator;
import nahamawiki.oef.tileentity.TileEntityEEItemImporter;
import nahamawiki.oef.tileentity.TileEntityEEItemTransporter;
import nahamawiki.oef.tileentity.TileEntityEELamp;
import nahamawiki.oef.tileentity.TileEntityEEMiner;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import takumicraft.Takumi.Block.CreeperWall_B;

public class OEFBlockCore {

	public static Block EEGenerator;
	public static Block EECapacitor;
	public static Block EELamp;
	public static Block EECharger;
	public static Block EECannon;
	public static Block EEFurnace;
	public static Block EECraftingTable;
	public static Block EEConductor;
	public static Block EEItemTransporter;
	public static Block EEItemImporter;
	public static Block EEMiner;
	public static Block EESurveyor;
	public static Block EESwordWall;
	public static Block EERobo;
	public static Block EEBomb;

	/** ブロックを追加・登録する */
	public static void registerBlocks() {
		EEGenerator = new BlockEEGenerator()
				.setBlockName("EEGenerator")
				.setBlockTextureName(OEFCore.DOMEINNAME + "EEGenerator");
		GameRegistry.registerBlock(EEGenerator, ItemBlockOEF.class, "EEGenerator");
		GameRegistry.registerTileEntity(TileEntityEEGenerator.class, "TileEntityEEGenerator");

		EECapacitor = new BlockEECapacitor()
				.setBlockName("EECapacitor")
				.setBlockTextureName(OEFCore.DOMEINNAME + "EECapacitor");
		GameRegistry.registerBlock(EECapacitor, ItemBlockOEF.class, "EECapacitor");
		GameRegistry.registerTileEntity(TileEntityEECapacitor.class, "TileEntityEECapacitor");

		EELamp = new BlockEELamp()
				.setBlockName("EELamp")
				.setBlockTextureName(OEFCore.DOMEINNAME + "EELamp");
		GameRegistry.registerBlock(EELamp, EELamp.getUnlocalizedName());
		GameRegistry.registerTileEntity(TileEntityEELamp.class, "TileEntityEELamp");

		EECharger = new BlockEECharger()
				.setBlockName("EECharger")
				.setBlockTextureName(OEFCore.DOMEINNAME + "EECharger");
		GameRegistry.registerBlock(EECharger, ItemBlockOEF.class, "EECharger");
		GameRegistry.registerTileEntity(TileEntityEECharger.class, "TileEntityEECharger");

		EECannon = new BlockEECannon()
				.setBlockName("EECannon")
				.setBlockTextureName(OEFCore.DOMEINNAME + "EECannon");
		GameRegistry.registerBlock(EECannon, "EECannon");
		GameRegistry.registerTileEntity(TileEntityEECannon.class, "TileEntityEECannon");

		EEFurnace = new BlockEEFurnace()
				.setBlockName("EEFurnace")
				.setBlockTextureName(OEFCore.DOMEINNAME + "EEFurnace");
		GameRegistry.registerBlock(EEFurnace, ItemBlockOEF.class, "EEFurnace");
		GameRegistry.registerTileEntity(TileEntityEEFurnace.class, "TileEntityEEFurnace");

		EECraftingTable = new BlockEECraftingTable()
				.setBlockName("EECraftingTable")
				.setBlockTextureName(OEFCore.DOMEINNAME + "EECraftingTable");
		GameRegistry.registerBlock(EECraftingTable, ItemBlockOEF.class, "EECraftingTable");
		GameRegistry.registerTileEntity(TileEntityEECraftingTable.class, "TileEntityEECraftingTable");

		EEConductor = new BlockEEConductor()
				.setBlockName("EEConductor")
				.setBlockTextureName(OEFCore.DOMEINNAME + "EEConductor");
		GameRegistry.registerBlock(EEConductor, ItemBlockOEF.class, "EEConductor");
		GameRegistry.registerTileEntity(TileEntityEEConductor.class, "TileEntityEEConductor");

		EEItemTransporter = new BlockEEItemTransporter()
				.setBlockName("EEItemTransporter")
				.setBlockTextureName(OEFCore.DOMEINNAME + "EEItemTransporter");
		GameRegistry.registerBlock(EEItemTransporter, ItemBlockOEF.class, "EEItemTransporter");
		GameRegistry.registerTileEntity(TileEntityEEItemTransporter.class, "TileEntityEEItemTransporter");

		EEItemImporter = new BlockEEItemImporter()
				.setBlockName("EEItemImporter")
				.setBlockTextureName(OEFCore.DOMEINNAME + "EEItemImporter");
		GameRegistry.registerBlock(EEItemImporter, ItemBlockOEF.class, "EEItemImporter");
		GameRegistry.registerTileEntity(TileEntityEEItemImporter.class, "TileEntityEEItemImporter");

		EEMiner = new BlockEEMiner()
				.setBlockName("EEMiner")
				.setBlockTextureName(OEFCore.DOMEINNAME + "EEMiner");
		GameRegistry.registerBlock(EEMiner, ItemBlockOEF.class, "EEMiner");
		GameRegistry.registerTileEntity(TileEntityEEMiner.class, "TileEntityEEMiner");

		EESurveyor = new BlockEESurveyor()
				.setBlockName("EESurveyor")
				.setBlockTextureName(OEFCore.DOMEINNAME + "EESurveyor");
		GameRegistry.registerBlock(EESurveyor, "EESurveyor");

		EESwordWall = new CreeperWall_B(Material.rock)
				.setBlockName("EESwordWall")
				.setBlockTextureName(OEFCore.DOMEINNAME + "EESwordWall");
		GameRegistry.registerBlock(EESwordWall, "EESwordWall");

		EERobo = new BlockEERobo()
				.setBlockName("EERobo")
				.setBlockTextureName(OEFCore.DOMEINNAME + "EERobo");
		GameRegistry.registerBlock(EERobo, "EERobo");

		EEBomb = new BlockEEBomb()
				.setBlockName("EEBomb")
				.setBlockTextureName(OEFCore.DOMEINNAME + "EEBomb");
		GameRegistry.registerBlock(EEBomb, "EEBomb");
	}

}
