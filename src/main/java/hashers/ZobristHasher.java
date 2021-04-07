package hashers;

import java.util.Map;

/**
 * An implementation of zobrist hashing.
 * 
 * Used for games maintaining evaluated previous states of a game.
 * 
 * @author Ethan
 *
 */
public class ZobristHasher {
	public static long hash64(String[][] board, Map<String, Long> pieceBitstrings, String emptySymbol) {
		long hash = 0;
		for (String[] row : board) {
			for (String symbol : row) {
				if (!symbol.equals(emptySymbol)) {
					hash ^= pieceBitstrings.get(symbol);
				}
			}
		}
		return hash;
	}
}
