package nahamawiki.oef.tileentity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nahama.ofalenmod.entity.EntityBlueLaser;
import nahama.ofalenmod.entity.EntityGreenLaser;
import nahama.ofalenmod.entity.EntityRedLaser;
import nahama.ofalenmod.entity.EntityWhiteLaser;
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
import net.minecraft.world.World;

public class TileEntityEECannon extends TileEntityEEMachineBase {

	protected int remainingTime;
	private int duration;
	/** 左右角 */
	private float rotationYaw;
	/** 上下角 */
	private float rotationPitch;
	private String color = "";
	private EntityLivingBase targetEntity;
	private String ownName;
	private int size;

	@Override
	public int getMachineType(int side) {
		return 2;
	}

	@Override
	public int recieveEE(int amount, int side) {
		remainingTime = 5;
		World world = this.worldObj;
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		for (Object entity : world.playerEntities) {
			if (entity instanceof EntityPlayer && ((EntityPlayer) entity).getDisplayName().equalsIgnoreCase(getOwnPlayer())) {
				player = (EntityPlayer) entity;
				world = player.worldObj;
				break;
			}
		}
		if (player != null) {
			String color = this.getColor();
			if (duration < 0 && color != null && this.getStack() > 0) {
				duration = 10;
				if (color.equals("Red")) {
					for (int i = -2; i < 3; i++) {
						EntityRedLaser laser = new EntityRedLaser(world, player, i);
						laser.setLocationAndAngles(xCoord, yCoord + 1D, zCoord, rotationYaw, rotationPitch);
						world.spawnEntityInWorld(laser);
					}
				} else if (color.equals("Green")) {
					EntityGreenLaser laser = new EntityGreenLaser(world, player);
					laser.setLocationAndAngles(xCoord, yCoord + 1D, zCoord, rotationYaw, rotationPitch);
					world.spawnEntityInWorld(laser);
				} else if (color.equals("Blue")) {
					EntityBlueLaser laser = new EntityBlueLaser(world, player);
					laser.setLocationAndAngles(xCoord, yCoord + 1D, zCoord, rotationYaw, rotationPitch);
					world.spawnEntityInWorld(laser);
				} else if (color.equals("White")) {
					for (int i = -2; i < 3; i++) {
						EntityWhiteLaser laser = new EntityWhiteLaser(world, player, i);
						laser.setLocationAndAngles(xCoord, yCoord + 1D, zCoord, rotationYaw, rotationPitch);
						world.spawnEntityInWorld(laser);
					}
				}
				this.size--;
			}
		}
		if (this.getStack() <= 0) {
			this.setColor("");
		}
		return amount;
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

	public int getStack() {
		return this.size;
	}

	public void setStack(int stack) {
		this.size = stack;
	}

	@Override
	public String[] getState() {
		return new String[] {
				StatCollector.translateToLocal("info.EEMachineState.name") + StatCollector.translateToLocal(this.getBlockType().getLocalizedName()),
				StatCollector.translateToLocal("info.EEMachineState.level") + this.getLevel(this.getBlockMetadata()),
		};
	}

	@Override
	public int getLevel(int meta) {
		return 0;
	}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote)
			return;
		if (duration > 0)
			duration--;
		EntityPlayer player = null;
		for (Object entity : worldObj.playerEntities) {
			if (entity instanceof EntityPlayer && ((EntityPlayer) entity).getDisplayName().equalsIgnoreCase(getOwnPlayer())) {
				player = (EntityPlayer) entity;
				break;
			}
		}
		if (player != null) {
			List list = this.worldObj.loadedEntityList;
			Collections.sort(list, new TileEntityEECannon.Sorter(player));
			if (!list.isEmpty()) {
				int i = 0;
				for (Object entity : list) {
					if (entity instanceof EntityMob) {
						this.targetEntity = (EntityLivingBase) list.get(i);
						break;
					} else {
						i++;
					}
				}
			}
		}
		this.angleUpdate();
	}

	protected void angleUpdate() {
		if (this.targetEntity != null) {
			float eYaw, ePitch;
			double eX, eY, eZ;
			eX = this.targetEntity.posX;
			eY = this.targetEntity.posY;
			eZ = this.targetEntity.posZ;

			eYaw = (float) Math.atan2(eX - this.xCoord, eZ - this.zCoord);

			double distance = Math.sqrt((eX - xCoord) * (eX - xCoord) + (eZ - zCoord) * (eZ - zCoord));
			ePitch = (float) Math.atan2(distance, eY - this.yCoord);

			if (this.rotationYaw > eYaw) {
				this.rotationYaw--;
			} else if (this.rotationYaw < eYaw) {
				this.rotationYaw++;
			}

			if (this.rotationPitch > ePitch) {
				this.rotationPitch--;
			} else if (this.rotationPitch < ePitch) {
				this.rotationPitch++;
			}
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setFloat("rotationYaw", rotationYaw);
		nbt.setFloat("rotationPitch", rotationPitch);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		NBTTagCompound nbt = pkt.func_148857_g();
		rotationYaw = nbt.getFloat("rotationYaw");
		rotationPitch = nbt.getFloat("rotationPitch");
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.setColor(nbt.getString("Color"));
		this.setOwnPlayer(nbt.getString("Player"));
		this.setStack(nbt.getInteger("Size"));

	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setString("Color", this.getColor());
		nbt.setString("Player", this.getOwnPlayer());
		nbt.setInteger("Size", this.getStack());
	}

	public static class Sorter implements Comparator {
		private final Entity theEntity;

		public Sorter(Entity p_i1662_1_) {
			this.theEntity = p_i1662_1_;
		}

		public int compare(Entity p_compare_1_, Entity p_compare_2_) {
			double d0 = this.theEntity.getDistanceSqToEntity(p_compare_1_);
			double d1 = this.theEntity.getDistanceSqToEntity(p_compare_2_);
			return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
		}

		@Override
		public int compare(Object p_compare_1_, Object p_compare_2_) {
			return this.compare((Entity) p_compare_1_, (Entity) p_compare_2_);
		}
	}

	public float getRotationPitch() {
		return rotationPitch;
	}

	public float getRotationYaw() {
		return rotationYaw;
	}

}
