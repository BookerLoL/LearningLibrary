package hashers;

/**
 * Peter J. Weinberger's hash function.
 * 
 * References: https://en.wikipedia.org/wiki/PJW_hash_function
 * 
 * @author Ethan
 *
 */
public class PJWHasher {
	public static int hash32(String input) {
		int hash = 0;
		int high = 0;
		for (int i = 0; i < input.length(); i++) {
			hash = (hash << 4) + input.charAt(i);
			high = hash & 0xF0000000;
			if (high != 0) {
				hash ^= high >> 24;
			}
			hash &= ~high;
		}
		return hash;
	}
}
