package nahamawiki.oef.core;

import nahamawiki.oef.OEFCore;
import nahamawiki.oef.block.BlockEEGenerator;
import net.minecraft.block.Block;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

public class OEFBlockCore {

	public static Block EEGenerator;

	public static void registerBlock() {
		EEGenerator = new BlockEEGenerator();
		EEGenerator.setBlockName("EEGenerator");
		EEGenerator.setBlockTextureName(OEFCore.DOMEINNAME + "EEGenerator");
		GameRegistry.registerBlock(EEGenerator, EEGenerator.getUnlocalizedName());
		OreDictionary.registerOre(EEGenerator.getUnlocalizedName(), EEGenerator);
	}

}
