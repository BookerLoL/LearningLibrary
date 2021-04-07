package hashers;

/**
 * An implementation of the SDBM hash function
 * 
 * @author Ethan
 *
 */
public class SDBMHasher {
	public static long hash64(String input) {
		long hash = 0;
		for (int i = 0; i < input.length(); i++) {
			hash = input.charAt(i) + (hash << 6) + (hash << 16) - hash;
		}
		return hash;
	}
}
