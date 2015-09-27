package nahamawiki.oef.item;

import nahamawiki.oef.core.OEFBlockCore;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
//import takumicraft.Takumi.enchantment.TEnchantment;

public class ItemEESword_R extends ItemEESword {

	public ItemEESword_R(Item.ToolMaterial p_i45356_1_) {
		super(p_i45356_1_);
	}

	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase hit, EntityLivingBase player) {
		double dx = player.posX - hit.posX;
		double dz = player.posZ - hit.posZ;
		if (dx < dz) {
			for (int x = (int) hit.posX - 2; x < hit.posX + 3; x++) {
				int z = (int) hit.posZ;
				for (int y = (int) hit.posY; y < hit.posY + 4; y++) {
					player.worldObj.createExplosion(player, x, y, z, 0.5F, false);
					player.worldObj.setBlock(x, y, z, OEFBlockCore.EESwordWall);
				}
			}
		} else {
			for (int z = (int) hit.posZ - 2; z < hit.posZ + 3; z++) {
				int x = (int) hit.posX;
				for (int y = (int) hit.posY; y < hit.posY + 4; y++) {
					player.worldObj.createExplosion(player, x, y, z, 0.5F, false);
					player.worldObj.setBlock(x, y, z, OEFBlockCore.EESwordWall);
				}
			}
		}
		itemStack.damageItem(5, player);
		return true;
	}

}
