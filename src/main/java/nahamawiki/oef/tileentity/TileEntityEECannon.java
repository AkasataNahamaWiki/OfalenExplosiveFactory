package nahamawiki.oef.tileentity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nahama.ofalenmod.entity.EntityBlueLaser;
import nahama.ofalenmod.entity.EntityGreenLaser;
import nahama.ofalenmod.entity.EntityRedLaser;
import nahama.ofalenmod.entity.EntityWhiteLaser;
import nahamawiki.oef.OEFCore;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.StatCollector;

public class TileEntityEECannon extends TileEntityEEMachineBase {

	private final int capacity = 800;
	private int duration;
	public String color = "";
	private String ownName;
	private EntityLivingBase targetEntity;
	public int size;
	/** 左右角 */
	private float rotationYaw = 0;
	/** 上下角 */
	private float rotationPitch = 0;
	private boolean isSpawning;

	@Override
	public int getMachineType(int side) {
		return 2;
	}

	@Override
	public int recieveEE(int amount, int side) {
		holdingEE += amount;
		if (holdingEE > capacity) {
			int surplus = holdingEE - capacity;
			holdingEE = capacity;
			return surplus;
		}
		return 0;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getOwnPlayer() {
		return this.ownName;
	}

	public void setOwnPlayer(String name) {
		this.ownName = name;
	}

	@Override
	public String[] getState() {
		return new String[] {
				StatCollector.translateToLocal("info.EEMachineState.name") + StatCollector.translateToLocal(this.getBlockType().getLocalizedName()),
				StatCollector.translateToLocal("info.EEMachineState.capacity") + capacity + " EE",
				StatCollector.translateToLocal("info.EEMachineState.holding") + holdingEE + " EE",
				StatCollector.translateToLocal("info.EEMachineState.color") + color,
				StatCollector.translateToLocal("info.EEMachineState.charged") + size
		};
	}

	@Override
	public byte getLevel(int meta) {
		return 0;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (worldObj.isRemote)
			return;
		if (duration > 0)
			duration--;
		if (holdingEE < 1)
			return;
		EntityPlayer player = null;
		for (Object entity : worldObj.playerEntities) {
			if (entity instanceof EntityPlayer && ((EntityPlayer) entity).getDisplayName().equalsIgnoreCase(getOwnPlayer())) {
				player = (EntityPlayer) entity;
				break;
			}
		}
		if (player == null) {
			OEFCore.logger.error("player == null");
			return;
		}
		List list = this.worldObj.loadedEntityList;
		Collections.sort(list, new TileEntityEECannon.Sorter(player));
		if (!list.isEmpty()) {
			for (Object entity : list) {
				if (entity instanceof EntityMob) {
					this.targetEntity = (EntityLivingBase) entity;
					break;
				}
			}
		}
		this.angleUpdate();
		if (duration < 1 && color != null && size > 0 && holdingEE >= 400 && targetEntity != null) {
			duration = 10;
			NBTTagCompound localnbt = new NBTTagCompound();
			player.writeToNBT(localnbt);
			player.setLocationAndAngles(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, rotationYaw, rotationPitch);
			if (color.equals("Red")) {
				for (int i = -2; i < 3; i++) {
					EntityRedLaser laser = new EntityRedLaser(worldObj, player, i);
					worldObj.spawnEntityInWorld(laser);
				}
			} else if (color.equals("Green")) {
				EntityGreenLaser laser = new EntityGreenLaser(worldObj, player);
				worldObj.spawnEntityInWorld(laser);
			} else if (color.equals("Blue")) {
				EntityBlueLaser laser = new EntityBlueLaser(worldObj, player);
				worldObj.spawnEntityInWorld(laser);
			} else if (color.equals("White")) {
				for (int i = -2; i < 3; i++) {
					EntityWhiteLaser laser = new EntityWhiteLaser(worldObj, player, i);
					worldObj.spawnEntityInWorld(laser);
				}
			}
			player.readFromNBT(localnbt);
			isSpawning = true;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			this.size--;
			holdingEE -= 400;
			if (size < 1) {
				this.setColor("");
			}
		}
	}

	protected void angleUpdate() {
		if (this.targetEntity != null) {
			float eYaw, ePitch;
			double eX = this.targetEntity.posX;
			double eY = this.targetEntity.posY;
			double eZ = this.targetEntity.posZ;

			eYaw = (float) Math.atan2(eX - this.xCoord, eZ - this.zCoord);
			double distance = Math.sqrt((eX - xCoord) * (eX - xCoord) + (eZ - zCoord) * (eZ - zCoord));
			ePitch = (float) Math.atan2(distance, eY - this.yCoord);

			if (this.rotationYaw > eYaw) {
				this.rotationYaw -= 0.01;
			} else if (this.rotationYaw < eYaw) {
				this.rotationYaw += 0.01;
			}

			if (rotationPitch > ePitch) {
				this.rotationPitch -= 0.01;
			} else if (rotationPitch < ePitch) {
				this.rotationPitch += 0.01;
			}
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		if (isSpawning) {
			nbt.setBoolean("isSpawning", true);
			nbt.setString("color", color);
			isSpawning = false;
		} else {
			nbt.setBoolean("isSpawning", false);
		}
		nbt.setFloat("rotationYaw", rotationYaw);
		nbt.setFloat("rotationPitch", rotationPitch);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		NBTTagCompound nbt = pkt.func_148857_g();
		rotationYaw = nbt.getFloat("rotationYaw");
		rotationPitch = nbt.getFloat("rotationPitch");
		if (nbt.getBoolean("isSpawning")) {
			color = nbt.getString("color");
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			NBTTagCompound localnbt = new NBTTagCompound();
			player.writeToNBT(localnbt);
			player.setLocationAndAngles(xCoord + 0.5, yCoord + 0.5 - player.getEyeHeight(), zCoord + 0.5, rotationYaw, rotationPitch);
			if (color.equals("Red")) {
				for (int i = -2; i < 3; i++) {
					EntityRedLaser laser = new EntityRedLaser(worldObj, player, i);
					worldObj.spawnEntityInWorld(laser);
				}
			} else if (color.equals("Green")) {
				EntityGreenLaser laser = new EntityGreenLaser(worldObj, player);
				worldObj.spawnEntityInWorld(laser);
			} else if (color.equals("Blue")) {
				EntityBlueLaser laser = new EntityBlueLaser(worldObj, player);
				worldObj.spawnEntityInWorld(laser);
			} else if (color.equals("White")) {
				for (int i = -2; i < 3; i++) {
					EntityWhiteLaser laser = new EntityWhiteLaser(worldObj, player, i);
					worldObj.spawnEntityInWorld(laser);
				}
			}
			player.readFromNBT(localnbt);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.setColor(nbt.getString("Color"));
		this.setOwnPlayer(nbt.getString("Player"));
		size = nbt.getInteger("Size");
		rotationYaw = nbt.getFloat("rotationYaw");
		rotationPitch = nbt.getFloat("rotationPitch");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setString("Color", this.getColor());
		nbt.setString("Player", this.getOwnPlayer());
		nbt.setInteger("Size", size);
		nbt.setFloat("rotationYaw", rotationYaw);
		nbt.setFloat("rotationPitch", rotationPitch);
	}

	public static class Sorter implements Comparator {
		private final Entity theEntity;

		public Sorter(Entity entity) {
			this.theEntity = entity;
		}

		public int compare(Entity entity1, Entity entity2) {
			double d0 = this.theEntity.getDistanceSqToEntity(entity1);
			double d1 = this.theEntity.getDistanceSqToEntity(entity2);
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

	public float getRotationYaw() {
		return rotationYaw;
	}

}
