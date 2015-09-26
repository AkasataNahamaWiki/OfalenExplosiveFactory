package nahamawiki.oef.item;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
//import takumicraft.Takumi.enchantment.TEnchantment;
import net.minecraft.util.MathHelper;

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

    	for(int t = 0 ; t< 5 ;t++)
        {
        	Random x = new Random();
        	int sx =MathHelper.getRandomIntegerInRange(x, -5, 5);
        	Random y = new Random();
        	int sy = 0;
        	Random z = new Random();
        	int sz =MathHelper.getRandomIntegerInRange(z, -5, 5);

        	EntityLightningBolt var17 = new EntityLightningBolt(player.worldObj,player.posX+ sx,player.posY+ sy,player.posZ+ sz);
        	player.worldObj.addWeatherEffect(var17);
        	if(!player.worldObj.isRemote)player.worldObj.createExplosion(player,player.posX+ sx,player.posY+ sy,player.posZ+ sz, 1.5F, true);
        	player.worldObj.spawnEntityInWorld(var17);

        }
    	
        itemStack.damageItem(5, player);
        return true;
    }
}