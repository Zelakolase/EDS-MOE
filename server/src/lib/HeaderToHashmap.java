package lib;

import java.util.HashMap;
/**
 * The name says it all
 * @author morad
 */
public class HeaderToHashmap {
	/**
	 * Reads the request, then makes a HashMap with headers
	 * @param req Original request
	 * @return HashMap. Key: Header name, Value: Header Value
	 */
	public static HashMap<String, String> convert(String req) {
		HashMap<String, String> data = new HashMap<>();
		String[] lines = req.split("\r\n");
		String[] fir_data = lines[0].split(" ");
		data.put("method", fir_data[0]);
		data.put("path", PathFilter.filter(fir_data[1]));
		for (int i = 1; i < lines.length; i++) {
			String[] temp = lines[i].split(": ");
			data.put(temp[0], temp[1]);
		}
		return data;
	}
}
