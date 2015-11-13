package nahamawiki.oef.block;

import nahamawiki.oef.core.OEFItemCore;
import nahamawiki.oef.entity.EntityRoboCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import takumicraft.Takumi.TakumiCraftCore;

public class BlockEERobo extends BlockOEFBase {

	public BlockEERobo() {
		super();
		this.setResistance(1000000F);
		this.setHardness(10F);
	}

	/** ブロックが右クリックされた時の処理。 */
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float posX, float posY, float posZ) {
		// このブロックの上が高性能爆弾でないか、プレイヤーが何も持っていないなら、何も起きない。
		if (world.getBlock(x, y + 1, z) != TakumiCraftCore.creeperblock || player.getHeldItem() == null)
			return false;
		int meta = player.getHeldItem().getItemDamage();
		// プレイヤーが持っているのがチップでないなら、何も起きない。
		if (player.getHeldItem().getItem() != OEFItemCore.materials || meta < 6 || 9 < meta)
			return false;
		// プレイヤーがコントロールチップを持っていたら、対応した色のロボを召還する。
		EntityRoboCreeper robo = new EntityRoboCreeper(world);
		robo.setType(meta - 6);
		robo.setPosition(x, y, z);
		player.swingItem();
		world.createExplosion(player, x, y, z, 0, false);
		world.setBlockToAir(x, y, z);
		world.setBlockToAir(x, y + 1, z);
		if (!world.isRemote)
			world.spawnEntityInWorld(robo);
		return true;
	}

}
