package nahamawiki.oef.item;

import nahamawiki.oef.tileentity.ITileEntityEEMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class ItemEEMater extends ItemOEFBase {

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof ITileEntityEEMachine) {
			String[] state = ((ITileEntityEEMachine) tileEntity).getState(side);
			for (int i = 0; i < state.length; i++) {
				player.addChatMessage(new ChatComponentText(state[i]));
			}
			return true;
		}
		return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
	}
}
