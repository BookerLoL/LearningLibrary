package hashers;

/**
 * An implementation of Knuth's variant on the Cormen's division hash method.
 * 
 * @author Ethan
 *
 */
public class KDivisionHasher {
	public static long hash64(long value, long primeNumber) {
		return (value * (value + 3)) % primeNumber;
	}

	public static int hash32(int value, int primeNumber) {
		return (value * (value + 3)) % primeNumber;
	}
}
