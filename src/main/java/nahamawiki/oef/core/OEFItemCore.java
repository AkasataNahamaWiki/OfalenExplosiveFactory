package nahamawiki.oef.core;

import nahamawiki.oef.OEFCore;
import nahamawiki.oef.item.ItemEEMater;
import nahamawiki.oef.item.ItemEESword;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;
import cpw.mods.fml.common.registry.GameRegistry;

public class OEFItemCore {

	public static final ToolMaterial  EES_0 = EnumHelper.addToolMaterial("EES_0", 0, 500, 0, 21F, 0);

	public static Item EEMater;
	public static Item EESword;

	/** アイテムを追加・登録する */
	public static void registerItems() {
		EEMater = new ItemEEMater()
				.setUnlocalizedName("EEMater")
				.setTextureName(OEFCore.DOMEINNAME + "EEMater");
		GameRegistry.registerItem(EEMater, EEMater.getUnlocalizedName());

		EESword = new ItemEESword(EES_0)
		.setUnlocalizedName("EESword")
		.setTextureName(OEFCore.DOMEINNAME + "EESword");
GameRegistry.registerItem(EESword, EESword.getUnlocalizedName());
	}

}
