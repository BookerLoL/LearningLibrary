package hashers;

/**
 * An implementation of the BUZ hash function.
 * 
 * @author Ethan
 *
 */
public class BUZHasher {

	/**
	 * Creates a 32 bit hash value from the given input and precomputed random
	 * numbers.
	 * 
	 * @param input
	 * @param randomNumbers Precomputed random numbers for each char value.
	 * @return
	 */
	public static int hash32(String input, int[] randomNumbers) {
		int hash = 0;

		for (int i = 0; i < input.length(); i++) {
			int high = hash & 0x80000000;
			hash <<= 1;
			hash ^= (high >> 31);
			hash ^= randomNumbers[input.charAt(i)];
		}

		return hash;
	}
}
