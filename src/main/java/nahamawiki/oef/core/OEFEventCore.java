package nahamawiki.oef.core;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.relauncher.Side;
import nahamawiki.oef.OEFCore;
import nahamawiki.oef.entity.EntityRoboCreeper;
import nahamawiki.oef.item.IItemEEBatteryTool;
import nahamawiki.oef.item.armor.EEArmor;
import nahamawiki.oef.util.ControllerCreeperedMiner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

public class OEFEventCore {

	/** アイテム耐久値ゼロの時にフック */
	@SubscribeEvent
	public void EEItemBroken(PlayerDestroyItemEvent e) {
		if (e.original.getItem() instanceof IItemEEBatteryTool && !e.entity.worldObj.isRemote) {
			e.entity.worldObj.createExplosion(null, e.entity.posX, e.entity.posY, e.entity.posZ, 5, true);
			e.entity.attackEntityFrom(DamageSource.outOfWorld, 100);
		}
	}

	/** 爆発にフック */
	@SubscribeEvent
	public void ExpEvent(Detonate e) {
		if (e.explosion.exploder != null) {
			Entity entity = e.explosion.exploder;
			if (entity instanceof EntityPlayer) {
				if (((EntityPlayer) entity).getHeldItem() != null && ((EntityPlayer) entity).getHeldItem().getItem() == OEFItemCore.EESword_GREEN) {
					for (Entity hit : e.getAffectedEntities()) {
						if (hit instanceof EntityMob) {
							try {
								if (hit.getDataWatcher().getWatchableObjectByte(17) == 1) {
									hit.getDataWatcher().updateObject(17, (byte) 0);
									hit.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) entity), 30);
									hit.worldObj.createExplosion(null, hit.posX, hit.posY, hit.posZ, 0, false);
								}
							} catch (Throwable throwable) {}
						}
					}
				}
			} else if (entity instanceof EntityRoboCreeper) {
				if (((EntityRoboCreeper) entity).getType() == 1 || ((EntityRoboCreeper) entity).getType() == 3) {
					for (Entity hit : e.getAffectedEntities()) {
						if (hit instanceof EntityMob) {
							try {
								if (hit.getDataWatcher().getWatchableObjectByte(17) == 1) {
									hit.getDataWatcher().updateObject(17, (byte) 0);
									hit.worldObj.createExplosion(null, hit.posX, hit.posY, hit.posZ, 0, false);
								}
							} catch (Throwable throwable) {}
						}
					}
				}
			}
		}
	}

	/** プレイヤー負傷時にフック */
	@SubscribeEvent
	public void onHurt(LivingHurtEvent e) {
		boolean flg[] = new boolean[4];
		if (e.entityLiving != null && e.entityLiving instanceof EntityPlayer) {
			for (int i = 0; i < 4; i++) {
				if (((EntityPlayer) e.entityLiving).getCurrentArmor(i) != null)
					if (((EntityPlayer) e.entityLiving).getCurrentArmor(i).getItem() instanceof EEArmor) {
						flg[i] = true;
					}
			}

			if (flg[0] && flg[1] && flg[2] && flg[3]) {
				if (e.source != null && e.source != DamageSource.outOfWorld) {
					int i = e.entityLiving.getRNG().nextInt(4);
					((EntityPlayer) e.entityLiving).getCurrentArmor(i).damageItem(1, e.entityLiving);
					if (((EntityPlayer) e.entityLiving).getCurrentArmor(i).getItemDamage() == 0 && !e.entityLiving.worldObj.isRemote) {
						e.entity.worldObj.createExplosion(null, e.entity.posX, e.entity.posY, e.entity.posZ, 5, true);
						e.entity.attackEntityFrom(DamageSource.outOfWorld, 100);

					} else {
						e.setCanceled(true);
					}
				}
			}
		}
	}

	/** プレイヤーログイン時のイベント */
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		if (OEFCore.update != null) {
			OEFCore.update.notifyUpdate(event.player, Side.CLIENT);
		}
	}

	/** ブロック設置時のイベント */
	@SubscribeEvent
	public void onBlockPlaced(PlaceEvent event) {
		if (event.world.isRemote)
			return;
		if (!ControllerCreeperedMiner.getInstance(event.world).isInArea(event.x, event.y, event.z))
			return;
		// 匠化採掘機の採掘範囲内ならブロックの設置をキャンセルさせて爆発を起こす。
		event.setCanceled(true);
		// event.world.setBlockToAir(event.x, event.y, event.z);
		event.world.createExplosion(null, event.x + 0.5, event.y + 0.5, event.z + 0.5, 2, true);
	}

}
