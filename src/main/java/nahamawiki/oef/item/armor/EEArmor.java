package nahamawiki.oef.item.armor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.OEFCore;
import nahamawiki.oef.core.OEFConfigCore;
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
		boolean flag[] = new boolean[4];

		if (player.isPotionActive(CreeperPotion.exp.id)) {
			player.removePotionEffect(CreeperPotion.exp.id);
		}

		if (itemStack.isItemEnchanted() == false) {
			itemStack.addEnchantment(TEnchantment.enchantmentPE, 10);
		}

		player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 400, 0));
		// ヘルメット
		if (player.getCurrentArmor(3) != null) {
			if (player.getCurrentArmor(3).getItem() instanceof EEArmor) {
				player.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 10, 0));
				flag[3] = true;
			}
		}

		// チェストプレート
		if (player.getCurrentArmor(2) != null) {
			if (player.getCurrentArmor(2).getItem() instanceof EEArmor) {
				player.addPotionEffect(new PotionEffect(23, 10, 0));
				flag[2] = true;
			}
		}

		// レギンス
		if (player.getCurrentArmor(1) != null) {
			if (player.getCurrentArmor(1).getItem() instanceof EEArmor) {
				player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 10, 2));
				flag[1] = true;
			}
		}

		// ブーツ
		if (player.getCurrentArmor(0) != null) {
			if (player.getCurrentArmor(0).getItem() instanceof EEArmor) {
				player.addPotionEffect(new PotionEffect(Potion.jump.id, 10, 2));
				flag[0] = true;
			}
		}

		if (world.isRemote || !OEFConfigCore.isArmorPowered)
			return;
		if (!flag[0] || !flag[1] || !flag[2] || !flag[3])
			return;
		boolean flag1 = false;
		if (world.loadedEntityList == null || world.loadedEntityList.isEmpty())
			return;
		for (Object entity : world.loadedEntityList) {
			if (!(entity instanceof EntityPoweredArmor))
				continue;
			if (((EntityPoweredArmor) entity).getOwnerName().equalsIgnoreCase(player.getCommandSenderName())) {
				flag1 = true;
				break;
			}
		}
		if (!flag1) {
			EntityPoweredArmor armor = new EntityPoweredArmor(world, player);
			armor.setPosition(player.posX, player.posY, player.posZ);
			world.spawnEntityInWorld(armor);
			OEFCore.logger.info("spawned armor");
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
