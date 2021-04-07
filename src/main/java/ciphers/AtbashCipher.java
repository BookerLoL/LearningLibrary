package ciphers;

/**
 * Atbash Cipher implementation
 * 
 * References:
 * https://www.youtube.com/watch?v=YJMuUIIfMzg&ab_channel=SecretScreening
 * https://en.wikipedia.org/wiki/Atbash
 * 
 * @author Ethan
 *
 */
public class AtbashCipher {
	public String encode(String message) {
		return translate(message);
	}

	public String decode(String encoding) {
		return translate(encoding);
	}

	private String translate(String input) {
		input = input.toUpperCase();
		StringBuilder sb = new StringBuilder(input.length());
		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);

			if (Character.isLetter(ch)) {
				sb.append((char) ('Z' - (ch - 'A')));
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
