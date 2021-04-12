package ciphers;

/**
 * Implementation of the ROT13 Cipher.
 * 
 * Essentially a Caesar Cipher with a key of 13.
 * 
 * Non-letter characters are not removed or transformed.
 * 
 * @author Ethan
 * @version 1.0
 * @since 2021-04-07
 */
public class ROT13Cipher implements Cipherable {
	private CaesarCipher scrambler;

	public ROT13Cipher() {
		scrambler = new CaesarCipher(13);
	}

	public String encode(String message) {
		return scrambler.encode(message);
	}

	public String decode(String encoding) {
		return scrambler.decode(encoding);
	}
}
