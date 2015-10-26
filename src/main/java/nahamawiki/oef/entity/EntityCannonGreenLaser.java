package nahamawiki.oef.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityCannonGreenLaser extends EntityCannonLaser {

	public EntityCannonGreenLaser(EntityPlayer player, World world, double x, double y, double z, float yaw, float pitch) {
		super(player, world, x, y, z, yaw, pitch);
	}

	@Override
	protected void onImpact(MovingObjectPosition position) {
		if (position.entityHit != null && position.entityHit != thrower) {
			position.entityHit.attackEntityFrom(DamageSource.causePlayerDamage(thrower), 10.0F);
		}
		this.worldObj.createExplosion(thrower, this.posX, this.posY, this.posZ, 3, false);
		this.setDead();
	}

}
