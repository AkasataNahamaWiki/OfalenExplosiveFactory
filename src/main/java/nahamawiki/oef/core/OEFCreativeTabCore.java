package nahamawiki.oef.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class OEFCreativeTabCore extends CreativeTabs
{
	public OEFCreativeTabCore(String label) {
		super(label);
	}

	/**アイコンの設定*/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return new ItemStack(Items.apple,1,0).getItem();
	}
	
 
	@Override
	@SideOnly(Side.CLIENT)
	public String getTranslatedTabLabel()
	{
		return "OEF";
	}


}