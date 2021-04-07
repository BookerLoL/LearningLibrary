package hashers;

/**
 * Justin Sobel hash function
 * 
 * @author Ethan
 *
 */
public class JSHasher {
	public static long hash64(String input) {
		long hash = 1315423911;
		for (int i = 0; i < input.length(); i++) {
			hash ^= ((hash << 5) + input.charAt(i) + (hash >> 2));
		}
		return hash;
	}
}
