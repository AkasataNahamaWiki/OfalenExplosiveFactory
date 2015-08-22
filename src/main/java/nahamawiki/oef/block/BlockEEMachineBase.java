package nahamawiki.oef.block;

import nahamawiki.oef.OEFCore;
import net.minecraft.block.Block;

public abstract class BlockEEMachineBase extends Block {

	public BlockEEMachineBase() {
		super(OEFCore.materialOEF);
		this.setCreativeTab(OEFCore.tabOEF);
		this.setHarvestLevel("pickaxe", 3);
	}

	public abstract boolean canProvideEE();

	public abstract boolean canReciveEE();

}
