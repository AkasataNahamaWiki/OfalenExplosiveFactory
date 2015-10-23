package nahamawiki.oef.util;

import java.util.ArrayList;
import java.util.Iterator;

public class EEUtil {

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

	public static ArrayList<Integer> copyList(ArrayList<Integer> list) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			result.add((Integer) iterator.next());
		}
		return result;
	}

}
