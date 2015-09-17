package nahamawiki.oef.tileentity;

public interface ITileEntityEEMachine {

	/**
	 * その面が送受信可能かを返す。
	 *
	 * @return 0:送受信不可, 1:送信可能, 2:受信可能, 3:送受信可能
	 */
	public int getMachineType(int side);

	/**
	 * EEを受け取る処理
	 *
	 * @param amount
	 *            受け取るEE
	 * @param side
	 *            受け取る面
	 * @return あまり
	 */
	public int reciveEE(int amount, int side);

}
