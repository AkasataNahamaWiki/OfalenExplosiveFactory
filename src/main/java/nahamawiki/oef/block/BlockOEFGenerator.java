package nahamawiki.oef.block;

public class BlockOEFGenerator extends BlockOEFBase {

	public BlockOEFGenerator() {
		super();
		this.setHardness(7.5F);
		this.setResistance(1000000F);
	}

	@Override
	public boolean canProvideEE() {
		return false;
	}
}