package nahamawiki.oef.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

public class ItemEEBattery extends ItemOEFBase {

	private IIcon[][] iicon = new IIcon[2][4];

	public ItemEEBattery() {
		super();
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) {
		for (int i = 0; i < 4; i++) {
			this.iicon[0][i] = register.registerIcon(this.getIconString() + "-" + i);
			this.iicon[1][i] = register.registerIcon(this.getIconString() + "_Full-" + i);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconIndex(ItemStack itemStack) {
		if (itemStack.hasTagCompound()) {
			NBTTagCompound nbt = itemStack.getTagCompound();
			if (nbt.getInteger("holdingEE") == 0)
				return this.iicon[0][itemStack.getItemDamage()];
			return this.iicon[1][itemStack.getItemDamage()];
		}
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("holdingEE", 0);
		itemStack.setTagCompound(nbt);
		return this.iicon[0][itemStack.getItemDamage()];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
		ItemStack itemStack;
		NBTTagCompound nbt = new NBTTagCompound();

		for (int i = 0; i < 4; i++) {
			itemStack = new ItemStack(item, 1, i);
			nbt.setInteger("holdingEE", 0);
			itemStack.setTagCompound(nbt);
			list.add(itemStack);

			nbt.setInteger("holdingEE", getCapacity(i));
			itemStack.setTagCompound(nbt);
			list.add(itemStack);
		}
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
		if (itemStack.hasTagCompound())
			list.add(StatCollector.translateToLocal("info.EEMachineState.holding")
					+ itemStack.getTagCompound().getInteger("holdingEE"));
	}

	@Override
	public int getMetadata(int meta) {
		return meta;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return this.getUnlocalizedName() + "." + itemStack.getItemDamage();
	}

	private int getCapacity(int level) {
		switch (level) {
		case 0:
			return 4000;
		case 1:
			return 8000;
		case 2:
			return 16000;
		case 3:
			return 32000;
		}
		return 0;
	}

}
