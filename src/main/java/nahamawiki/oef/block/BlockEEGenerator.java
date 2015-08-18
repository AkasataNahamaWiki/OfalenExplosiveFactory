package nahamawiki.oef.block;

public class BlockEEGenerator extends BlockEEMachineBase {

	public BlockEEGenerator() {
		super();
		this.setHardness(7.5F);
		this.setResistance(1000000F);
	}

	@Override
	public boolean canProvideEE() {
		return true;
	}
}