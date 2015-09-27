package nahamawiki.oef.core;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import nahamawiki.oef.item.ItemEESword;
import nahamawiki.oef.item.armor.EEArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

public class OEFEventCore {

	/** アイテム耐久値ゼロの時にフック */
	@SubscribeEvent
	public void EEItemBroken(PlayerDestroyItemEvent e) {
		if (e.original.getItem() instanceof ItemEESword || e.original.getItem() instanceof EEArmor) {
			e.entity.worldObj.createExplosion(null, e.entity.posX, e.entity.posY, e.entity.posZ, 5, true);
			e.entity.setDead();
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
							} catch (Throwable throwable) {} finally {

							}
						}
					}
				}
			}
		}
	}

	/** プレイヤーの描画にフック */
	/* private static final ResourceLocation texture_armor = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
	 * private final ModelBase armorModel = new ModelCreeper();
	 * private ModelBiped modelArmorChestplate;
	 * private ModelBiped modelArmor;
	 * @SideOnly(Side.CLIENT)
	 * @SubscribeEvent
	 * public void EEArmorEvent(RenderPlayerEvent.SetArmorModel e)
	 * {
	 * this.modelArmorChestplate = new ModelBiped(1.0F);
	 * this.modelArmor = new ModelBiped(0.5F);
	 * if(e.entityPlayer.getCurrentArmor(e.slot) != null && FMLCommonHandler.instance().getSide() == Side.CLIENT)
	 * {
	 * Item item = e.entityPlayer.getCurrentArmor(e.slot).getItem();
	 * if (item instanceof ItemArmor)
	 * {
	 * ItemArmor itemarmor = (ItemArmor)item;
	 * RenderManager manager = RenderManager.instance;
	 * e.renderer.setRenderManager(manager);
	 * manager.renderEngine.bindTexture(RenderBiped.getArmorResource(e.entityPlayer, e.entityPlayer.getCurrentArmor(e.slot), e.slot, null));
	 * ModelBiped modelbiped = e.slot == 2 ? this.modelArmor : this.modelArmorChestplate;
	 * modelbiped.bipedHead.showModel = e.slot == 0;
	 * modelbiped.bipedHeadwear.showModel = e.slot == 0;
	 * modelbiped.bipedBody.showModel = e.slot == 1 || e.slot == 2;
	 * modelbiped.bipedRightArm.showModel = e.slot == 1;
	 * modelbiped.bipedLeftArm.showModel = e.slot == 1;
	 * modelbiped.bipedRightLeg.showModel = e.slot == 2 || e.slot == 3;
	 * modelbiped.bipedLeftLeg.showModel = e.slot == 2 || e.slot == 3;
	 * float f1 = (float)e.entityPlayer.ticksExisted + e.partialRenderTick;
	 * manager.renderEngine.bindTexture(texture_armor);
	 * GL11.glMatrixMode(GL11.GL_TEXTURE);
	 * GL11.glLoadIdentity();
	 * float f2 = f1 * 0.01F;
	 * float f3 = f1 * 0.01F;
	 * GL11.glTranslatef(f2, f3, 0.0F);
	 * e.renderer.setRenderPassModel(modelbiped);
	 * GL11.glMatrixMode(GL11.GL_MODELVIEW);
	 * GL11.glEnable(GL11.GL_BLEND);
	 * float f4 = 0.5F;
	 * GL11.glColor4f(f4, f4, f4, 1.0F);
	 * GL11.glDisable(GL11.GL_LIGHTING);
	 * GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
	 * }
	 * }
	 * } */

	/** プレイヤー負傷時にフック */
	@SubscribeEvent
	public void onHurt(LivingHurtEvent e) {
		boolean flg = false;
		if (e.entityLiving != null && e.entityLiving instanceof EntityPlayer) {
			for (int i = 0; i < 4; i++) {
				if (((EntityPlayer) e.entityLiving).getCurrentArmor(i) != null)
					if (((EntityPlayer) e.entityLiving).getCurrentArmor(i).getItem() instanceof EEArmor) {
						flg = true;
						break;
					}
			}

			if (flg) {
				if (e.source != null && e.source != DamageSource.outOfWorld) {
					int i = e.entityLiving.getRNG().nextInt(4);
					((EntityPlayer) e.entityLiving).getCurrentArmor(i).damageItem(1, e.entityLiving);
					e.setCanceled(true);
				}
			}
		}
	}

}
