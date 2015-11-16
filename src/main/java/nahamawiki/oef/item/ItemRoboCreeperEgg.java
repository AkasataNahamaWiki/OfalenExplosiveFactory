package nahamawiki.oef.item;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.OEFCore;
import nahamawiki.oef.entity.EntityEngineCreeper;
import nahamawiki.oef.entity.EntityRoboCreeper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemRoboCreeperEgg extends Item {

	// このスポーンエッグから生成されるエンティティのリスト
	public static Class[] spawnableEntities = {
			EntityRoboCreeper.class,
			EntityEngineCreeper.class
	};

	public ItemRoboCreeperEgg() {
		this.setHasSubtypes(true);
		this.setCreativeTab(OEFCore.tabOEF);
	}

	// ItemMonsterPlacerのspawnCreatureがstaticでオーバーライドできないので呼び出し側をコピペ
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float posX, float posY, float posZ) {
		if (world.isRemote)
			return true;
		Block block = world.getBlock(x, y, z);
		x += Facing.offsetsXForSide[side];
		y += Facing.offsetsYForSide[side];
		z += Facing.offsetsZForSide[side];
		double d0 = 0.0D;

		if (side == 1 && block.getRenderType() == 11) {
			d0 = 0.5D;
		}

		Entity entity = spawnCreature(world, itemStack.getItemDamage(), x + 0.5D, y + d0, z + 0.5D);

		if (entity != null) {
			if (entity instanceof EntityLivingBase && itemStack.hasDisplayName()) {
				((EntityLiving) entity).setCustomNameTag(itemStack.getDisplayName());
			}

			if (!player.capabilities.isCreativeMode) {
				--itemStack.stackSize;
			}
		}

		return true;
	}

	// ItemMonsterPlacerのspawnCreatureがstaticでオーバーライドできないので呼び出し側をコピペ
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (world.isRemote)
			return itemStack;
		MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);

		if (movingobjectposition == null) {
			return itemStack;
		} else {
			if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
				int i = movingobjectposition.blockX;
				int j = movingobjectposition.blockY;
				int k = movingobjectposition.blockZ;

				if (!world.canMineBlock(player, i, j, k)) {
					return itemStack;
				}

				if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, itemStack)) {
					return itemStack;
				}

				if (world.getBlock(i, j, k) instanceof BlockLiquid) {
					Entity entity = spawnCreature(world, itemStack.getItemDamage(), i, j, k);

					if (entity != null) {
						if (entity instanceof EntityLivingBase && itemStack.hasDisplayName()) {
							((EntityLiving) entity).setCustomNameTag(itemStack.getDisplayName());
						}

						if (!player.capabilities.isCreativeMode) {
							--itemStack.stackSize;
						}
					}
				}
			}
			return itemStack;
		}
	}

	// spawnableEntitiesのエンティティをスポーンさせるようにItemMonsterPlacerのspawnCreatureを改変
	public static Entity spawnCreature(World world, int par2, double x, double y, double z) {
		Class c = spawnableEntities[par2];
		Entity entity = null;
		try {
			entity = (Entity) c.getConstructor(new Class[] { World.class }).newInstance(new Object[] { world });

			EntityCreeper entityliving = (EntityCreeper) entity;
			entity.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F), 0.0F);
			entityliving.rotationYawHead = entityliving.rotationYaw;
			entityliving.renderYawOffset = entityliving.rotationYaw;
			entityliving.onSpawnWithEgg((IEntityLivingData) null);
			if (entityliving instanceof EntityRoboCreeper) {
				((EntityRoboCreeper) entityliving).setType(new Random().nextInt(4));
			}
			world.spawnEntityInWorld(entity);
			entityliving.playLivingSound();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return entity;
	}

	// spawnableEntitiesの各エンティティをスポーンさせるスポーンエッグを登録
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs par2, List par3) {
		for (int i = 0; i < spawnableEntities.length; ++i) {
			par3.add(new ItemStack(par1, 1, i));
		}
	}

	@Override
	public int getMetadata(int meta) {
		return meta;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return this.getUnlocalizedName() + "." + itemStack.getItemDamage();
	}

}
