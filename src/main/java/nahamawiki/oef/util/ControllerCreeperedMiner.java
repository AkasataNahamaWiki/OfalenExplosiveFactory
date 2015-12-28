package nahamawiki.oef.util;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.world.World;

public class ControllerCreeperedMiner {

	private static HashMap<Integer, ControllerCreeperedMiner> instanceList = new HashMap<Integer, ControllerCreeperedMiner>();
	private int id;
	private ArrayList<int[]> areaList = new ArrayList<int[]>();

	private ControllerCreeperedMiner(int id) {
		this.id = id;
	}

	public static void init() {
		instanceList.clear();
	}

	/** ディメンションに対応したコントローラのインスタンスを返す。 */
	public static ControllerCreeperedMiner getInstance(World world) {
		// ディメンションIDを取得し、そのディメンションに対応するコントローラがないなら生成して返す。
		int id = world.provider.dimensionId;
		if (!instanceList.containsKey(id)) {
			instanceList.put(id, new ControllerCreeperedMiner(id));
		}
		return instanceList.get(id);
	}

	/** 引数の座標が匠化採掘機の採掘範囲内かどうか。 */
	public boolean isInArea(int x, int y, int z) {
		// 採掘範囲リストに登録されていたらtrueを返す。
		if (areaList.isEmpty())
			return false;
		return OEFUtil.listIndexOf(areaList, new int[] { x, y, z }) > -1;
	}

	/** 匠化採掘機の採掘範囲を登録する。 */
	public void registerArea(int[] coord, int[] miningRange) {
		this.renewArea(coord, miningRange, true);
	}

	/** 匠化採掘機の採掘範囲を解除する。 */
	public void removeArea(int[] coord, int[] miningRange) {
		this.renewArea(coord, miningRange, false);
	}

	/** coordとminingRangeからなる採掘範囲内の座標をリストに登録/削除する。 */
	private void renewArea(int[] coord, int[] miningRange, boolean flag) {
		// 採掘機のgetNextBlockとほとんど同じ。
		int[] point = new int[3];
		point[0] = miningRange[0] < 0 ? coord[0] : coord[0] + miningRange[0];
		point[1] = coord[1] + 1;
		point[2] = miningRange[1] < 0 ? coord[2] - 1 : coord[2] + miningRange[1] - 1;
		while (true) {
			point[0]--;
			if (!this.isInRange(point, coord, miningRange)) {
				point[0] = miningRange[0] < 0 ? coord[0] - 1 : coord[0] + miningRange[0] - 1;
				point[2]--;
				if (!this.isInRange(point, coord, miningRange)) {
					point[2] = miningRange[1] < 0 ? coord[2] - 1 : coord[2] + miningRange[1] - 1;
					point[1]--;
					if (!this.isInRange(point, coord, miningRange)) {
						break;
					}
				}
			}
			if (flag) {
				areaList.add(OEFUtil.copyArray(point));
			} else {
				areaList.remove(OEFUtil.listIndexOf(areaList, point));
			}
		}
	}

	/** pointの座標がcoordとminingRangeからなる採掘範囲に含まれているならtrueを返す。 */
	private boolean isInRange(int[] point, int[] coord, int[] miningRange) {
		// 採掘機のisInAreaを少し改変したもの。
		if ((coord[0] < point[0] && point[0] < coord[0] + miningRange[0]) || (coord[0] + miningRange[0] < point[0] && point[0] < coord[0])) {
			if ((coord[2] < point[2] && point[2] < coord[2] + miningRange[1]) || (coord[2] + miningRange[1] < point[2] && point[2] < coord[2])) {
				if (0 < point[1] && point[1] <= coord[1] + 1) {
					return true;
				}
			}
		}
		return false;
	}

}
