package hashers;

/**
 * Brian Kernigham and Dennis Ritchie's hash function
 * 
 * @author Ethan
 *
 */
public class BKDRHasher {
	public static int hash32(String input) {
		return hash32(input, 131);
	}

	// seed: 31, 131, 1313, 13131, etc...
	public static int hash32(String input, int seed) {
		if (!isValidSeed(seed)) {
			throw new IllegalArgumentException("Number must be an alternating pattern of '1' and '3'");
		}

		int hash = 0;
		for (int i = 0; i < input.length(); i++) {
			hash = (hash * seed) + input.charAt(i);
		}

		return (hash & 0x7FFFFFFF);
	}

	public static boolean isValidSeed(int seed) {
		String numSeed = String.valueOf(seed);

		if (!isValidNumber(numSeed.charAt(0))) {
			return false;
		}

		boolean prevIsOne = numSeed.charAt(0) == '1';
		for (int i = 1; i < numSeed.length(); i++) {
			char num = numSeed.charAt(i);

			if (!isValidNumber(num)) {
				return false;
			}

			if (prevIsOne) {
				if (num == '1') {
					return false;
				}
			} else {
				if (num == '3') {
					return false;
				}
			}
			prevIsOne = !prevIsOne;

		}
		return true;
	}

	private static boolean isValidNumber(char ch) {
		return ch == '1' || ch == '3';
	}
}
