package nahamawiki.oef.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.item.armor.EEArmor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityPoweredArmor extends EntityLiving {

	private EntityPlayer owner;
	private String ownerName = "";

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
		super.onUpdate();
		if (worldObj.isRemote)
			return;
		if (owner == null || owner.isDead) {
			this.setDead();
			return;
		}
		ticksExisted = owner.ticksExisted;
		ItemStack[] armor = new ItemStack[4];
		for (int i = 0; i < 4; i++) {
			armor[i] = owner.getCurrentArmor(i);
			if (armor[i] == null || !(armor[i].getItem() instanceof EEArmor)) {
				this.setDead();
				return;
			}
		}
		this.setPosition(owner.posX, owner.posY, owner.posZ);
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
