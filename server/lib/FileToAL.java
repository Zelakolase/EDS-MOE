package lib;

import java.util.ArrayList;
import java.util.Collections;

public class FileToAL {
	public static ArrayList<String> convert(String filename) {
		ArrayList<String> data = new ArrayList<>();
		String[] raw = new String(IO.read(filename)).split(System.lineSeparator());
		Collections.addAll(data, raw);
		return data;
	}
}
