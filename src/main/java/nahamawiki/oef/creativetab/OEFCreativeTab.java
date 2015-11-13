package nahamawiki.oef.creativetab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.core.OEFItemCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class OEFCreativeTab extends CreativeTabs {

	@SideOnly(Side.CLIENT)
	private ItemStack icon;

	public OEFCreativeTab(String label) {
		super(label);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getIconItemStack() {
		if (icon == null) {
			icon = new ItemStack(OEFItemCore.EEBattery, 1, 3);
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger("holdingEE", 1);
			icon.setTagCompound(nbt);
		}
		return icon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return null;
	}

}
