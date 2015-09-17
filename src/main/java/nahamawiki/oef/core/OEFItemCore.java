package nahamawiki.oef.core;

import nahamawiki.oef.OEFCore;
import nahamawiki.oef.item.ItemEEMater;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;

public class OEFItemCore {

	public static Item EEMater;

	/** アイテムを追加・登録する */
	public static void registerItems() {
		EEMater = new ItemEEMater()
				.setUnlocalizedName("EEMater")
				.setTextureName(OEFCore.DOMEINNAME + "EEMater");
		GameRegistry.registerItem(EEMater, EEMater.getUnlocalizedName());
	}

}
