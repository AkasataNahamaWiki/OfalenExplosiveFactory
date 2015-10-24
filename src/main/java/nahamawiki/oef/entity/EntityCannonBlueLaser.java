package nahamawiki.oef.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityCannonBlueLaser extends EntityCannonLaser {

	private int power = 5;

	public EntityCannonBlueLaser(World world, double x, double y, double z, float yaw, float pitch) {
		super(world, x, y, z, yaw, pitch);
	}

	@Override
	protected void onImpact(MovingObjectPosition position) {
		power--;

		if (position.entityHit != null) {
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			position.entityHit.attackEntityFrom(DamageSource.causePlayerDamage(player), 20.0F);
		}

		if (this.power <= 0) {
			this.setDead();
		}
	}

}
