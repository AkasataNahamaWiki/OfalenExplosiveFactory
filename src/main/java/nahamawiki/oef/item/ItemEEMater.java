package nahamawiki.oef.item;

import nahamawiki.oef.tileentity.ITileEntityEEMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class ItemEEMater extends ItemOEFBase {

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof ITileEntityEEMachine) {
			if (world.isRemote) {
				return true;
			}
			String[] state = ((ITileEntityEEMachine) tileEntity).getState();
			if (state == null) {
				player.addChatMessage(new ChatComponentText("Error on reciving packet"));
				return true;
			}
			for (int i = 0; i < state.length; i++) {
				player.addChatMessage(new ChatComponentText(state[i]));
			}
			return true;
		}
		return false;
	}

}
