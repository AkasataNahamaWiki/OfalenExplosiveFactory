package nahamawiki.oef.creativetab;

import nahamawiki.oef.core.OEFItemCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class OEFCreativeTab extends CreativeTabs {

	public OEFCreativeTab(String label) {
		super(label);
	}

	@Override
	public Item getTabIconItem() {
		return OEFItemCore.EESword;
	}

}
