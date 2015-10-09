package nahamawiki.oef.block;

import net.minecraft.block.ITileEntityProvider;

public abstract class BlockEEMachineBase extends BlockOEFBase implements ITileEntityProvider {

	public BlockEEMachineBase() {
		super();
		this.setHardness(5.0F);
		this.setResistance(6000000.0F);
		this.setHarvestLevel("pickaxe", 3);
		this.setStepSound(soundTypeMetal);
	}

}
