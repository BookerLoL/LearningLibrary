package ciphers;

/**
 * An implementation of the caesar cipher.
 * 
 * Non-letter characters are not removed or transformed.
 * 
 * @author Ethan
 *
 */
public class CaesarCipher {
	public static final int DEFAULT_SHIFTS = 1;
	public static final int MIN_SHIFTS = 0;
	public static final int MAX_SHIFTS = 26;

	private char[] alphabet;

	private int key;

	public CaesarCipher(int shifts) {
		alphabet = new char[26];
		for (char c = 'A'; c <= 'Z'; c++) {
			alphabet[c - 'A'] = c;
		}

		if (shifts < MIN_SHIFTS) {
			shifts = DEFAULT_SHIFTS;
		} else if (shifts > MAX_SHIFTS) {
			shifts = shifts % MAX_SHIFTS;
		}
		key = shifts;
	}

	protected char shiftForward(char letter) {
		return alphabet[(letter + key - 'A') % MAX_SHIFTS];
	}

	protected char shiftBackward(char letter) {
		return alphabet[(letter - key + 'A') % MAX_SHIFTS];
	}

	public String encode(String message) {
		StringBuilder sb = new StringBuilder(message.length());
		message = message.toUpperCase();

		for (int i = 0; i < message.length(); i++) {
			char ch = message.charAt(i);

			if (Character.isAlphabetic(ch)) {
				sb.append(shiftForward(ch));
			} else {
				sb.append(ch);
			}
		}

		return sb.toString();
	}

	public String decode(String encoding) {
		StringBuilder sb = new StringBuilder(encoding.length());
		encoding = encoding.toUpperCase();

		for (int i = 0; i < encoding.length(); i++) {
			char ch = encoding.charAt(i);

			if (Character.isAlphabetic(ch)) {
				sb.append(shiftBackward(ch));
			} else {
				sb.append(ch);
			}
		}

		return sb.toString();
	}

	public static void main(String[] args) {
		CaesarCipher c = new CaesarCipher(2);
		String encoding = c.encode("abcdefghijklmnopqrstuvwxyz");
		System.out.println(encoding);
		String message = c.decode(encoding);
		System.out.println(message);
	}
}
