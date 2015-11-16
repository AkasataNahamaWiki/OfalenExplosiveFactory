package nahamawiki.oef.core;

import static nahama.ofalenmod.core.OfalenModRecipeCore.*;

import cpw.mods.fml.common.registry.GameRegistry;
import nahama.ofalenmod.core.OfalenModItemCore;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import takumicraft.Takumi.TakumiCraftCore;

public class OEFRecipeCore {

	private static final OEFItemCore ITEM = new OEFItemCore();
	private static final OEFBlockCore BLOCK = new OEFBlockCore();
	/** 金インゴット */
	private static final String ingotGold = "ingotGold";
	/** 鉄インゴット */
	private static final String ingotIron = "ingotIron";
	/** エメラルド */
	private static final String gemEmerald = "gemEmerald";
	/** 匠魔石 */
	private static final String TDiamond = "TDiamond";
	/** 匠式高性能爆弾 */
	private static final String CreeperBomb = "CreeperBomb";
	/** 棒 */
	private static final String stickWood = "stickWood";
	/** レッドストーン */
	private static final String dustRedstone = "dustRedstone";
	/** エメラルドブロック */
	private static final String blockEmerald = "blockEmerald";
	/** ラピスラズリ */
	private static final String dyeBlue = "dyeBlue";
	/** 匠ポータルフレーム */
	private static final String TWFrame = "TWFrame";
	/** 金ブロック */
	private static final String blockGold = "blockGold";
	/** かみなりのいし */
	private static final String BoltStone = "BoltStone";
	/** TNT式高性能爆弾 */
	private static final String TNTBomb = "TNTBomb";
	/** 匠式宝剣 */
	private static final String CreeperSword = "CreeperSword";
	/** 匠のオファレン */
	private static final String Creeper_sOfalenStone = "Creeper_sOfalenStone";

