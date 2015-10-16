package nahamawiki.oef.block;

import nahamawiki.oef.core.OEFItemCore;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class BlockEESurveyor extends BlockOEFBase {

	public BlockEESurveyor() {
		super();
		this.setHardness(0F);
		this.setResistance(6000000.0F);
		this.setStepSound(soundTypeMetal);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (player.getHeldItem() != null && player.getHeldItem().getItem() == OEFItemCore.EEMater) {
			if (world.isRemote) {
				return true;
			}
			String[] state = new String[] {
					StatCollector.translateToLocal("info.EEMachineState.name") + StatCollector.translateToLocal(this.getLocalizedName()),
					StatCollector.translateToLocal("info.EEMachineState.meta") + world.getBlockMetadata(x, y, z),
			};
			for (int i = 0; i < state.length; i++) {
				player.addChatMessage(new ChatComponentText(state[i]));
			}
			return true;
		}
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		int i = MathHelper.floor_double(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		int j = 0;
		switch (i) {
		case 0:
			j = 2;
			break;
		case 1:
			j = 5;
			break;
		case 2:
			j = 3;
			break;
		case 3:
			j = 4;
		}
		world.setBlockMetadataWithNotify(x, y, z, j, 2);
	}

}
