package hashers;

/**
 * An implementation of a CRC variant hash function.
 * 
 * @author Ethan
 *
 */
public class CRCHasher {
	public int hash32(String input) {
		int hash = 0;
		for (int i = 0; i < input.length(); i++) {
			int high = hash & 0xF8000000;

			hash <<= 5;
			hash ^= (high >> 27);
			hash ^= input.charAt(i);
		}

		return hash;
	}
}
