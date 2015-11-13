package nahamawiki.oef.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.tileentity.TileEntityEEMachineBase;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAICreeperSwell;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import takumicraft.Takumi.TakumiCraftCore;
import takumicraft.Takumi.item.Entity.EntityAttackBlock;
import takumicraft.Takumi.mobs.ai.AB.EntityAIABCreeperSwell;

public class EntityEngineCreeper extends EntityCreeper {

	private int timeSinceIgnited;
	private int lastActiveTime;

	public EntityEngineCreeper(World world) {
		super(world);

		/* タスクにAIを追加する.
		 * 引数は(優先度, AIクラスのインスタンス) */

		/* 近接攻撃を行うAIを追加する.
		 * EntityAIAttackOnCollideの引数のうち, 末尾2つは(攻撃距離, ずっと追い続けるかどうか) */
		this.tasks.addTask(1, new EntityAISwimming(this));

		this.tasks.addTask(1, new EntityAICreeperSwell(this));
		this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.2D, true));
		/* うろうろ移動するAIの追加 */
		this.tasks.addTask(3, new EntityAIWander(this, 1.0D));

		/* 見回すAIの追加 */
		this.tasks.addTask(4, new EntityAILookIdle(this));

		/* ターゲットタスクにAIを追加する. */

		/* 攻撃されたら反撃するAIを追加する.
		 * EntityAIHurtByTargetの第二引数は反撃時に周りのMobに助けを求めるかどうか */
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));

		/* 周囲に特定のEntityがいる場合, ターゲッティングするAI.
		 * EntityAINearestAttackableTargetの最後の引数は視界で遮るかどうか.
		 * trueだとブロックに囲われた(ガラスブロックでも)村人はターゲットにならない.
		 * falseだとどんなブロックに囲まれていようとターゲットにする(ゾンビと同じ). */
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 1, false));
		this.targetTasks.addTask(4, new EntityAIHurtByTarget(this, false));

		this.tasks.addTask(1, new EntityAIABCreeperSwell(this));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityAttackBlock.class, 1, false));
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
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
	}

	/* このMobが生きてるときの音のファイルパスを返す. */
	@Override
	protected String getLivingSound() {
		return "";
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
		this.playSound("", 0.15F, 1.0F);
	}

	/* Mobの属性を返す.
	 * 今回はUNDEADにしているので, エンチャントのSmiteが有効. */
	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEFINED;
	}

	/* このMobの通常ドロップのアイテムを返すメソッド. */
	@Override
	protected Item getDropItem() {
		return Items.gunpowder;
	}

	@Override
	public void onUpdate() {

		if (this.isEntityAlive()) {
			if (this.worldObj.isThundering()) {
				this.dataWatcher.updateObject(17, Byte.valueOf((byte) 1));
			}
			this.lastActiveTime = this.timeSinceIgnited;

			if (this.func_146078_ca()) {
				this.setCreeperState(1);
			}
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

				int r = getPowered() ? 6 : 2;
				this.searchLight((int) this.posX, (int) this.posY, (int) this.posZ, r, this.worldObj);
				if (!worldObj.isRemote)
					this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, r, false);
				this.setDead();

			}
		}

		super.onUpdate();
	}

	protected void searchLight(int ox, int oy, int oz, int height, World par1World) {
		int blockX, blockY, blockZ;
		int searchRange = height;
		for (int x = -1 * searchRange; x < 1 * searchRange; ++x) {
			blockX = x + ox;
			for (int y = -1 * searchRange; y < 1 * searchRange; ++y) {
				blockY = y + oy;
				for (int z = -1 * searchRange; z < 1 * searchRange; ++z) {
					blockZ = z + oz;

					if (par1World.getTileEntity(blockX, blockY, blockZ) != null && par1World.getTileEntity(blockX, blockY, blockZ) instanceof TileEntityEEMachineBase) {
						((TileEntityEEMachineBase) par1World.getTileEntity(blockX, blockY, blockZ)).setCreeper(true);
						if (!worldObj.isRemote)
							par1World.createExplosion(this, blockX, blockY, blockZ, height, true);
					}

				}
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

	private int fuseTime = 40;
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
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float p_70070_1_) {
		return 15728880;
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness(float p_70013_1_) {
		return 50.0F;
	}

	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);
		if (source.getSourceOfDamage() instanceof EntityPlayer) {
			for (Object entity : worldObj.loadedEntityList) {
				if (entity instanceof EntityAttackBlock) {
					((EntityAttackBlock) entity).addTP(50);
					break;
				}
			}
		}
	}

}