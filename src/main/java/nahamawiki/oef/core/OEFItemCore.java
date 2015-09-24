package nahamawiki.oef.core;

import nahamawiki.oef.OEFCore;
import nahamawiki.oef.item.ItemEEMater;
import nahamawiki.oef.item.ItemEESword;
import nahamawiki.oef.item.ItemEESword_B;
import nahamawiki.oef.item.ItemEESword_G;
import nahamawiki.oef.item.ItemEESword_R;
import nahamawiki.oef.item.ItemEESword_W;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;
import cpw.mods.fml.common.registry.GameRegistry;

public class OEFItemCore {

	public static final ToolMaterial  TMEESWORD = EnumHelper.addToolMaterial("EES_0", 0, 250, 0, 21F, 0);

	public static Item EEMater;
	public static Item EESword;
	public static Item EESword_RED;
	public static Item EESword_BLUE;
	public static Item EESword_GREEN;
	public static Item EESword_WHITE;

	/** アイテムを追加・登録する */
	public static void registerItems() {
		EEMater = new ItemEEMater()
				.setUnlocalizedName("EEMater")
				.setTextureName(OEFCore.DOMEINNAME + "EEMater");
		GameRegistry.registerItem(EEMater, EEMater.getUnlocalizedName());

		EESword = new ItemEESword(TMEESWORD)
			.setUnlocalizedName("EESword")
			.setTextureName(OEFCore.DOMEINNAME + "EESword");
		GameRegistry.registerItem(EESword, EESword.getUnlocalizedName());

		EESword_RED = new ItemEESword_R(TMEESWORD)
			.setUnlocalizedName("EESword_RED")
			.setTextureName(OEFCore.DOMEINNAME + "EESword_RED");
		GameRegistry.registerItem(EESword_RED, EESword_RED.getUnlocalizedName());

		EESword_GREEN = new ItemEESword_G(TMEESWORD)
			.setUnlocalizedName("EESword_GREEN")
			.setTextureName(OEFCore.DOMEINNAME + "EESword_GREEN");
		GameRegistry.registerItem(EESword_GREEN, EESword_GREEN.getUnlocalizedName());

		EESword_BLUE = new ItemEESword_B(TMEESWORD)
		.setUnlocalizedName("EESword_BLUE")
		.setTextureName(OEFCore.DOMEINNAME + "EESword_BLUE");
	GameRegistry.registerItem(EESword_BLUE, EESword_BLUE.getUnlocalizedName());


		EESword_WHITE = new ItemEESword_W(TMEESWORD)
			.setUnlocalizedName("EESword_WHITE")
			.setTextureName(OEFCore.DOMEINNAME + "EESword_WHITE");
		GameRegistry.registerItem(EESword_WHITE, EESword_WHITE.getUnlocalizedName());
	}

}
