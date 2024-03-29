package lib;

import java.util.ArrayList;
import java.util.Collections;

public class FileToAL {
	/**
	 * Every line is an element in a file
	 * @param filename Filename
	 * @return ArrayList of every line as an element
	 */
	public static ArrayList<String> convert(String filename) {
		ArrayList<String> data = new ArrayList<>();

		try {
		String[] raw = new String(IO.read(filename)).split("\n");
		Collections.addAll(data, raw);
		} catch(Exception e) {
			// Shhh.....
		}
		return data;
	}
}
