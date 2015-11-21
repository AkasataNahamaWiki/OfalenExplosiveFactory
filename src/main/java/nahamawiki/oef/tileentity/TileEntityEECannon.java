package nahamawiki.oef.tileentity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import nahamawiki.oef.entity.EntityCannonBlueLaser;
import nahamawiki.oef.entity.EntityCannonBoltLaser;
import nahamawiki.oef.entity.EntityCannonEPLaser;
import nahamawiki.oef.entity.EntityCannonGreenLaser;
import nahamawiki.oef.entity.EntityCannonRedLaser;
import nahamawiki.oef.entity.EntityCannonWhiteLaser;
import nahamawiki.oef.entity.EntityEngineCreeper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;

public class TileEntityEECannon extends TileEntityEEMachineBase {

	protected boolean isSpawning;
	protected byte type;
	public int size;
	protected int duration;
	protected String color = "";
	protected String ownerName;
	protected EntityPlayer owner;
	protected EntityLivingBase targetEntity;
	/** 左右角 */
	protected float rotationYaw = 0;
	protected float prevRotationYaw;
	/** 上下角 */
	protected float rotationPitch = 0;
	protected float prevRotationPitch;

	@Override
	public String[] getState() {
		String color1 = "";
		if (color.length() > 0)
			color1 = StatCollector.translateToLocal("info.color:" + new String(color).toLowerCase());
		return new String[] {
				StatCollector.translateToLocal("info.EEMachineState.name") + StatCollector.translateToLocal(this.getBlockType().getLocalizedName()),
				StatCollector.translateToLocal("info.EEMachineState.capacity") + capacity + " EE",
				StatCollector.translateToLocal("info.EEMachineState.holding") + holdingEE + " EE",
				StatCollector.translateToLocal("info.EEMachineState.crystal") + color1,
				StatCollector.translateToLocal("info.EEMachineState.charged") + size
		};
	}

	@Override
	public byte getLevel(int meta) {
		return 0;
	}

	@Override
	public int getCapacity(int level) {
		return 800;
	}

	@Override
	public void updateMachine() {
		this.angleUpdate();
		prevRotationYaw = rotationYaw;
		prevRotationPitch = rotationPitch;
		if (duration > 0)
			duration--;
		if (holdingEE < 1)
			return;
		if (this.getOwner() == null)
			return;
		this.updateTarget(owner);
		if (duration < 1 && color != null && size > 0 && holdingEE >= 400 && targetEntity != null) {
			this.spawnLaser(color, owner);
			isSpawning = true;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			holdingEE -= 400;
			duration = 10;
			size--;
			if (size < 1) {
				this.setColor("");
			}
		}
	}

