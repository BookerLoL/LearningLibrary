package hashers;

/**
 * An implementation of the DJB Hash funciton
 * 
 * @author Ethan
 *
 */
public class DJBHasher {
	public static long hash64(String input) {
		long hash = 5381;
		for (int i = 0; i < input.length(); i++) {
			hash = ((hash << 5) + hash) + input.charAt(i);
		}
		return hash;
	}
}
