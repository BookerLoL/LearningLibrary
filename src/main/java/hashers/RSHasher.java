package hashers;

/**
 * Rober Sedgewicks Hash Function
 * 
 * @author Ethan
 *
 */
public class RSHasher {
	public static long hash64(String input) {
		long b = 378551;
		long a = 63689;
		long hash = 0;

		for (int i = 0; i < input.length(); i++) {
			hash *= a + input.charAt(i);
			a *= b;
		}

		return hash;
	}
}
