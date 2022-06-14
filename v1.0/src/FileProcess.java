import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import lib.HTTPCode;
import lib.IO;

public class FileProcess {
	/**
	 * Serving Static file process
	 */
	public static HashMap<String, byte[]> redirector(String in, ArrayList<String> files) throws Exception{
		HashMap<String, byte[]> res = new HashMap<>();
		if (files.contains(in)) {
			if (new File("./www/" + in).exists()) {
				res.put("body", IO.read("./www/" + in));
				res.put("code", HTTPCode.OK.getBytes());
			} else {
				res.put("body", "Not found".getBytes());
				res.put("code", HTTPCode.NOT_FOUND.getBytes());
			}
		} else {
			res.put("body", "Not permitted".getBytes());
			res.put("code", HTTPCode.FORBIDDEN.getBytes());
		}
		return res;
	}
}
