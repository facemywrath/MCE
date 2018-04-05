package facejup.mce.util;

import java.util.Random;

public class Numbers {

	public static int getRandom(int lower, int upper) {
		Random random = new Random();
		return random.nextInt((upper - lower) + 1) + lower;
	}

}
