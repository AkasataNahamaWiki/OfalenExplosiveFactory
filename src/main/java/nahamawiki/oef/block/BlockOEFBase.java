package nahamawiki.oef.block;

import nahamawiki.oef.OEFCore;
import net.minecraft.block.Block;

public abstract class BlockOEFBase extends Block
{
	public BlockOEFBase()
	{
		super(OEFCore.materialOEF);
		this.setCreativeTab(OEFCore.tabOEF);
	}
}