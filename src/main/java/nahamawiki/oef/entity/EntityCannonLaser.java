package nahamawiki.oef.entity;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.core.OEFBlockCore;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityCannonLaser extends Entity {

	protected int xTile = -1;
	protected int yTile = -1;
	protected int zTile = -1;
	protected Block inBlock;
	protected boolean inGround;
	public int throwableShake;
	protected EntityPlayer thrower;
	protected String throwerName;
	protected int ticksInGround;
	protected int ticksInAir;
	protected double startX;
	protected double startY;
	protected double startZ;

	public float startYaw;
	public float startPitch;

	@Override
	protected void entityInit() {}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double range) {
		double d1 = this.boundingBox.getAverageEdgeLength() * 4.0D;
		d1 *= 64.0D;
		return range < d1 * d1;
	}

	protected EntityCannonLaser(World world) {
		super(world);
	}

	public EntityCannonLaser(EntityPlayer player, World world, double x, double y, double z, float yaw, float pitch) {
		super(world);
		this.thrower = player;
		this.setSize(0.5F, 0.5F);
		this.setLocationAndAngles(x, y, z, yaw, pitch);
		this.startYaw = yaw;
		this.startPitch = pitch;
		/* this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		 * this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F); */
		// this.posY -= 0.10000000149011612D;
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

	protected float getSpeed() {
		return 0.15F;
	}

	/** IProjectileのオーバーライド。ディスペンサーなどで利用される。(?) */
	public void setThrowableHeading(double x, double y, double z, float speed, float par5) {
		float f2 = MathHelper.sqrt_double(x * x + y * y + z * z);
		x /= f2;
		y /= f2;
		z /= f2;
		// ランダム性をなくす。
		/* x += this.rand.nextGaussian() * 0.007499999832361937D * (double)par5;
		 * y += this.rand.nextGaussian() * 0.007499999832361937D * (double)par5;
		 * z += this.rand.nextGaussian() * 0.007499999832361937D * (double)par5; */
		x *= speed;
		y *= speed;
		z *= speed;
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		float f3 = MathHelper.sqrt_double(x * x + z * z);
		this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(x, z) * 180.0D / Math.PI);
		this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(y, f3) * 180.0D / Math.PI);
		this.ticksInGround = 0;
	}

	/** 速度の処理 */
	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(double x, double y, double z) {
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;

		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt_double(x * x + z * z);
			this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(x, z) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(y, f) * 180.0D / Math.PI);
		}
	}

	/** 更新時の処理 */
	@Override
	public void onUpdate() {
		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;
		// this.setAngles(startYaw, startPitch);

		super.onUpdate();

		if (this.throwableShake > 0) {
			--this.throwableShake;
		}

		if (this.inGround) {
			if (this.worldObj.getBlock(this.xTile, this.yTile, this.zTile) == this.inBlock) {
				++this.ticksInGround;

				if (this.ticksInGround == 1200) {
					this.setDead();
				}

				return;
			}

			this.inGround = false;
			this.ticksInGround = 0;
			this.ticksInAir = 0;
		} else {
			++this.ticksInAir;
		}

		Vec3 vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
		Vec3 vec31 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
		vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
		vec31 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

		if (movingobjectposition != null) {
			vec31 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
		}

		if (!this.worldObj.isRemote) {
			Entity entity = null;
			List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
			double d0 = 0.0D;
			// EntityLivingBase entitylivingbase = this.getThrower();

			for (int j = 0; j < list.size(); ++j) {
				Entity entity1 = (Entity) list.get(j);

				if (entity1.canBeCollidedWith() && (/* entity1 != entitylivingbase || */this.ticksInAir >= 5)) {
					float f = 0.3F;
					AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f, f, f);
					MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);

					if (movingobjectposition1 != null) {
						double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

						if (d1 < d0 || d0 == 0.0D) {
							entity = entity1;
							d0 = d1;
						}
					}
				}
			}

			if (entity != null) {
				movingobjectposition = new MovingObjectPosition(entity);
			}
		}

		if (movingobjectposition != null && this.worldObj.getBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ) != OEFBlockCore.EECannon) {
			if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.worldObj.getBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ) == Blocks.portal) {
				this.setInPortal();
			} else if (!(movingobjectposition.entityHit instanceof EntityEngineCreeper)) {
				this.onImpact(movingobjectposition);
			}
		}

		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

		for (this.rotationPitch = (float) (Math.atan2(this.motionY, f1) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
			;
		}

		while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
		this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
		float f2 = 0.99F;

		if (this.isInWater()) {
			for (int i = 0; i < 4; ++i) {
				float f4 = 0.25F;
				this.worldObj.spawnParticle("bubble", this.posX - this.motionX * f4, this.posY - this.motionY * f4, this.posZ - this.motionZ * f4, this.motionX, this.motionY, this.motionZ);
			}

			f2 = 0.8F;
		}

		if (Math.abs(this.startX - this.posX) > 200) {
			this.setDead();
		} else if (Math.abs(this.startY - this.posY) > 200) {
			this.setDead();
		} else if (Math.abs(this.startZ - this.posZ) > 200) {
			this.setDead();
		} else if (this.posY < 0 || this.posY > 255) {
			this.setDead();
		}

		this.motionX *= 1.2D;
		this.motionY *= 1.2D;
		this.motionZ *= 1.2D;

		this.setPosition(this.posX, this.posY, this.posZ);
	}

	protected void onImpact(MovingObjectPosition position) {}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setShort("XTile", (short) this.xTile);
		nbt.setShort("YTile", (short) this.yTile);
		nbt.setShort("ZTile", (short) this.zTile);
		nbt.setByte("InTile", (byte) Block.getIdFromBlock(this.inBlock));
		nbt.setByte("Shake", (byte) this.throwableShake);
		nbt.setByte("InGround", (byte) (this.inGround ? 1 : 0));

		if ((this.throwerName == null || this.throwerName.length() == 0) && this.thrower != null && this.thrower instanceof EntityPlayer) {
			this.throwerName = this.thrower.getCommandSenderName();
		}

		nbt.setString("OwnerName", this.throwerName == null ? "" : this.throwerName);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		this.xTile = nbt.getShort("XTile");
		this.yTile = nbt.getShort("YTile");
		this.zTile = nbt.getShort("ZTile");
		this.inBlock = Block.getBlockById(nbt.getByte("InTile") & 255);
		this.throwableShake = nbt.getByte("Shake") & 255;
		this.inGround = nbt.getByte("InGround") == 1;
		this.throwerName = nbt.getString("OwnerName");
		if (this.throwerName != null && this.throwerName.length() == 0) {
			this.throwerName = null;
		}
		if (throwerName != null)
			thrower = this.worldObj.getPlayerEntityByName(this.throwerName);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getShadowSize() {
		return 0.0F;
	}

	/* public EntityLivingBase getThrower() {
	 * if (this.thrower == null && this.throwerName != null && this.throwerName.length() > 0) {
	 * this.thrower = this.worldObj.getPlayerEntityByName(this.throwerName);
	 * }
	 * return this.thrower;
	 * } */

}
