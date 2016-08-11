package ua.kiral.project4.model.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class provides hash utils, like MD5 String encoding and etc.
 *
 */
public class HashGenerator {

	/**
	 * Returns random a-z char sequence, with specified lenght.
	 * 
	 * @return
	 */
	public static String generateKey(int lenght) {
		StringBuilder builder = new StringBuilder();

		while (builder.length() < lenght) {
			builder.append(getRandomChar());
		}

		return builder.toString();
	}

	/**
	 * Encodes string as hash value with specified algoritm. Based on
	 * java.security.MessageDigest class. Supported encoding algoritms: MD5,
	 * SHA-1, SHA-256.
	 * 
	 * @param key
	 * @param algorithm
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String generateHash(String key, String algorithm) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

		messageDigest.reset();
		messageDigest.update(key.getBytes());

		String md5Hex = new BigInteger(1, messageDigest.digest()).toString(16);

		while (md5Hex.length() < 32) {
			md5Hex = "0" + md5Hex;
		}

		return md5Hex;
	}

	/**
	 * Returns random char which match to diapason a-z.
	 * 
	 * @return char value
	 */
	private static char getRandomChar() {
		return (char) ((int) (Math.random() * 25 + 97));
	}
}