	@Override
	public void updateCreepered() {
		this.angleUpdate();
		prevRotationYaw = rotationYaw;
		prevRotationPitch = rotationPitch;
		if (duration > 0)
			duration--;
		if (targetEntity == null || targetEntity.isDead || !(targetEntity instanceof EntityPlayer)) {
			List list = worldObj.playerEntities;
			if (list.isEmpty()) {
				targetEntity = null;
				return;
			}
			for (Object entity : list) {
				if (entity instanceof EntityPlayer) {
					targetEntity = (EntityPlayer) entity;
					break;
				}
			}
		}
		if (targetEntity != null && targetEntity.isDead) {
			targetEntity = null;
		}
		if (duration < 1 && targetEntity != null) {
			if (this.getOwner() == null)
				owner = (EntityPlayer) targetEntity;
			duration = 10;
			Random random = new Random();
			if (random.nextBoolean()) {
				this.spawnLaser("Bolt", owner);
				type = 1;
			} else {
				this.spawnLaser("EP", owner);
				type = 2;
			}
			isSpawning = true;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	protected void angleUpdate() {
		if (targetEntity != null) {
			double x = targetEntity.posX - xCoord;
			double y = targetEntity.posY - yCoord;
			double z = targetEntity.posZ - zCoord;
			double distance = MathHelper.sqrt_double(x * x + z * z);
			float pYaw = (float) (Math.atan2(z, x) * (180.0D / Math.PI)) - 90.0F;
			float pPitch = (float) (-(Math.atan2(y, distance) * (180.0D / Math.PI)));
			rotationPitch = MathHelper.wrapAngleTo180_float(this.updateRotation(rotationPitch, pPitch, 360f));
			rotationYaw = MathHelper.wrapAngleTo180_float(this.updateRotation(rotationYaw, pYaw, 360f));

			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	private float updateRotation(float par1, float par2, float par3) {
		float f3 = MathHelper.wrapAngleTo180_float(par2 - par1);
		if (f3 > par3) {
			f3 = -par3;
		}
		if (f3 < -par3) {
			f3 = par3;
		}
		return par1 + f3;
	}

	private void updateTarget(EntityPlayer player) {
		if (targetEntity == null || targetEntity.isDead) {
			List list = worldObj.loadedEntityList;
			Collections.sort(list, new TileEntityEECannon.Sorter(player));
			if (!list.isEmpty()) {
				for (Object entity : list) {
					if (entity instanceof EntityMob && !(entity instanceof EntityEngineCreeper)) {
						targetEntity = (EntityLivingBase) entity;
						break;
					}
				}
			}
		}
		if (targetEntity != null && targetEntity.isDead) {
			targetEntity = null;
		}
	}

	protected void spawnLaser(String color, EntityPlayer player) {
		if (player == null)
			return;
		if (color.equals("Red")) {
			for (int i = -2; i < 3; i++) {
				EntityCannonRedLaser laser = new EntityCannonRedLaser(player, worldObj, xCoord + 0.5, yCoord + 0.65, zCoord + 0.5, rotationYaw, rotationPitch, i);
				worldObj.spawnEntityInWorld(laser);
			}
		} else if (color.equals("Green")) {
			EntityCannonGreenLaser laser = new EntityCannonGreenLaser(player, worldObj, xCoord + 0.5, yCoord + 0.65, zCoord + 0.5, rotationYaw, rotationPitch);
			worldObj.spawnEntityInWorld(laser);
		} else if (color.equals("Blue")) {
			EntityCannonBlueLaser laser = new EntityCannonBlueLaser(player, worldObj, xCoord + 0.5, yCoord + 0.65, zCoord + 0.5, rotationYaw, rotationPitch);
			worldObj.spawnEntityInWorld(laser);
		} else if (color.equals("White")) {
			for (int i = -2; i < 3; i++) {
				EntityCannonWhiteLaser laser = new EntityCannonWhiteLaser(player, worldObj, xCoord + 0.5, yCoord + 0.65, zCoord + 0.5, rotationYaw, rotationPitch, i);
				worldObj.spawnEntityInWorld(laser);
			}
		} else if (color.equals("Bolt")) {
			EntityCannonBoltLaser laser = new EntityCannonBoltLaser(player, worldObj, xCoord + 0.5, yCoord + 0.65, zCoord + 0.5, rotationYaw, rotationPitch);
			worldObj.spawnEntityInWorld(laser);
		} else if (color.equals("EP")) {
			EntityCannonEPLaser laser = new EntityCannonEPLaser(player, worldObj, xCoord + 0.5, yCoord + 0.65, zCoord + 0.5, rotationYaw, rotationPitch);
			worldObj.spawnEntityInWorld(laser);
		}
	}

	@Override
	public void setCreeper(boolean flag) {
		if (flag == isCreeper)
			return;
		if (!worldObj.isRemote && !flag) {
			if (targetEntity != null && targetEntity instanceof EntityPlayer) {
				targetEntity = null;
			}
		}
		super.setCreeper(flag);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setString("Color", color);
		nbt.setInteger("Duration", duration);
		nbt.setInteger("Size", size);
		nbt.setFloat("RotationYaw", rotationYaw);
		nbt.setFloat("RotationPitch", rotationPitch);
		if ((ownerName == null || ownerName.length() == 0) && owner != null) {
			ownerName = owner.getCommandSenderName();
		}
		nbt.setString("OwnerName", ownerName == null ? "" : ownerName);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.setColor(nbt.getString("Color"));
		duration = nbt.getInteger("Duration");
		size = nbt.getInteger("Size");
		rotationYaw = nbt.getFloat("RotationYaw");
		rotationPitch = nbt.getFloat("RotationPitch");
		ownerName = nbt.getString("OwnerName");
		if (ownerName != null && ownerName.length() == 0) {
			ownerName = null;
		}
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		if (isSpawning) {
			nbt.setBoolean("IsSpawning", true);
			nbt.setString("Color", color);
			isSpawning = false;
			if (this.getCreeper()) {
				nbt.setBoolean("IsCreeper", true);
				nbt.setByte("Type", type);
			}
			if ((ownerName == null || ownerName.length() == 0) && owner != null) {
				ownerName = owner.getCommandSenderName();
			}
			nbt.setString("OwnerName", ownerName == null ? "" : ownerName);
		}
		nbt.setFloat("RotationYaw", rotationYaw);
		nbt.setFloat("RotationPitch", rotationPitch);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		NBTTagCompound nbt = pkt.func_148857_g();
		rotationYaw = nbt.getFloat("RotationYaw");
		rotationPitch = nbt.getFloat("RotationPitch");
		ownerName = nbt.getString("OwnerName");
		this.setColor(nbt.getString("Color"));
		isCreeper = nbt.getBoolean("IsCreeper");
		type = nbt.getByte("Type");
		if (ownerName != null && ownerName.length() == 0) {
			ownerName = null;
		}
		if (nbt.getBoolean("IsSpawning")) {
			String s = color;
			if (isCreeper) {
				if (type == 1) {
					s = "Bolt";
				} else if (type == 2) {
					s = "EP";
				} else {
					s = "";
				}
			}
			this.spawnLaser(s, this.getOwner());
		}
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public EntityPlayer getOwner() {
		if (owner == null && ownerName != null && ownerName.length() > 0) {
			owner = worldObj.getPlayerEntityByName(ownerName);
		}
		return owner;
	}

	public void setOwner(EntityPlayer player) {
		owner = player;
	}

	public static class Sorter implements Comparator {
		private final Entity theEntity;

		public Sorter(Entity entity) {
			theEntity = entity;
		}

		public int compare(Entity entity1, Entity entity2) {
			double d0 = theEntity.getDistanceSqToEntity(entity1);
			double d1 = theEntity.getDistanceSqToEntity(entity2);
			return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
		}

		@Override
		public int compare(Object par1, Object par2) {
			return this.compare((Entity) par1, (Entity) par2);
		}
	}

	public float getRotationPitch() {
		return rotationPitch;
	}

	public float getPrevRotationPitch() {
		return prevRotationPitch;
	}

	public float getRotationYaw() {
		return rotationYaw;
	}

	public float getPrevRotationYaw() {
		return prevRotationYaw;
	}

}
