package nahamawiki.oef.core;

import nahamawiki.oef.item.ItemEESword;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class OEFEventCore {
	@SubscribeEvent
	public void EESwordBroken(PlayerDestroyItemEvent e) {
		if (e.original.getItem() instanceof ItemEESword) {
			e.entity.worldObj.createExplosion(null, e.entity.posX, e.entity.posY, e.entity.posZ, 5, true);
		}
	}

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
}

