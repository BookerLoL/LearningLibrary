package ciphers;

/**
 * Atbash Cipher implementation
 * 
 * Non-letter characters are not removed or transformed.
 * 
 * References:
 * https://www.youtube.com/watch?v=YJMuUIIfMzg&ab_channel=SecretScreening
 * https://en.wikipedia.org/wiki/Atbash
 * 
 * @author Ethan
 * @version 1.0
 * @since 2021-04-06
 */
public class AtbashCipher implements Cipherable {
	public String encode(String message) {
		return translate(message);
	}

	public String decode(String encoding) {
		return translate(encoding);
	}

	protected char shift(char c) {
		return (char) ('Z' - (c - 'A'));
	}

	private String translate(String input) {
		input = input.toUpperCase();
		StringBuilder sb = new StringBuilder(input.length());
		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);

			if (Character.isLetter(ch)) {
				sb.append(shift(ch));
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		AtbashCipher a = new AtbashCipher();
		String encoding = a.encode("abcdefghijklmnopqrstuvwxyz");
		System.out.println(encoding);
		String message = a.decode(encoding);
		System.out.println(message);
	}
}
