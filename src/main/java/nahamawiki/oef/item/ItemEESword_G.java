package nahamawiki.oef.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
//import takumicraft.Takumi.enchantment.TEnchantment;

public class ItemEESword_G extends ItemEESword {

	public ItemEESword_G(Item.ToolMaterial p_i45356_1_) {
		super(p_i45356_1_);
	}

	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase hit, EntityLivingBase player) {
		player.worldObj.createExplosion(player, player.posX, player.posY, player.posZ, 3, true);
		itemStack.damageItem(5, player);
		return true;
	}

}
