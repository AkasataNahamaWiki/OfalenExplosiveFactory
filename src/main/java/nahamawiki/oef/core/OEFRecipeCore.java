package nahamawiki.oef.core;

import static nahama.ofalenmod.core.OfalenModRecipeCore.*;
import nahama.ofalenmod.core.OfalenModItemCore;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import takumicraft.Takumi.TakumiCraftCore;
import cpw.mods.fml.common.registry.GameRegistry;

public class OEFRecipeCore {

	private static final OEFItemCore ITEM = new OEFItemCore();
	private static final OEFBlockCore BLOCK = new OEFBlockCore();

	/** レシピを登録する */
	public static void registerRecipes() {
		// EEメーター
		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.EEMater,
				"GAG", "SBS", "ICI", 'G', Items.gold_ingot, 'S', Blocks.stone_button, 'I', Items.iron_ingot,
				'A', ITEM.EEControlChipA, 'B', ITEM.EEControlChipB, 'C', ITEM.EEControlChipC));
		// エメラルドパウダー
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ITEM.materials, 9, 1), Items.emerald));
		// EEパウダー
		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.EEPowder,
				"GGG", "GEG", "GGG", 'G', Items.gunpowder, 'E', ITEM.EmeraldPowder));
		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.EEPoweredPowder,
				"PPP", "POP", "PPP", 'P', ITEM.EEPowder, 'O', frag[3]));
		// EEクリスタル
		GameRegistry.addSmelting(ITEM.EEPowder, ITEM.EECrystal, 0F);
		GameRegistry.addSmelting(ITEM.EEPoweredPowder, ITEM.EEPoweredCrystal, 0F);
		// EEコントロールチップ
		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.EEControlChipA,
				"PCP", "COC", "PCP", 'P', ITEM.EEPowder, 'C', ITEM.EECrystal, 'O', frag[0]));
		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.EEControlChipB,
				"PCP", "COC", "PCP", 'P', ITEM.EEPowder, 'C', ITEM.EECrystal, 'O', frag[1]));
		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.EEControlChipC,
				"PCP", "COC", "PCP", 'P', ITEM.EEPowder, 'C', ITEM.EECrystal, 'O', frag[2]));
		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.EEControlChipS,
				"PCP", "COC", "PCP", 'P', ITEM.EEPoweredPowder, 'C', ITEM.EEPoweredCrystal, 'O', frag[3]));
		// 硬質機械用カバープレートセット
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.materials, 2, 10),
				"PBP", "POP", "PBP", 'P', new ItemStack(OfalenModItemCore.partsOfalen, 1, 0), 'B', TakumiCraftCore.creeperblock, 'O', gem[3]));
		// EEバッテリー
		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.EEBattery,
				"CAC", "OEO", "GBG", 'C', ITEM.EECrystal, 'O', frag[1], 'E', Items.emerald, 'G', Items.gold_nugget,
				'A', ITEM.EEControlChipA, 'B', ITEM.EEControlChipB));
		
		//EE匠ロボパーツ
		GameRegistry.addRecipe(new ShapedOreRecipe(BLOCK.EERobo,
				"EEE", "EBE", "CCC" , 'E' , ITEM.EEPoweredPowder , 'B' , ITEM.EEBattery , 'C' , TakumiCraftCore.creeperblock));
		
		//EE式高性能爆弾
		GameRegistry.addRecipe(new ShapedOreRecipe(BLOCK.EEBomb,
				"BBB", "BCB", "BBB" , 'B' , ITEM.EEBattery , 'C' , TakumiCraftCore.creeperblock));
	}

}
