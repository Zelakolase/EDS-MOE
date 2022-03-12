import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import lib.HTTPCode;
import lib.IO;

public class FileProcess {
	/**
	 * Serving Static file process
	 */
	static ArrayList<String> files = new ArrayList<>() {{
		add("index.html");
	}};
	public static HashMap<String, byte[]> redirector(String in) {
		HashMap<String, byte[]> res = new HashMap<String, byte[]>();
		if(files.contains(in)) {
			if(new File("./www/"+in).exists()) {
				res.put("body", IO.read("./www/"+in));
				res.put("code", HTTPCode.OK.getBytes());
			}else {
				res.put("body", "Not found".getBytes());
				res.put("code", HTTPCode.NOT_FOUND.getBytes());
			}
		}else {
			res.put("body", "Not permitted".getBytes());
			res.put("code", HTTPCode.FORBIDDEN.getBytes());
		}
		return res;
	}
}
