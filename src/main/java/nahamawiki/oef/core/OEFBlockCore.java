package nahamawiki.oef.core;

import nahamawiki.oef.OEFCore;
import nahamawiki.oef.block.BlockEEConductor;
import nahamawiki.oef.block.BlockEEGenerator;
import nahamawiki.oef.block.BlockEELamp;
import nahamawiki.oef.block.BlockEEMachineBase;
import nahamawiki.oef.tileentity.TileEntityEEConductor;
import net.minecraft.block.Block;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

public class OEFBlockCore {

	public static Block EEGenerator;
	public static BlockEEMachineBase EELamp;
	public static BlockEEMachineBase EELamp_on;

	public static Block conductorEE;

	/** ブロックを追加・登録する */
	public static void registerBlocks() {
		EEGenerator = new BlockEEGenerator()
				.setBlockName("EEGenerator")
				.setBlockTextureName(OEFCore.DOMEINNAME + "EEGenerator");
		GameRegistry.registerBlock(EEGenerator, EEGenerator.getUnlocalizedName());
		OreDictionary.registerOre(EEGenerator.getUnlocalizedName(), EEGenerator);

		EELamp = new BlockEELamp(false);
		EELamp.setBlockName("EELamp");
		EELamp.setBlockTextureName(OEFCore.DOMEINNAME + "EELamp");
		GameRegistry.registerBlock(EELamp, EELamp.getUnlocalizedName());
		OreDictionary.registerOre(EELamp.getUnlocalizedName(), EELamp);

		EELamp_on = new BlockEELamp(true);
		EELamp_on.setCreativeTab(null);
		EELamp_on.setBlockName("EELamp_on");
		EELamp_on.setBlockTextureName(OEFCore.DOMEINNAME + "EELamp_on");
		EELamp_on.setBlockTextureName(OEFCore.DOMEINNAME + "EELamp_on");
		GameRegistry.registerBlock(EELamp_on, EELamp_on.getUnlocalizedName());
		OreDictionary.registerOre(EELamp_on.getUnlocalizedName(), EELamp_on);

		conductorEE = new BlockEEConductor()
				.setBlockName("EEConductor")
				.setBlockTextureName(OEFCore.DOMEINNAME + "EE_Conductor");
		GameRegistry.registerBlock(conductorEE, conductorEE.getUnlocalizedName());
		GameRegistry.registerTileEntity(TileEntityEEConductor.class, "TileEntityEEConductor");
	}

}
