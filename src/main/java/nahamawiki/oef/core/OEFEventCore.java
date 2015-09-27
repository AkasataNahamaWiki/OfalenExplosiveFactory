package nahamawiki.oef.core;

import nahamawiki.oef.item.ItemEESword;
import nahamawiki.oef.item.armor.EEArmor;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class OEFEventCore {
	/**アイテム耐久値ゼロの時にフック**/
	@SubscribeEvent
	public void EEItemBroken(PlayerDestroyItemEvent e) {
		if (e.original.getItem() instanceof ItemEESword || e.original.getItem() instanceof EEArmor) {
			e.entity.worldObj.createExplosion(null, e.entity.posX, e.entity.posY, e.entity.posZ, 5, true);
			e.entity.setDead();
		}
	}

	/**爆発にフック**/
	@SubscribeEvent
	public void ExpEvent(Detonate e)
	{
		if(e.explosion.exploder != null)
		{
			Entity entity = e.explosion.exploder;
			if(entity instanceof EntityPlayer)
			{
				if(((EntityPlayer) entity).getHeldItem() != null && ((EntityPlayer) entity).getHeldItem().getItem() == OEFItemCore.EESword_GREEN)
				{
					for(Entity hit : e.getAffectedEntities())
					{
						if(hit instanceof EntityMob)
						{
							try
							{
								if (
										hit.getDataWatcher().getWatchableObjectByte(17) == 1)
								{

									hit.getDataWatcher().updateObject(17, (byte)0);
									hit.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) entity), 30);
									hit.worldObj.createExplosion(null,hit.posX, hit.posY, hit.posZ, 0, false);

								}
							}
							catch (Throwable throwable)
							{}
							finally
							{

							}
						}
					}
				}
			}
		}
	}

	/**プレイヤーの描画にフック**/
	private static final ResourceLocation texture_armor = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
	private final ModelBase armorModel = new ModelBiped();
	@SubscribeEvent
	public void EEArmorEvent(RenderPlayerEvent.SetArmorModel e)
	{
		if(e.stack.getItem() instanceof EEArmor)
		{
			GL11.glDepthMask(true);
			float f1 = e.entity.ticksExisted + e.partialRenderTick;
			f1 *= 1.5F;
			RenderManager manager = RenderManager.instance;
			manager.renderEngine.bindTexture(texture_armor);
			e.renderer.setRenderManager(manager);
            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glLoadIdentity();
            float f2 = f1 * 0.02F;
            float f3 = f1 * 0.02F;
            GL11.glTranslatef(f2, f3, 0.0F);
            e.renderer.setRenderPassModel(this.armorModel);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glEnable(GL11.GL_BLEND);
            float f4 = 0.5F;
            GL11.glColor4f(f4, f4, f4, 1.0F);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
		}
	}

		/**プレイヤー負傷時にフック**/
	@SubscribeEvent
    public void onHurt(LivingHurtEvent e)
    {
		boolean flg = false;
		if(e.entityLiving != null && e.entityLiving instanceof EntityPlayer)
    	{
    		for(int i = 0; i < 4; i++)
    		{
    			if(((EntityPlayer) e.entityLiving).getCurrentArmor(i) != null)
    			if(((EntityPlayer) e.entityLiving).getCurrentArmor(i).getItem() instanceof EEArmor)
    			{
    				flg = true;
    				break;
    			}
    		}


    		if(flg)
    		{
    			if(e.source != DamageSource.outOfWorld)
    			{
    				int i = e.entityLiving.getRNG().nextInt(4);
    				((EntityPlayer) e.entityLiving).getCurrentArmor(i).damageItem(1, e.entityLiving);
    				e.setCanceled(flg);
    			}
    		}
    	}
    }
}

