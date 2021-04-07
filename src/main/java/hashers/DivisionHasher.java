package hashers;

/**
 * An implementation of Cormen's basic division hash method.
 * 
 * @author Ethan
 *
 */
public class DivisionHasher {
	public static long hash64(long value, long primeNumber) {
		return value % primeNumber;
	}

	public static int hash32(int value, int primeNumber) {
		return value % primeNumber;
	}
}
