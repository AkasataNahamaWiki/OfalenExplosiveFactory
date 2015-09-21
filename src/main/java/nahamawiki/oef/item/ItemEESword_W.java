package nahamawiki.oef.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
//import takumicraft.Takumi.enchantment.TEnchantment;

public class ItemEESword_W extends ItemEESword
{

    public ItemEESword_W(Item.ToolMaterial p_i45356_1_)
    {
    	super(p_i45356_1_);
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase hit, EntityLivingBase player)
    {
    	double pX = player.posX;
    	double pY = player.posY;
    	double pZ = player.posZ;
    	
    	
    	
    	for(int x = -5; x <= 5; x++)
    	{
    		player.worldObj.createExplosion(player, pX + x, pY, pZ, 2, true);
    	}
    	for(int z = -5; z <= 5; z++)
    	{
    		player.worldObj.createExplosion(player, pX , pY, pZ + z, 2, true);
    	}
    	
        itemStack.damageItem(5, player);
        return true;
    }
}