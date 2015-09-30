package nahamawiki.oef.core;

import cpw.mods.fml.common.registry.GameRegistry;
import nahamawiki.oef.OEFCore;
import nahamawiki.oef.block.BlockEECharger;
import nahamawiki.oef.block.BlockEEConductor;
import nahamawiki.oef.block.BlockEEGenerator;
import nahamawiki.oef.block.BlockEEMachineBase;
import nahamawiki.oef.itemblock.ItemBlockOEF;
import nahamawiki.oef.tileentity.TileEntityEECharger;
import nahamawiki.oef.tileentity.TileEntityEEConductor;
import nahamawiki.oef.tileentity.TileEntityEEGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import takumicraft.Takumi.Block.CreeperWall_B;

public class OEFBlockCore {

	public static Block EEGenerator;
	public static Block EECharger;
	public static Block EEConductor;
	public static Block EESwordWall;

	public static BlockEEMachineBase EELamp;
	public static BlockEEMachineBase EELamp_on;

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

		// EELamp = new BlockEELamp(false);
		// EELamp.setBlockName("EELamp");
		// EELamp.setBlockTextureName(OEFCore.DOMEINNAME + "EELamp");
		// GameRegistry.registerBlock(EELamp, EELamp.getUnlocalizedName());
		//
		// EELamp_on = new BlockEELamp(true);
		// EELamp_on.setCreativeTab(null);
		// EELamp_on.setBlockName("EELamp_on");
		// EELamp_on.setBlockTextureName(OEFCore.DOMEINNAME + "EELamp_on");
		// GameRegistry.registerBlock(EELamp_on, EELamp_on.getUnlocalizedName());

		EEConductor = new BlockEEConductor()
				.setBlockName("EEConductor")
				.setBlockTextureName(OEFCore.DOMEINNAME + "EEGenerator");
		GameRegistry.registerBlock(EEConductor, "EEConductor");
		GameRegistry.registerTileEntity(TileEntityEEConductor.class, "TileEntityEEConductor");

		EESwordWall = new CreeperWall_B(Material.rock)
				.setBlockName("EESwordWall")
				.setBlockTextureName(OEFCore.DOMEINNAME + "EESwordWall");
		GameRegistry.registerBlock(EESwordWall, "EESwordWall");
	}

}
