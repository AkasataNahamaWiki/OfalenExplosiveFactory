package nahamawiki.oef.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import takumicraft.Takumi.TakumiCraftCore;
import takumicraft.Takumi.Potion.CreeperPotion;
import takumicraft.Takumi.Potion.PotionExplosion;

public class EntityCannonEPLaser extends EntityCannonLaser {

	private int power = 10;

	public EntityCannonEPLaser(EntityPlayer player, World world, double x, double y, double z, float yaw, float pitch) {
		super(player, world, x, y, z, yaw, pitch);
	}

	@Override
	protected void onImpact(MovingObjectPosition position) {
		if (this.worldObj.getBlock(position.blockX, position.blockY, position.blockZ).getBlockHardness(worldObj, position.blockX, position.blockY, position.blockZ) > 0 && this.worldObj.getBlock(position.blockX, position.blockY, position.blockZ) != TakumiCraftCore.EPBlock) {
			this.worldObj.setBlock(position.blockX, position.blockY, position.blockZ, TakumiCraftCore.EPBlock);
			this.power--;
		}

		else if (position.entityHit != null && position.entityHit instanceof EntityLivingBase) {
			((EntityLivingBase) position.entityHit).addPotionEffect(new PotionEffect(CreeperPotion.exp.id, PotionExplosion.sec * 20 + 1, 0));
			this.power--;
		}

		if (this.power <= 0) {
			this.setDead();
		}
	}

}
