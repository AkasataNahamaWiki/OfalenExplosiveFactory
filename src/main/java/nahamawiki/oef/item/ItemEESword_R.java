package nahamawiki.oef.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
//import takumicraft.Takumi.enchantment.TEnchantment;

public class ItemEESword_R extends ItemEESword
{

    public ItemEESword_R(Item.ToolMaterial p_i45356_1_)
    {
    	super(p_i45356_1_);
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase hit, EntityLivingBase player)
    {
    	double radT = Math.atan2(hit.posX - player.posX, hit.posZ - player.posZ);
    	for(int x = -2 ; x <= 2 ; x++)
    	{
    		double cross = Math.toRadians(90);
    		double z = Math.tan(radT + cross) * x;
    		hit.worldObj.createExplosion(player,hit.posX + (x * 1.1), hit.posY, hit.posZ + (z * 1.1), 2, true);
    	}
        itemStack.damageItem(5, player);
        return true;
    }
}