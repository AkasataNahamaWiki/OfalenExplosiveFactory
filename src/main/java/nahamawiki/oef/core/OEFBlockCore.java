package nahamawiki.oef.core;


import nahamawiki.oef.OEFCore;
import nahamawiki.oef.block.BlockOEFGenerator;
import net.minecraft.block.Block;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

public class OEFBlockCore {

	public static final Block EEGen = 
			new BlockOEFGenerator()
			.setBlockName("EEGenerator")
			.setBlockTextureName(OEFCore.DOMEINNAME + "eegen");

	/**ブロックを設定する*/
	public static void registerBlock () {
		GameRegistry.registerBlock(EEGen, EEGen.getUnlocalizedName());
		
		OreDictionary.registerOre(EEGen.getUnlocalizedName(), EEGen);

	}

}
