package nahamawiki.oef.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import takumicraft.Takumi.item.Entity.EntityExplosionBall;
//import takumicraft.Takumi.enchantment.TEnchantment;

public class ItemEESword_B extends ItemEESword
{

    public ItemEESword_B(Item.ToolMaterial p_i45356_1_)
    {
    	super(p_i45356_1_);
    }

    /**
     * Current implementations of player method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase hit, EntityLivingBase player)
    {
    	//音を出す
		player.worldObj.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
    	 for (int i = 0; i < 5; ++i)
         {
    			player.worldObj.spawnEntityInWorld(new EntityExplosionBall(player.worldObj, player , 1 , 2 , true));
         }
        itemStack.damageItem(5, player);
        return true;
    }
}