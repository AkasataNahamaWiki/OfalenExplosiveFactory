package nahamawiki.oef.core;

import nahamawiki.oef.OEFCore;
import nahamawiki.oef.block.BlockEEGenerator;
import nahamawiki.oef.block.BlockEEGenerator_on;
import nahamawiki.oef.block.BlockEELamp;
import nahamawiki.oef.block.BlockEEMachineBase;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

public class OEFBlockCore {

	public static BlockEEMachineBase EEGenerator;
	public static BlockEEMachineBase EEGenerator_on;
	public static BlockEEMachineBase EELamp;
	public static BlockEEMachineBase EELamp_on;

	public static void registerBlock() {
		EEGenerator = new BlockEEGenerator();
		EEGenerator.setBlockName("EEGenerator");
		EEGenerator.setBlockTextureName(OEFCore.DOMEINNAME + "EEGenerator");
		GameRegistry.registerBlock(EEGenerator, EEGenerator.getUnlocalizedName());
		OreDictionary.registerOre(EEGenerator.getUnlocalizedName(), EEGenerator);

		EEGenerator_on = new BlockEEGenerator_on();
		EEGenerator_on.setBlockName("EEGenerator_on");
		EEGenerator_on.setBlockTextureName(OEFCore.DOMEINNAME + "EEGenerator_on");
		GameRegistry.registerBlock(EEGenerator_on, EEGenerator_on.getUnlocalizedName());
		OreDictionary.registerOre(EEGenerator_on.getUnlocalizedName(), EEGenerator_on);

		EELamp = new BlockEELamp(false);
		EELamp.setBlockName("EELamp");
		EELamp.setBlockTextureName(OEFCore.DOMEINNAME + "EELamp");
		GameRegistry.registerBlock(EELamp, EELamp.getUnlocalizedName());
		OreDictionary.registerOre(EELamp.getUnlocalizedName(), EELamp);

		EELamp_on = new BlockEELamp(true);
		EELamp_on.setCreativeTab(null);
		EELamp_on.setBlockName("EELamp_on");
		EELamp_on.setBlockTextureName(OEFCore.DOMEINNAME + "EELamp_on");
		EELamp_on.setBlockTextureName("creepermod:creeperbomb");
		GameRegistry.registerBlock(EELamp_on, EELamp_on.getUnlocalizedName());
		OreDictionary.registerOre(EELamp_on.getUnlocalizedName(), EELamp_on);
	}

}
