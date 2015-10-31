package nahamawiki.oef.block;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.OEFCore;
import nahamawiki.oef.core.OEFItemCore;
import nahamawiki.oef.tileentity.TileEntityEECapacitor;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockEECapacitor extends BlockEEMachineBase {

	protected IIcon[] iicon = new IIcon[4];

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityEECapacitor();
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityEECapacitor)
			((TileEntityEECapacitor) tileEntity).updateDirection(world, x, y, z);
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityEECapacitor)
			((TileEntityEECapacitor) tileEntity).updateDirection(world, x, y, z);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (player.getHeldItem() != null && player.getHeldItem().isItemEqual(OEFItemCore.EEPliers)) {
			if (world.isRemote)
				return true;
			TileEntityEECapacitor capacitor = (TileEntityEECapacitor) world.getTileEntity(x, y, z);
			int type = capacitor.getSideTypes()[side] + 1;
			if (type > 2)
				type = 0;
			capacitor.setSideType(side, type);
			player.addChatMessage(new ChatComponentText("Set type : " + type));
			return true;
		}
		player.openGui(OEFCore.instance, 1, world, x, y, z);
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		for (int i = 0; i < 4; i++) {
			this.iicon[i] = register.registerIcon(this.getTextureName() + "-" + i);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return iicon[meta & 3];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < 4; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public int damageDropped(int meta) {
		return meta & 3;
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

}
