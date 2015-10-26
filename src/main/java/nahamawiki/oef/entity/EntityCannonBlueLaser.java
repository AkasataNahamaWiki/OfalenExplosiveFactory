package nahamawiki.oef.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityCannonBlueLaser extends EntityCannonLaser {

	private int power = 5;

	public EntityCannonBlueLaser(EntityPlayer player, World world, double x, double y, double z, float yaw, float pitch) {
		super(player, world, x, y, z, yaw, pitch);
	}

	@Override
	protected void onImpact(MovingObjectPosition position) {
		if (position.entityHit != null && position.entityHit != thrower) {
			power--;
			position.entityHit.attackEntityFrom(DamageSource.causePlayerDamage(thrower), 20.0F);
		}

		if (this.power <= 0) {
			this.setDead();
		}
	}

}
