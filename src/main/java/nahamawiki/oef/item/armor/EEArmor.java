package nahamawiki.oef.item.armor;

import nahamawiki.oef.OEFCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import takumicraft.Takumi.Potion.CreeperPotion;
import takumicraft.Takumi.enchantment.TEnchantment;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EEArmor extends ItemArmor {

	private IIcon overlayIcon;


	public EEArmor(ArmorMaterial material, int par2) {
		super(material, 0, par2);
		this.setCreativeTab(OEFCore.tabOEF);

	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack itemStack, int pass) {
		return true;
	}

	/**テクスチャを指定する*/
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		int i = 1;
		if (this.armorType == 2) i = 2;
		return "ofalenmod:textures/models/armor/ofalen_P_layer_" + i + ".png";
	}

	/**アップデート時の処理*/
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if(player.isPotionActive(CreeperPotion.exp.id))
		{
			player.removePotionEffect(CreeperPotion.exp.id);
		}

		 if(itemStack.isItemEnchanted()==false)
			{
			 	itemStack.addEnchantment(TEnchantment.enchantmentPE, 10);
			}
		 	player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 1200, 0));
				//ヘルメット
				if(player.getCurrentArmor(3) != null) {
					player.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 10, 0));
				}

				//チェストプレート
				if(player.getCurrentArmor(2) != null) {
					if(!player.isPotionActive(Potion.regeneration)) {
						player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 20, 2));
					}
				}

				//レギンス
				if(player.getCurrentArmor(1) != null) {
					player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 10, 2));
				}

				//ブーツ
				if(player.getCurrentArmor(0) != null) {
					player.addPotionEffect(new PotionEffect(Potion.jump.id, 10, 2));
				}
	}

	@Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean isHeld)
	{
       if(itemStack.isItemEnchanted()==false)
		{
			itemStack.addEnchantment(TEnchantment.enchantmentPE, 10);
		}


	}

	@Override
    public void onCreated(ItemStack itemStack, World p_77622_2_, EntityPlayer p_77622_3_)
	{
		itemStack.addEnchantment(TEnchantment.enchantmentPE, 10);
	}
}
