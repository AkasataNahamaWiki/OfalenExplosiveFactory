package nahamawiki.oef.block;


public class BlockEEGenerator extends BlockEEMachineBase {

	public BlockEEGenerator() {
		super();
		this.setHardness(7.5F);
		this.setResistance(0.0F);
		this.setStepSound(soundTypeMetal);
	}

	@Override
	public boolean canProvideEE() {
		return true;
	}

	@Override
	public boolean canReciveEE() {
		return false;
	}

}
