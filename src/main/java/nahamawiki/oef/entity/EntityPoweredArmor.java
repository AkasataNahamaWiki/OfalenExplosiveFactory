package nahamawiki.oef.entity;

import nahamawiki.oef.item.armor.EEArmor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityPoweredArmor extends EntityLiving
{
	private EntityPlayer Owner;
	private  String name = "Player";

	public EntityPoweredArmor(World world)
	{
		super(world);
		this.setSize(0.05f, 0.05f);
	}

	public EntityPoweredArmor(World world , EntityPlayer player) {
		this(world);
		this.Owner = player;
		this.name = player.getDisplayName();

	}

	/**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float p_70069_1_) {}

	public void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(29, "");

	}
	  /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

	public void onUpdate()
	{

		ItemStack[] armor = new ItemStack[4];
			try
            {
            	if(this.worldObj.playerEntities != null)
            	{
            		for(Object entity : this.worldObj.playerEntities)
            		{
            			if(entity instanceof EntityPlayer && ((EntityPlayer) entity).getDisplayName().equalsIgnoreCase(this.getOwnerName()))
            			{
            				this.Owner = (EntityPlayer) entity;
            				break;
            			}
            		}
            	}
            }
			catch(Exception e){}
			finally{}
			if(this.Owner != null && !this.Owner.isDead)
			{
				this.ticksExisted = Owner.ticksExisted;
				for(int i = 0; i< 4; i++)
				{
					armor[i] = this.Owner.getCurrentArmor(i);
				}

				if(armor[0] != null &&
						armor[0].getItem() instanceof EEArmor &&
						armor[1] != null &&
						armor[1].getItem() instanceof EEArmor &&
						armor[2] != null &&
						armor[2].getItem() instanceof EEArmor &&
						armor[3] != null &&
						armor[3].getItem() instanceof EEArmor
						)
					{
						this.setPositionAndUpdate(this.Owner.posX, this.Owner.posY, this.Owner.posZ);
					}
					else
					{
						this.setDead();
					}

			}
		super.onUpdate();
	}

	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float p_70070_1_)
	{
	    return 15728880;
	}

	/**
	 * Gets how bright this entity is.
	 */
	public float getBrightness(float p_70013_1_)
	{
	    return 50.0F;
	}

	/**
     * Get the experience points the entity currently has.
     */
    protected int getExperiencePoints(EntityPlayer p_70693_1_)
    {
    	return 0;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage)
    {
    	return false;
    }

	public EntityPlayer getOwner()
	{
		return this.Owner;
	}

	public String getOwnerName()
	{
		return this.dataWatcher.getWatchableObjectString(29);
	}

	public void setOwnerName(String name)
	{
		this.dataWatcher.updateObject(29, name);
	}

	 /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setString("Owner", this.getOwnerName());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        this.setOwnerName(par1NBTTagCompound.getString("Owner"));
    }


}