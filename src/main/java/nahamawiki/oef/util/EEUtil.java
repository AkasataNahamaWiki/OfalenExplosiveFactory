package nahamawiki.oef.util;

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

}
