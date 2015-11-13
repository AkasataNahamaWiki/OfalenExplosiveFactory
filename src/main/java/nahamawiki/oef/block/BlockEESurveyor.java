package nahamawiki.oef.block;

import nahamawiki.oef.core.OEFItemCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class BlockEESurveyor extends BlockOEFBase {

	private IIcon iicon;

	public BlockEESurveyor() {
		super();
		this.setHardness(0F);
		this.setResistance(6000000.0F);
		this.setStepSound(soundTypeMetal);
	}

	/** ブロックが右クリックされた時の処理。 */
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		// EEメーターで右クリックされていたら情報をチャットに出力する。
		if (player.getHeldItem() != null && player.getHeldItem().getItem() == OEFItemCore.materials && player.getHeldItem().getItemDamage() == 0) {
			if (world.isRemote)
				return true;
			String[] state = new String[] {
					StatCollector.translateToLocal("info.EEMachineState.name") + StatCollector.translateToLocal(this.getLocalizedName()),
					StatCollector.translateToLocal("info.EEMachineState.direction") + StatCollector.translateToLocal("info.side-" + world.getBlockMetadata(x, y, z)),
			};
			for (int i = 0; i < state.length; i++) {
				player.addChatMessage(new ChatComponentText(state[i]));
			}
			return true;
		}
		return false;
	}

	/** ブロックが設置された時の処理。 */
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		// 設置したEntityの向きによってメタデータを設定する。
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

	@Override
	public void registerBlockIcons(IIconRegister register) {
		super.registerBlockIcons(register);
		iicon = register.registerIcon(this.getTextureName() + "-0");
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		if (side != 0 && side == meta)
			return iicon;
		return super.getIcon(side, meta);
	}

}
