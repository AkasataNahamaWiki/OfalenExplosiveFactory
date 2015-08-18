package nahamawiki.oef.block;

import nahamawiki.oef.OEFCore;
import net.minecraft.block.Block;

public abstract class BlockEEMachineBase extends Block {

	public BlockEEMachineBase() {
		super(OEFCore.materialOEF);
		this.setCreativeTab(OEFCore.tabOEF);
	}

	public abstract boolean canProvideEE();
}