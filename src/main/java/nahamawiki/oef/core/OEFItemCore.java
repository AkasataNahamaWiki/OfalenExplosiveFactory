package nahamawiki.oef.core;

import cpw.mods.fml.common.registry.GameRegistry;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahamawiki.oef.OEFCore;
import nahamawiki.oef.item.ItemEEBattery;
import nahamawiki.oef.item.ItemEESword;
import nahamawiki.oef.item.ItemEESword_B;
import nahamawiki.oef.item.ItemEESword_G;
import nahamawiki.oef.item.ItemEESword_R;
import nahamawiki.oef.item.ItemEESword_W;
import nahamawiki.oef.item.ItemEETool;
import nahamawiki.oef.item.ItemOEFMaterial;
import nahamawiki.oef.item.ItemRoboCreeperEgg;
import nahamawiki.oef.item.armor.EEArmor;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

public class OEFItemCore {

	public static final ToolMaterial TMEESWORD = EnumHelper.addToolMaterial("EES_0", 0, 250, 0, 21F, 0);
	public static ToolMaterial EET = EnumHelper.addToolMaterial("EET", 1, 500, OfalenModConfigCore.efficiencyPerfectTool, 0, 100);
	public static ArmorMaterial AMEE = EnumHelper.addArmorMaterial("EEA", 30, new int[] { 0, 0, 0, 0 }, 100);

	/** 0:EE Mater, 1:Emerald Powder, 2:EE Powder, 3:EE Powered Powder, 4:EE Crystal, 5:EE Powered Crystal, 6:EE Control Chip [A], 7:EECC[B], 8:EECC[C], 9:EECC[S], 10:Tempered Machine Cover Plate Set, 11:EE Pliers */
	public static Item materials;
	public static ItemStack EEMater;
	public static ItemStack EmeraldPowder;
	public static ItemStack EEPowder;
	public static ItemStack EEPoweredPowder;
	public static ItemStack EECrystal;
	public static ItemStack EEPoweredCrystal;
	public static ItemStack EEControlChipA;
	public static ItemStack EEControlChipB;
	public static ItemStack EEControlChipC;
	public static ItemStack EEControlChipS;
	public static ItemStack TemperedMachineCoverPlateSet;
	public static ItemStack EEPliers;
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

	public static Item EERoboEgg;

	public static Item EETool;

	/** アイテムを追加・登録する */
	public static void registerItems() {
		materials = new ItemOEFMaterial(12)
				.setUnlocalizedName("materials")
				.setTextureName(OEFCore.DOMEINNAME + "material");
		GameRegistry.registerItem(materials, materials.getUnlocalizedName());

		EEMater = new ItemStack(materials, 1, 0);
		EmeraldPowder = new ItemStack(materials, 1, 1);
		EEPowder = new ItemStack(materials, 1, 2);
		EEPoweredPowder = new ItemStack(materials, 1, 3);
		EECrystal = new ItemStack(materials, 1, 4);
		EEPoweredCrystal = new ItemStack(materials, 1, 5);
		EEControlChipA = new ItemStack(materials, 1, 6);
		EEControlChipB = new ItemStack(materials, 1, 7);
		EEControlChipC = new ItemStack(materials, 1, 8);
		EEControlChipS = new ItemStack(materials, 1, 9);
		TemperedMachineCoverPlateSet = new ItemStack(materials, 1, 10);
		EEPliers = new ItemStack(materials, 1, 11);

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

		EERoboEgg = new ItemRoboCreeperEgg()
				.setUnlocalizedName("EERoboEgg")
				.setTextureName(OEFCore.DOMEINNAME + "EERoboEgg");
		GameRegistry.registerItem(EERoboEgg, "EERoboEgg");

	}

}
