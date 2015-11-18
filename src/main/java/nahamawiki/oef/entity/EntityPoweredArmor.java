package nahamawiki.oef.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.OEFCore;
import nahamawiki.oef.item.armor.EEArmor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityPoweredArmor extends EntityLiving {

	private EntityPlayer owner;
	private String ownerName;

	public EntityPoweredArmor(World world) {
		super(world);
		this.setSize(0.05F, 0.05F);
	}

	public EntityPoweredArmor(World world, EntityPlayer player) {
		this(world);
		owner = player;
		ownerName = player.getCommandSenderName();
	}

	@Override
	protected void fall(float p_70069_1_) {}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public void onUpdate() {
		if (worldObj.isRemote) {
			if (owner == null) {
				double distance = Double.MAX_VALUE;
				for (Object object : worldObj.playerEntities) {
					if (!(object instanceof EntityPlayer))
						continue;
					EntityPlayer player = (EntityPlayer) object;
					if (distance < player.getDistanceSqToEntity(this))
						continue;
					owner = player;
					ownerName = player.getCommandSenderName();
					OEFCore.logger.info("set name to " + ownerName);
				}
			}
			super.onUpdate();
			this.updateCoord();
			return;
		}
		if (owner == null || owner.isDead) {
			this.setDead();
			super.onUpdate();
			return;
		}
		ticksExisted = owner.ticksExisted;
		ItemStack[] armor = new ItemStack[4];
		for (int i = 0; i < 4; i++) {
			armor[i] = owner.getCurrentArmor(i);
			if (armor[i] == null || !(armor[i].getItem() instanceof EEArmor)) {
				this.setDead();
				super.onUpdate();
				return;
			}
		}
		super.onUpdate();
		this.updateCoord();
	}

	public void updateCoord() {
		if (owner == null) {
			if (ownerName == null) {
				OEFCore.logger.info("ownerName == null");
				return;
			}
			owner = worldObj.getPlayerEntityByName(ownerName);
		}
		this.prevPosX = owner.prevPosX;
		this.prevPosY = owner.prevPosY + 2;
		this.prevPosZ = owner.prevPosZ;
		this.posX = owner.posX;
		this.posY = owner.posY + 2;
		this.posZ = owner.posZ;
		this.lastTickPosX = owner.lastTickPosX;
		this.lastTickPosY = owner.lastTickPosY + 2;
		this.lastTickPosZ = owner.lastTickPosZ;
		this.motionX = owner.motionX;
		this.motionY = owner.motionY;
		this.motionZ = owner.motionZ;
		this.rotationPitch = 0;
		this.rotationYaw = 0;
	}

	@Override
	public void setDead() {
		OEFCore.logger.info("Set dead");
		super.setDead();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float p_70070_1_) {
		return 15728880;
	}

	@Override
	public float getBrightness(float p_70013_1_) {
		return 50.0F;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float damage) {
		return false;
	}

	@Override
	protected int getExperiencePoints(EntityPlayer p_70693_1_) {
		return 0;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		if ((ownerName == null || ownerName.length() == 0) && owner != null && owner instanceof EntityPlayer) {
			ownerName = owner.getCommandSenderName();
		}
		nbt.setString("OwnerName", ownerName == null ? "" : ownerName);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		ownerName = nbt.getString("OwnerName");
		if (ownerName != null && ownerName.length() == 0) {
			ownerName = null;
		}
		if (ownerName != null)
			owner = worldObj.getPlayerEntityByName(ownerName);
	}

	public EntityPlayer getOwner() {
		return owner;
	}

	public String getOwnerName() {
		return ownerName;
	}

}
