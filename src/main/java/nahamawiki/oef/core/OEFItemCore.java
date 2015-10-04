package nahamawiki.oef.core;

import nahama.ofalenmod.core.OfalenModConfigCore;
import nahamawiki.oef.OEFCore;
import nahamawiki.oef.item.ItemEEBattery;
import nahamawiki.oef.item.ItemEEMater;
import nahamawiki.oef.item.ItemEESword;
import nahamawiki.oef.item.ItemEESword_B;
import nahamawiki.oef.item.ItemEESword_G;
import nahamawiki.oef.item.ItemEESword_R;
import nahamawiki.oef.item.ItemEESword_W;
import nahamawiki.oef.item.ItemEETool;
import nahamawiki.oef.item.armor.EEArmor;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import cpw.mods.fml.common.registry.GameRegistry;

public class OEFItemCore {

	public static final ToolMaterial TMEESWORD = EnumHelper.addToolMaterial("EES_0", 0, 250, 0, 21F, 0);
	public static ToolMaterial EET = EnumHelper.addToolMaterial("EET", 1, 500 , OfalenModConfigCore.efficiencyPerfectTool, 0, 100);
	public static ArmorMaterial AMEE = EnumHelper.addArmorMaterial("EEA", 30, new int[] { 0, 0, 0, 0 }, 100);

	public static Item EEMater;
	public static Item EEBattery;

	public static Item EESword;
	public static Item EESword_RED;
	public static Item EESword_BLUE;
	public static Item EESword_GREEN;
	public static Item EESword_WHITE;

	public static Item EEHelmet;
	public static Item EEChestPlate;
	public static Item EELeggings;
	public static Item EEBoots;

	public static Item EETool;

	/** アイテムを追加・登録する */
	public static void registerItems() {
		EEMater = new ItemEEMater()
				.setUnlocalizedName("EEMater")
				.setTextureName(OEFCore.DOMEINNAME + "EEMater");
		GameRegistry.registerItem(EEMater, EEMater.getUnlocalizedName());

		EEBattery = new ItemEEBattery()
				.setUnlocalizedName("EEBattery")
				.setTextureName(OEFCore.DOMEINNAME + "EEBattery");
		GameRegistry.registerItem(EEBattery, EEBattery.getUnlocalizedName());

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

		EEHelmet = new EEArmor(AMEE, 0)
				.setUnlocalizedName("EEHelmet")
				.setTextureName(OEFCore.DOMEINNAME + "EEHelmet");
		GameRegistry.registerItem(EEHelmet, EEHelmet.getUnlocalizedName());

		EEChestPlate = new EEArmor(AMEE, 1)
				.setUnlocalizedName("EEChestPlate")
				.setTextureName(OEFCore.DOMEINNAME + "EEChestPlate");
		GameRegistry.registerItem(EEChestPlate, EEChestPlate.getUnlocalizedName());

		EELeggings = new EEArmor(AMEE, 2)
				.setUnlocalizedName("EELeggings")
				.setTextureName(OEFCore.DOMEINNAME + "EELeggings");
		GameRegistry.registerItem(EELeggings, EELeggings.getUnlocalizedName());

		EEBoots = new EEArmor(AMEE, 3)
				.setUnlocalizedName("EEBoots")
				.setTextureName(OEFCore.DOMEINNAME + "EEBoots");
		GameRegistry.registerItem(EEBoots, EEBoots.getUnlocalizedName());

		EETool = new ItemEETool(EET)
				.setUnlocalizedName("EETool")
				.setTextureName(OEFCore.DOMEINNAME + "EETool");
		GameRegistry.registerItem(EETool, "EETool");


	}

}
