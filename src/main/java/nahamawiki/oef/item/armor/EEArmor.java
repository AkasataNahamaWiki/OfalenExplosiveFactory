package nahamawiki.oef.item.armor;

import nahamawiki.oef.OEFCore;
import nahamawiki.oef.core.OEFItemCore;
import nahamawiki.oef.entity.EntityPoweredArmor;
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
	private int slotNo;

	public EEArmor(ArmorMaterial material, int par2) {
		super(material, 0, par2);
		this.slotNo = par2;
		this.setCreativeTab(OEFCore.tabOEF);

	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack itemStack, int pass) {
		return true;
	}

	/** テクスチャを指定する */
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		int i = 1;
		if (this.armorType == 2)
			i = 2;
		return OEFCore.DOMEINNAME + "textures/models/armor/eearmor_" + i + ".png";
	}

	/** アップデート時の処理 */
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {

		if (player.isPotionActive(CreeperPotion.exp.id)) {
			player.removePotionEffect(CreeperPotion.exp.id);
		}

		if (itemStack.isItemEnchanted() == false) {
			itemStack.addEnchantment(TEnchantment.enchantmentPE, 10);
		}

		player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 400, 0));
		// ヘルメット
		if (player.getCurrentArmor(3) != null) {
			player.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 10, 0));
		}

		// チェストプレート
		if (player.getCurrentArmor(2) != null && !player.isPotionActive(23)) {
			player.addPotionEffect(new PotionEffect(23, 1200, 0));
		}

		// レギンス
		if (player.getCurrentArmor(1) != null) {
			player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 10, 2));
		}

		// ブーツ
		if (player.getCurrentArmor(0) != null) {
			player.addPotionEffect(new PotionEffect(Potion.jump.id, 10, 2));
		}

		if (!world.isRemote && player.getCurrentArmor(0) != null && player.getCurrentArmor(1) != null &&
				player.getCurrentArmor(2) != null && player.getCurrentArmor(3) != null) {
			if(player.getCurrentArmor(3).getItem() == OEFItemCore.EEHelmet && player.getCurrentArmor(2).getItem() == OEFItemCore.EEChestPlate &&
					player.getCurrentArmor(1).getItem() == OEFItemCore.EELeggings && player.getCurrentArmor(0).getItem() == OEFItemCore.EEBoots)
			{
				boolean flg = true;
				if (player.worldObj.loadedEntityList != null) {
					for (Object entity : player.worldObj.loadedEntityList) {
						if (entity instanceof EntityPoweredArmor) {

							if (((EntityPoweredArmor) entity).getOwnerName().equalsIgnoreCase(player.getDisplayName())) {
								flg = false;
								((EntityPoweredArmor) entity).setOwnerName(player.getDisplayName());
							} else {
								((EntityPoweredArmor) entity).setDead();
							}
						}
					}
					if (flg) {
						EntityPoweredArmor armor = new EntityPoweredArmor(player.worldObj, player);
						armor.setPosition(player.posX, player.posY, player.posZ);
						player.worldObj.spawnEntityInWorld(armor);
					}
				}
			}
		}
	}

	public int getSlotNo() {
		return slotNo;
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean isHeld) {
		if (itemStack.isItemEnchanted() == false) {
			itemStack.addEnchantment(TEnchantment.enchantmentPE, 10);
		}
	}

	@Override
	public void onCreated(ItemStack itemStack, World p_77622_2_, EntityPlayer p_77622_3_) {
		itemStack.addEnchantment(TEnchantment.enchantmentPE, 10);
	}

}
