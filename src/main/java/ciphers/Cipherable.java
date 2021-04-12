package ciphers;

public interface Cipherable {
	String encode(String message);

	String decode(String encoding);
}
