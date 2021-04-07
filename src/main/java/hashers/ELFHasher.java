package hashers;

/**
 * An implementation of the ELF hash function.
 * 
 * @author Ethan
 *
 */
public class ELFHasher {
	public static int hash32(String input) {
		int hash = 0;
		int x = 0;
		for (int i = 0; i < input.length(); i++) {
			hash = (hash << 4) + input.charAt(0);

			x = hash & 0xF0000000;
			if (x != 0) {
				hash ^= (x >> 24);
			}
			hash &= ~x;
		}

		return hash;
	}
}
