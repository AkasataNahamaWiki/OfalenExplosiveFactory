package nahamawiki.oef.entity;

import java.util.Random;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityCannonBoltLaser extends EntityCannonLaser {

	private int power = 1;

	public EntityCannonBoltLaser(EntityPlayer player, World world, double x, double y, double z, float yaw, float pitch) {
		super(player, world, x, y, z, yaw, pitch);
	}

	@Override
	protected void onImpact(MovingObjectPosition position) {
		for (int t = 0; t < 5; t++) {
			Random x = new Random();
			int sx = MathHelper.getRandomIntegerInRange(x, -5, 5);
			Random y = new Random();
			int sy = 0;
			Random z = new Random();
			int sz = MathHelper.getRandomIntegerInRange(z, -5, 5);

			EntityLightningBolt var17 = new EntityLightningBolt(this.worldObj, this.posX + sx, this.posY + sy, this.posZ + sz);
			this.worldObj.addWeatherEffect(var17);
			this.worldObj.spawnEntityInWorld(var17);
			if (!this.worldObj.isRemote)
				this.worldObj.createExplosion(this, this.posX + sx, this.posY + sy, this.posZ + sz, 3F, true);
		}
		this.setDead();
	}

}
