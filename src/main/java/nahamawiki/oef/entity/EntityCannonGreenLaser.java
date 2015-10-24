package nahamawiki.oef.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityCannonGreenLaser extends EntityCannonLaser {

	public EntityCannonGreenLaser(EntityPlayer player, World world, double x, double y, double z, float yaw, float pitch) {
		this(world, x, y, z, yaw, pitch);
		thrower = player;
	}

	public EntityCannonGreenLaser(World world, double x, double y, double z, float yaw, float pitch) {
		super(world, x, y, z, yaw, pitch);
	}

	@Override
	protected void onImpact(MovingObjectPosition position) {
		if (position.entityHit != null) {
			position.entityHit.attackEntityFrom(DamageSource.causePlayerDamage(Minecraft.getMinecraft().thePlayer), 10.0F);
		}
		this.worldObj.createExplosion(thrower, this.posX, this.posY, this.posZ, 3, false);
		this.setDead();
	}

}
