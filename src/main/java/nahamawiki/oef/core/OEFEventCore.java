package nahamawiki.oef.core;

import nahamawiki.oef.item.ItemEESword;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class OEFEventCore
{
	@SubscribeEvent
	public void EESwordBroken(PlayerDestroyItemEvent  e)
	{
		if(e.original.getItem() instanceof ItemEESword)
		{
			e.entity.worldObj.createExplosion(null, e.entity.posX, e.entity.posY, e.entity.posZ, 5, true);
		}
	}
}