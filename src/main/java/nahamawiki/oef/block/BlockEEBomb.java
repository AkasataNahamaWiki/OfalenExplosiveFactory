package nahamawiki.oef.block;

import nahama.ofalenmod.core.OfalenModItemCore;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import takumicraft.Takumi.enchantment.TEnchantment;

public class BlockEEBomb extends BlockOEFBase {

	private static float power = 7F;

	public BlockEEBomb() {
		super();
		this.setHardness(0.001F);
		this.setResistance(0);
	}

	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int side, EntityPlayer player) {
		// プレイヤーが持っているアイテムを取得する。
		Item usingItem = null;
		if (player.getHeldItem() != null)
			usingItem = player.getHeldItem().getItem();
		if (!world.isRemote) {
			// サーバー側で、爆発する条件を満たしていたら爆発を起こす。
			if (usingItem != null) {
				if (EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, player.getHeldItem()) < 1) {
					if (EnchantmentHelper.getEnchantmentLevel(TEnchantment.enchantmentMS.effectId, player.getHeldItem()) < 1) {
						if (usingItem != OfalenModItemCore.toolPerfectOfalen) {
							world.createExplosion(null, x, y, z, power, true);
						}
					}
				}
			} else {
				world.createExplosion(null, x, y, z, power, true);
			}
		} else {
			world.createExplosion(null, x, y, z, 0, false);
		}
		super.onBlockHarvested(world, x, y, z, side, player);
	}

	@Override
	public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion p_149723_5_) {
		if (!world.isRemote)
			world.createExplosion(null, x, y, z, power, true);
	}

}
