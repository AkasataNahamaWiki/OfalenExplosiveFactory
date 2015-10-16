package nahamawiki.oef.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.tileentity.ITileEntityEEMachine;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemOEFMaterial extends ItemOEFBase {

	protected IIcon[] iicon;
	private final int quantityType;

	public ItemOEFMaterial(int quantityType) {
		super();
		this.quantityType = quantityType;
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		switch (stack.getItemDamage()) {
		case 0:
			return this.onEEMaterUse(player, world, x, y, z);
		}
		return false;
	}

	private boolean onEEMaterUse(EntityPlayer player, World world, int x, int y, int z) {
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

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) {
		this.iicon = new IIcon[quantityType];
		for (int i = 0; i < quantityType; i++) {
			this.iicon[i] = register.registerIcon(this.getIconString() + "-" + i);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return iicon[meta];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < quantityType; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public int getMetadata(int meta) {
		return meta;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return this.getUnlocalizedName() + "." + itemStack.getItemDamage();
	}

}
