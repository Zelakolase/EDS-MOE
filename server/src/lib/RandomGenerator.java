package lib;

import java.util.Random;
/**
 * Generates random strings
 * @author Morad A.
 */
public class RandomGenerator {
	/**
	 * From StackOverflow, modified
	 */
	public static String getSaltString(int num, int mode) {
		String SALTCHARS = "";
		if (mode == 0) SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890qwertyuiopasdfghjklzxcvbnm";
		if (mode == 1) SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		if (mode == 2) SALTCHARS = "1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < num) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		return salt.toString();

	}

}
