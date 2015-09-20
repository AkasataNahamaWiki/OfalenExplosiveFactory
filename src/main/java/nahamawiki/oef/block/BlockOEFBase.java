package nahamawiki.oef.block;

import nahamawiki.oef.OEFCore;
import net.minecraft.block.Block;

public abstract class BlockOEFBase extends Block {

	public BlockOEFBase() {
		super(OEFCore.materialOEF);
		this.setCreativeTab(OEFCore.tabOEF);
	}

	public boolean canProvideEE() {
		return false;
	}

	public boolean canReciveEE() {
		return false;
	}

	public int providingEE(int meta) {
		return 0;
	}

}
