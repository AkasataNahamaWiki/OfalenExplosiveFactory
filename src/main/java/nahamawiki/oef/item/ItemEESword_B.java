package nahamawiki.oef.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
//import takumicraft.Takumi.enchantment.TEnchantment;

public class ItemEESword_B extends ItemEESword
{

    public ItemEESword_B(Item.ToolMaterial p_i45356_1_)
    {
    	super(p_i45356_1_);
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase hit, EntityLivingBase player)
    {
    	for(int c = 0; c <= 5; c++)
    	{
    		double blockX = player.posX + (c * (hit.posX - player.posX) / 3);
    		double blockY = player.posY + (c * (hit.posY - player.posY) / 3);
    		double blockZ = player.posZ + (c * (hit.posZ - player.posZ) / 3);
    		player.worldObj.createExplosion(player, blockX, blockY, blockZ, 2, true);
    	}
        itemStack.damageItem(5, player);
        return true;
    }
}