package nahamawiki.oef.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityCannonWhiteLaser extends EntityCannonLaser {

	private int power = 5;

	public EntityCannonWhiteLaser(EntityPlayer player, World world, double x, double y, double z, float yaw, float pitch, int dif) {
		this(world, x, y, z, yaw, pitch, dif);
		thrower = player;
	}

	public EntityCannonWhiteLaser(World world, double x, double y, double z, float yaw, float pitch, int dif) {
		super(world);
		this.setSize(0.5F, 0.5F);
		this.setLocationAndAngles(x, y, z, yaw + (dif * 5), pitch);
		/* this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		 * this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F); */
		this.posY -= 0.10000000149011612D;
		this.setPosition(this.posX, this.posY, this.posZ);
		this.startX = this.posX;
		this.startY = this.posY;
		this.startZ = this.posZ;
		this.yOffset = 0.0F;
		this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
		this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
		this.motionY = (-MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI));
		this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, this.getSpeed(), 1.0F);
	}

	@Override
	protected void onImpact(MovingObjectPosition position) {
		power--;

		if (position.entityHit != null) {
			position.entityHit.attackEntityFrom(DamageSource.causePlayerDamage(Minecraft.getMinecraft().thePlayer), 20.0F);
		}

		this.worldObj.createExplosion(thrower, this.posX, this.posY, this.posZ, 3, false);

		if (this.power <= 0) {
			this.setDead();
		}
	}

}
