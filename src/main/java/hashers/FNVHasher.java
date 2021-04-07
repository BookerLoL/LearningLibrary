package hashers;

/**
 * The FNV hash implementation
 * 
 * @author Ethan
 *
 */
public class FNVHasher {
	public static long hash64(String input) {
		long fnvPrime = 0x811C9DC5L;
		long hash = 0;

		for (int i = 0; i < input.length(); i++) {
			hash *= fnvPrime;
			hash ^= input.charAt(i);
		}
		return hash;
	}
}
