package nahamawiki.oef.block;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import takumicraft.Takumi.TakumiCraftCore;
import takumicraft.Takumi.enchantment.TEnchantment;

public class BlockEEBomb extends BlockOEFBase {
	private static float power = 7F;
	public BlockEEBomb() {
		super();

		this.setHardness(0.001F);
		this.setResistance(0);
	}

	public void onBlockHarvested(World p_149681_1_, int p_149681_2_, int p_149681_3_, int p_149681_4_, int p_149681_5_, EntityPlayer p_149681_6_)
    {
	//プレイヤーが持っているアイテムを取得
		Item usingItem = null;

		if (p_149681_6_.getHeldItem() != null) usingItem =  p_149681_6_.getHeldItem().getItem();
		 if(!p_149681_1_.isRemote)
			 {
			 if(usingItem == null)
			 {
				 p_149681_1_.createExplosion(null, p_149681_2_, p_149681_3_, p_149681_4_, power, true);
			 }
			 else if(! (EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, p_149681_6_.getHeldItem()) > 0)&& ! (EnchantmentHelper.getEnchantmentLevel(TEnchantment.enchantmentMS.effectId, p_149681_6_.getHeldItem()) > 0) && usingItem != TakumiCraftCore.PerTool)
			 	{

			 		p_149681_1_.createExplosion(null, p_149681_2_, p_149681_3_, p_149681_4_, power, true);
			 	}
			 }
		 	else
		 		{
		 			p_149681_1_.createExplosion(null, p_149681_2_, p_149681_3_, p_149681_4_, 0, false);
		 		}
		 	super.onBlockHarvested(p_149681_1_, p_149681_2_, p_149681_3_, p_149681_4_, p_149681_5_, p_149681_6_);
    }



 public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion p_149723_5_)
 {
	 	if(!world.isRemote)world.createExplosion(null, x, y, z,power, true);
 }


}
