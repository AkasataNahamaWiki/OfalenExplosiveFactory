package nahamawiki.oef.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class OEFCreativeTab extends CreativeTabs {

	public OEFCreativeTab(String label) {
		super(label);
	}

	@Override
	public Item getTabIconItem() {
		return Items.apple;
	}

}
