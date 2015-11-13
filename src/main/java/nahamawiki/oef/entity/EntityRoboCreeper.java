package nahamawiki.oef.entity;

import java.util.Random;

import nahamawiki.oef.core.OEFBlockCore;
import nahamawiki.oef.entity.ai.EntityAIRoboNearestAttackableTarget;
import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAICreeperSwell;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import takumicraft.Takumi.TakumiCraftCore;
import takumicraft.Takumi.item.Entity.EntityExplosionBall;

public class EntityRoboCreeper extends EntityCreeper {

	private int timeSinceIgnited;
	private int lastActiveTime;

	/**
	 * 0...red
	 * 1...green
	 * 2...blue
	 * 3...white
	 */
	public EntityRoboCreeper(World world) {

		super(world);

		/* タスクにAIを追加する.
		 * 引数は(優先度, AIクラスのインスタンス) */

		/* 近接攻撃を行うAIを追加する.
		 * EntityAIAttackOnCollideの引数のうち, 末尾2つは(攻撃距離, ずっと追い続けるかどうか) */
		this.tasks.addTask(1, new EntityAICreeperSwell(this));
		// this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityMob.class, 1.2D, false));

		/* うろうろ移動するAIの追加 */
		this.tasks.addTask(4, new EntityAIWander(this, 1.0D));

		/* 見回すAIの追加 */
		this.tasks.addTask(5, new EntityAILookIdle(this));

		/* ターゲットタスクにAIを追加する. */

		/* 攻撃されたら反撃するAIを追加する.
		 * EntityAIHurtByTargetの第二引数は反撃時に周りのMobに助けを求めるかどうか */
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));

		/* 周囲に特定のEntityがいる場合, ターゲッティングするAI.
		 * EntityAINearestAttackableTargetの最後の引数は視界で遮るかどうか.
		 * trueだとブロックに囲われた(ガラスブロックでも)村人はターゲットにならない.
		 * falseだとどんなブロックに囲まれていようとターゲットにする(ゾンビと同じ). */
		this.targetTasks.addTask(2, new EntityAIRoboNearestAttackableTarget(this, EntityMob.class, 1, true));
	}

	/* AIを使うかどうか.
	 * 今回は使うのでtrueを返している. */
	@Override
	public boolean isAIEnabled() {
		return TakumiCraftCore.AI;
	}

	/* このEntityに性質を付与する.
	 * 今回は移動速度を変更. */
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(this.getType() == 1 ? 1 : 0.2d);
	}

	@Override
	public void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(19, Byte.valueOf((byte) 0));
	}

	/* このMobが生きてるときの音のファイルパスを返す. */
	@Override
	protected String getLivingSound() {
		return "mob.creeper.say";
	}

	/* このMobがダメージを受けたときの音のファイルパスを返す. */
	@Override
	protected String getHurtSound() {
		return "mob.creeeper.hurt";
	}

	/* このMobが倒されたときの音のファイルパスを返す. */
	@Override
	protected String getDeathSound() {
		return "mob.creeper.death";
	}

	/* このMobが動いているときの音のファイルパスを返す.
	 * 引数のblockはMobの下にあるBlock. */
	@Override
	protected void func_145780_a(int x, int y, int z, Block block) {
		this.playSound("mob.creeper.step", 0.15F, 1.0F);
	}

	/* このMobの通常ドロップのアイテムを返すメソッド. */
	@Override
	protected Item getDropItem() {
		return null;
	}

	@Override
	public void onUpdate() {
		if (this.isEntityAlive()) {

			if (this.worldObj.isThundering()) {
				this.dataWatcher.updateObject(17, Byte.valueOf((byte) 1));
			}

			if (this.func_146078_ca()) {
				this.setCreeperState(1);
			}
			this.lastActiveTime = this.timeSinceIgnited;
			int i = this.getCreeperState();

			if (i > 0 && this.timeSinceIgnited == 0) {
				this.playSound("random.fuse", 1.0F, 0.5F);
			}

			this.timeSinceIgnited += i;

			if (this.timeSinceIgnited < 0) {
				this.timeSinceIgnited = 0;
			}

			if (this.timeSinceIgnited >= 30) {
				this.timeSinceIgnited = 30;

				// for (i = 0; i < 200; i++)
				// {
				// Entity splash = new EntityTofuSplash(worldObj, this);
				// worldObj.spawnEntityInWorld(splash);
				// }

				int r = getPowered() ? 3 : 1;
				if (!worldObj.isRemote) {
					this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 0, false);
					this.explode((int) this.posX, (int) this.posY, (int) this.posZ, r, this.worldObj);
				}
				this.setDead();
			}
		}

		super.onUpdate();
	}

	protected void explode(int ox, int oy, int oz, int height, World par1World) {
		switch (this.getType()) {
		case 0: {
			if (this.getAttackTarget() != null) {
				double dx = this.posX - this.getAttackTarget().posX;
				double dz = this.posZ - this.getAttackTarget().posZ;
				if (dx < dz) {
					for (int x = (int) this.getAttackTarget().posX - 2; x < this.getAttackTarget().posX + 3; x++) {
						int z = (int) this.getAttackTarget().posZ;
						for (int y = (int) this.getAttackTarget().posY; y < this.getAttackTarget().posY + 4; y++) {
							this.worldObj.createExplosion(this, x, y, z, 0.5F, false);
							this.worldObj.setBlock(x, y, z, OEFBlockCore.EESwordWall);
						}
					}
				} else {
					for (int z = (int) this.getAttackTarget().posZ - 2; z < this.getAttackTarget().posZ + 3; z++) {
						int x = (int) this.getAttackTarget().posX;
						for (int y = (int) this.getAttackTarget().posY; y < this.getAttackTarget().posY + 4; y++) {
							this.worldObj.createExplosion(this, x, y, z, 0.5F, false);
							this.worldObj.setBlock(x, y, z, OEFBlockCore.EESwordWall);
						}
					}
				}
			}
			break;
		}

		case 1: {
			this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 3, true);
			break;
		}

		case 2: {
			this.worldObj.playSoundAtEntity(this, "random.bow", 0.5F, 0.4F / (rand.nextFloat() * 0.4F + 0.8F));
			for (int i = 0; i < 5; ++i) {
				this.worldObj.spawnEntityInWorld(new EntityExplosionBall(this.worldObj, this, 1, 2, true));
			}
			break;
		}

		case 3: {
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

			break;
		}
		}
	}

	@Override
	public float getCreeperFlashIntensity(float par1) {
		return (this.lastActiveTime + (this.timeSinceIgnited - this.lastActiveTime) * par1) / (30 - 2);
	}

	@Override
	protected void fall(float par1) {
		super.fall(par1);
		this.timeSinceIgnited = (int) (this.timeSinceIgnited + par1 * 1.5F);

		if (this.timeSinceIgnited > 30 - 5) {
			this.timeSinceIgnited = 30 - 5;
		}
	}

	private int fuseTime = 30;
	private float explosionRadius = 3F;

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);

		if (this.dataWatcher.getWatchableObjectByte(17) == 1) {
			par1NBTTagCompound.setBoolean("powered", true);
		}
		par1NBTTagCompound.setByte("Type", (byte) this.getType());

		par1NBTTagCompound.setShort("Fuse", (short) this.fuseTime);
		par1NBTTagCompound.setFloat("ExplosionRadius", this.explosionRadius);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
		this.dataWatcher.updateObject(17, Byte.valueOf((byte) (par1NBTTagCompound.getBoolean("powered") ? 1 : 0)));

		if (par1NBTTagCompound.hasKey("Fuse")) {
			this.fuseTime = par1NBTTagCompound.getShort("Fuse");
		}

		this.dataWatcher.updateObject(19, Byte.valueOf(par1NBTTagCompound.getByte("Type")));

		if (par1NBTTagCompound.hasKey("ExplosionRadius")) {
			this.explosionRadius = par1NBTTagCompound.getFloat("ExplosionRadius");
		}
	}

	/**
	 * Returns true if the creeper is powered by a lightning bolt.
	 */
	@Override
	public boolean getPowered() {
		return this.dataWatcher.getWatchableObjectByte(17) == 1;
	}

	@Override
	public void onStruckByLightning(EntityLightningBolt par1EntityLightningBolt) {
		// super.onStruckByLightning(par1EntityLightningBolt);
		this.dealFireDamage(1);

		this.dataWatcher.updateObject(17, Byte.valueOf((byte) 1));
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float damage) {
		if (source.isFireDamage() == true || source.getDamageType() == "lava") {
			return false;
		} else {
			return super.attackEntityFrom(source, damage);
		}
	}

	public int getType() {
		return this.dataWatcher.getWatchableObjectByte(19);
	}

	public void setType(int type) {
		this.dataWatcher.updateObject(19, Byte.valueOf((byte) type));
	}
}