package nahamawiki.oef.block;

import nahamawiki.oef.core.OEFItemCore;
import nahamawiki.oef.entity.EntityRoboCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import takumicraft.Takumi.TakumiCraftCore;

public class BlockEERobo extends BlockOEFBase
{
	public BlockEERobo()
	{
		super();
		this.setResistance(1000000F);
		this.setHardness(10f);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float posX, float posY, float posZ) {
		if(world.getBlock(x, y + 1, z) == TakumiCraftCore.creeperblock && player.getHeldItem() != null && player.getHeldItem().getItem() == OEFItemCore.materials)
		{
			if(player.getHeldItem().getItemDamage() == 6)
			{
				EntityRoboCreeper robo = new EntityRoboCreeper(world);
				robo.setType(0);
				robo.setPosition(x, y, z);
				player.swingItem();
				world.createExplosion(player, x, y, z, 0, false);
				world.setBlockToAir(x, y, z);
				world.setBlockToAir(x, y + 1, z);
				if(!world.isRemote)world.spawnEntityInWorld(robo);
				return true;
			}
			
			else if(player.getHeldItem().getItemDamage() == 7)
			{
				EntityRoboCreeper robo = new EntityRoboCreeper(world);
				robo.setType(1);
				robo.setPosition(x, y, z);
				player.swingItem();
				world.createExplosion(player, x, y, z, 0, false);
				world.setBlockToAir(x, y, z);
				world.setBlockToAir(x, y + 1, z);
				if(!world.isRemote)world.spawnEntityInWorld(robo);
				return true;
			}
			
			else if(player.getHeldItem().getItemDamage() == 8)
			{
				EntityRoboCreeper robo = new EntityRoboCreeper(world);
				robo.setType(2);
				robo.setPosition(x, y, z);
				player.swingItem();
				world.createExplosion(player, x, y, z, 0, false);
				world.setBlockToAir(x, y, z);
				world.setBlockToAir(x, y + 1, z);
				if(!world.isRemote)world.spawnEntityInWorld(robo);
				return true;
			}
			
			else if(player.getHeldItem().getItemDamage() == 9)
			{
				EntityRoboCreeper robo = new EntityRoboCreeper(world);
				robo.setType(3);
				robo.setPosition(x, y, z);
				player.swingItem();
				world.createExplosion(player, x, y, z, 0, false);
				world.setBlockToAir(x, y, z);
				world.setBlockToAir(x, y + 1, z);
				if(!world.isRemote)world.spawnEntityInWorld(robo);
				return true;
			}
		}
		return true;
	}
}