	/** レシピを登録する */
	public static void registerRecipes() {
		// EEメーター
		addRecipe(ITEM.EEMater,
				"GAG", "SBS", "ICI",
				'G', ingotGold, 'S', Blocks.stone_button, 'I', ingotIron,
				'A', ITEM.EEControlChipA, 'B', ITEM.EEControlChipB, 'C', ITEM.EEControlChipC);

		// エメラルドパウダー
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ITEM.materials, 9, 1), gemEmerald));

		// EEパウダー
		addRecipe(ITEM.EEPowder,
				"GGG", "GEG", "GGG",
				'G', Items.gunpowder, 'E', ITEM.EmeraldPowder);
		addRecipe(ITEM.EEPoweredPowder,
				"PPP", "POP", "PPP",
				'P', ITEM.EEPowder, 'O', frag[3]);

		// EEクリスタル
		GameRegistry.addSmelting(ITEM.EEPowder, ITEM.EECrystal, 0.5F);
		GameRegistry.addSmelting(ITEM.EEPoweredPowder, ITEM.EEPoweredCrystal, 1F);

		// EEコントロールチップ
		addRecipe(ITEM.EEControlChipA,
				"ICI", "COC", "ICI",
				'I', ingotIron, 'C', ITEM.EECrystal, 'O', gem[0]);
		addRecipe(ITEM.EEControlChipB,
				"ICI", "COC", "ICI",
				'I', ingotIron, 'C', ITEM.EECrystal, 'O', gem[1]);
		addRecipe(ITEM.EEControlChipC,
				"ICI", "COC", "ICI",
				'I', ingotIron, 'C', ITEM.EECrystal, 'O', gem[2]);
		addRecipe(ITEM.EEControlChipS,
				"MCM", "COC", "MCM",
				'M', TDiamond, 'C', ITEM.EEPoweredCrystal, 'O', gem[3]);

		// 硬質機械用カバープレートセット
		addRecipe(new ItemStack(ITEM.materials, 2, 10),
				"PBP", "POP", "PBP",
				'P', new ItemStack(OfalenModItemCore.partsOfalen, 1, 0), 'B', CreeperBomb, 'O', gem[3]);

		// EEペンチ
		addRecipe(ITEM.EEPliers,
				"  I", "SR ", " S ",
				'I', ingotIron, 'S', stickWood, 'R', dustRedstone);

		// EEバッテリー
		addRecipe(ITEM.EEBattery,
				"CAC", "BEB", "GZG",
				'C', ITEM.EECrystal, 'E', blockEmerald, 'G', ingotGold, 'A', ITEM.EEControlChipA, 'B', ITEM.EEControlChipB, 'Z', ITEM.EEControlChipC);
		addRecipe(new ItemStack(ITEM.EEBattery, 1, 1),
				"CYC", "OBO", "GYG",
				'C', ITEM.EEPoweredCrystal, 'Y', ITEM.EEControlChipB, 'O', frag[1], 'B', ITEM.EEBattery, 'G', ingotGold);
		addRecipe(new ItemStack(ITEM.EEBattery, 1, 2),
				"CYC", "SBS", "GYG",
				'C', ITEM.EEPoweredCrystal, 'Y', ITEM.EEControlChipB, 'S', TDiamond, 'B', new ItemStack(ITEM.EEBattery, 1, 1), 'G', ingotGold);
		addRecipe(new ItemStack(ITEM.EEBattery, 1, 3),
				"CSC", "OBO", "GSG",
				'C', ITEM.EEPoweredCrystal, 'S', ITEM.EEControlChipS, 'O', gem[1], 'B', new ItemStack(ITEM.EEBattery, 1, 2), 'G', ingotGold);

		// レシピシート
		GameRegistry.addRecipe(new ShapelessOreRecipe(ITEM.recipeSheet,
				Items.paper, dyeBlue));

		// EE生成機
		addRecipe(BLOCK.EEGenerator,
				"BAB", "PCP", "BOB",
				'B', CreeperBomb, 'A', ITEM.EEControlChipC, 'P', ITEM.PlateSet, 'C', ITEM.EEPoweredCrystal, 'O', core[2]);
		addLvUpRecipe(BLOCK.EEGenerator, ITEM.EEControlChipC);

		// EE貯蓄機
		addRecipe(BLOCK.EECapacitor,
				"FBF", "PCP", "FOF",
				'F', TWFrame, 'B', ITEM.EEControlChipB, 'P', ITEM.PlateSet, 'C', new ItemStack(BLOCK.EEConductor), 'O', core[1]);
		addLvUpRecipe(BLOCK.EECapacitor, ITEM.EEControlChipB);

		// EEランプ
		addRecipe(BLOCK.EELamp,
				"BCB", "CLC", "BCB",
				'B', CreeperBomb, 'C', ITEM.EECrystal, 'L', Blocks.redstone_lamp);

		// EE充填機
		addRecipe(BLOCK.EECharger,
				"EAE", "PBP", "EOE",
				'E', gemEmerald, 'A', ITEM.EEControlChipA, 'P', ITEM.PlateSet, 'B', ITEM.EEBattery, 'O', core[2]);
		addLvUpRecipe(BLOCK.EECharger);

		// EEオファレン砲台
		addRecipe(BLOCK.EECannon,
				" A ", "PCP", "BGB",
				'A', ITEM.EEControlChipA, 'P', OfalenModItemCore.pistolLaser, 'C', ITEM.EEPoweredCrystal, 'B', CreeperBomb, 'G', blockGold);

		// EEかまど
		addRecipe(BLOCK.EEFurnace,
				"XAX", "PFP", "XCX",
				'X', Items.fire_charge, 'A', ITEM.EEControlChipA, 'P', ITEM.PlateSet, 'F', Blocks.furnace, 'C', ITEM.EEPoweredCrystal);
		addLvUpRecipe(BLOCK.EEFurnace);

		// EE作業台
		addRecipe(BLOCK.EECraftingTable,
				"BAB", "PTP", "BCB",
				'B', Blocks.bookshelf, 'A', ITEM.EEControlChipA, 'P', ITEM.PlateSet, 'T', Blocks.crafting_table, 'C', ITEM.EEPoweredCrystal);
		addLvUpRecipe(BLOCK.EECraftingTable);

		// EE伝導管
		addRecipe(BLOCK.EEConductor,
				"IBI", "XCY", "IBI",
				OfalenModItemCore.partsOfalen3D, 'B', CreeperBomb, 'X', ITEM.EEControlChipA, 'C', ITEM.EEPoweredCrystal, 'Y', ITEM.EEControlChipC);
		addDuctLvUpRecipe(BLOCK.EEConductor);

		// EEアイテム輸送管
		addRecipe(BLOCK.EEItemTransporter,
				"GOG", "HCH", "GOG",
				'G', ingotGold, 'O', frag[2], 'H', Blocks.hopper, 'C', new ItemStack(BLOCK.EEConductor));
		addDuctLvUpRecipe(BLOCK.EEItemTransporter);

		// EEアイテム導入管
		addRecipe(BLOCK.EEItemImporter,
				"ROR", "HTH", "ROR",
				'R', dustRedstone, 'O', frag[0], 'H', Blocks.hopper, 'T', new ItemStack(BLOCK.EEItemTransporter));
		addDuctLvUpRecipe(BLOCK.EEItemImporter);

		// EE採掘機
		addRecipe(BLOCK.EEMiner,
				"TAT", "POP", "TCT",
				'T', TNTBomb, 'A', ITEM.EEControlChipA, 'P', ITEM.PlateSet, 'O', OfalenModItemCore.pickaxeOfalen, 'C', ITEM.EEPoweredCrystal);
		addLvUpRecipe(BLOCK.EEMiner);

		// EE測量機
		addRecipe(BLOCK.EESurveyor,
				"SLS", "PEP", "SLS",
				'S', Items.paper, 'L', new ItemStack(OfalenModItemCore.crystalEnergyLaser, 1, 3), 'P', ITEM.PlateSet, 'E', Items.ender_eye);

		// EE匠ロボパーツ
		addRecipe(BLOCK.EERobo,
				"EEE", "EBE", "CCC",
				'E', ITEM.EEPoweredPowder, 'B', ITEM.EEBattery, 'C', CreeperBomb);

		// EE式高性能爆弾
		addRecipe(BLOCK.EEBomb,
				"CCC", "CEC", "CCC",
				'C', CreeperBomb, 'E', ITEM.EEPoweredCrystal);

		// 真・匠式宝剣
		addRecipe(ITEM.EESword,
				"EBE", "ESE", "EBE",
				'E', ITEM.EEPoweredCrystal, 'B', ITEM.EEBattery, 'S', CreeperSword);
		addSwordRecipe(ITEM.EESword_RED, 0);
		addSwordRecipe(ITEM.EESword_GREEN, 1);
		addSwordRecipe(ITEM.EESword_BLUE, 2);
		addSwordRecipe(ITEM.EESword_WHITE, 3);

		// 防具
		addArmorRecipe(ITEM.EEHelmet, OfalenModItemCore.helmetPerfectOfalen, TakumiCraftCore.CreeperAH);
		addArmorRecipe(ITEM.EEChestPlate, OfalenModItemCore.chestplatePerfectOfalen, TakumiCraftCore.CreeperAC);
		addArmorRecipe(ITEM.EELeggings, OfalenModItemCore.leggingsPerfectOfalen, TakumiCraftCore.CreeperAL);
		addArmorRecipe(ITEM.EEBoots, OfalenModItemCore.bootsPerfectOfalen, TakumiCraftCore.CreeperAB);

		// ツール
		addRecipe(ITEM.EETool,
				"OBO", "ETE", "LBL",
				'O', Creeper_sOfalenStone, 'B', ITEM.EEBattery, 'E', ITEM.EEPoweredCrystal, 'T', OfalenModItemCore.toolPerfectOfalen, 'L', BoltStone);
	}

	private static void addRecipe(Item result, Object... recipe) {
		addRecipe(new ItemStack(result), recipe);
	}

	private static void addRecipe(Block result, Object... recipe) {
		addRecipe(new ItemStack(result), recipe);
	}

	private static void addRecipe(ItemStack result, Object... recipe) {
		GameRegistry.addRecipe(new ShapedOreRecipe(result, recipe));
	}

	private static void addLvUpRecipe(Block block) {
		addLvUpRecipe(block, ITEM.EEControlChipA);
	}

	private static void addLvUpRecipe(Block machine, ItemStack chip) {
		addRecipe(new ItemStack(machine, 1, 1),
				"OXO", "BMB", "OCO",
				'O', gem[3], 'X', chip, 'B', CreeperBomb, 'M', new ItemStack(machine), 'C', ITEM.EEPoweredCrystal);
		addRecipe(new ItemStack(machine, 1, 2),
				"SXS", "PMP", "SCS",
				'S', TDiamond, 'X', chip, 'P', ITEM.EEControlChipS, 'M', new ItemStack(machine, 1, 1), 'C', ITEM.EEPoweredCrystal);
		addRecipe(new ItemStack(machine, 1, 3),
				"BXB", "SMS", "OCO",
				'B', BoltStone, 'X', chip, 'S', ITEM.EEControlChipS, 'M', new ItemStack(machine, 1, 2), 'O', core[3], 'C', ITEM.EEPoweredCrystal);
	}

	private static void addDuctLvUpRecipe(Block duct) {
		addRecipe(new ItemStack(duct, 1, 1),
				"AOC", "BDB", "OEO",
				'A', ITEM.EEControlChipA, 'O', gem[3], 'C', ITEM.EEControlChipB, 'B', CreeperBomb, 'D', new ItemStack(duct), 'E', ITEM.EEPoweredCrystal);
		addRecipe(new ItemStack(duct, 1, 2),
				"ASC", "PDP", "SES",
				'A', ITEM.EEControlChipA, 'S', TDiamond, 'C', ITEM.EEControlChipB, 'P', ITEM.EEControlChipS, 'D', new ItemStack(duct, 1, 1), 'E', ITEM.EEPoweredCrystal);
		addRecipe(new ItemStack(duct, 1, 3),
				"ABC", "SDS", "OEO",
				'A', ITEM.EEControlChipA, 'B', BoltStone, 'C', ITEM.EEControlChipB, 'S', ITEM.EEControlChipS, 'D', new ItemStack(duct, 1, 2), 'O', core[3], 'E', ITEM.EEPoweredCrystal);
	}

	private static void addSwordRecipe(Item sword, int color) {
		addRecipe(sword,
				"OBO", "OSO", "OBO",
				'O', core[color], 'B', ITEM.EEBattery, 'S', ITEM.EESword);
	}

	private static void addArmorRecipe(Item ee, Item ofalen, Item takumi) {
		addRecipe(ee,
				"CCC", "OCT", "CCC",
				'C', ITEM.EEPoweredCrystal, 'O', ofalen, 'T', takumi);
	}

}
