package nahamawiki.oef.block;

public abstract class BlockEEMachineBase extends BlockOEFBase {

	public BlockEEMachineBase() {
		super();
		this.setHardness(5.0F);
		this.setResistance(6000000.0F);
		this.setHarvestLevel("pickaxe", 3);
	}

	public abstract boolean canProvideEE();

	public abstract boolean canReciveEE();

	public abstract int providingEE(int meta);

}
