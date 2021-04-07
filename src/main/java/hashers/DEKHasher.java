package hashers;

/**
 * An implementation of Donald E. Knuth's hash function.
 * 
 * @author Ethan
 *
 */
public class DEKHasher {
	public static long hash64(String input) {
		long hash = input.length();

		for (int i = 0; i < input.length(); i++) {
			hash = ((hash << 5) ^ (hash >> 27)) ^ input.charAt(i);
		}

		return hash;
	}
}
