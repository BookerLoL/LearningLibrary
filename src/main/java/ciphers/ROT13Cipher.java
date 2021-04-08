package ciphers;

/**
 * Implementation of the ROT13 Cipher.
 * 
 * Essentially a Caesar Cipher with a key of 13.
 * 
 * Non-letter characters are not removed or transformed.
 * 
 * @author Ethan
 *
 */
public class ROT13Cipher {
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
