package nahamawiki.oef.util;

import java.util.ArrayList;
import java.util.Iterator;

public class OEFUtil {

	public static int getBaseCapacity(int level) {
		switch (level) {
		case 0:
			return 4000;
		case 1:
			return 8000;
		case 2:
			return 16000;
		case 3:
			return 32000;
		}
		return -1;
	}

	/** 引数のリストと要素が同じリストを返す。ただし、list != result。 */
	public static ArrayList<Integer> copyList(ArrayList<Integer> list) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			result.add((Integer) iterator.next());
		}
		return result;
	}

	/** 引数の配列と要素が同じ配列を返す。ただし、array != result。 */
	public static int[] copyArray(int[] array) {
		int[] result = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i];
		}
		return result;
	}

	/** 引数の配列を比較し、要素が同じならtrueを返す。 */
	public static boolean isArrayEqual(int[] a, int[] b) {
		if (a.length != b.length)
			return false;
		for (int i = 0; i < a.length; i++) {
			if (a[i] != b[i])
				return false;
		}
		return true;
	}

	/** 第一引数のリストのなかでの第二引数の配列のインデックスを返す。 */
	public static int listIndexOf(ArrayList<int[]> list, int[] array) {
		for (int i = 0; i < list.size(); i++) {
			if (OEFUtil.isArrayEqual(list.get(i), array)) {
				return i;
			}
		}
		return -1;
	}

}
