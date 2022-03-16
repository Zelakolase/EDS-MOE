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
		add("about.html");
		add("support.html");
		add("tools.html");
		add("_next/static/css/2ef95f13bec2f900.css");
		add("_next/static/chunks/34-bdf13a4717a36307.js");
		add("_next/static/chunks/651.243d23442247d286.js");
		add("_next/static/chunks/78e521c3-a0e872a2df8ff9e8.js");
		add("_next/static/chunks/863-a68842678ea25cb0.js");
		add("_next/static/chunks/framework-5f4595e5518b5600.js");
		add("_next/static/chunks/main-e267bb9839e5051c.js");
		add("_next/static/chunks/polyfills-5cd94c89d3acac5f.js");
		add("_next/static/chunks/webpack-c35fdbd9f927d367.js");
		add("_next/static/chunks/pages/_app-ce99db96f4671041.js");
		add("_next/static/chunks/pages/_error-2f883067a14f4c4a.js");
		add("_next/static/chunks/pages/about-32f4107dc1e7a18d.js");
		add("_next/static/chunks/pages/index-f3f292817416b1b3.js");
		add("_next/static/chunks/pages/support-f695cd8f0843fdc3.js");
		add("_next/static/chunks/pages/tools-ef4fd2538ba0ef4a.js");
		add("_next/static/2CXVybD6p7sPSHHHDiJuN/_buildManifest.js");
		add("_next/static/2CXVybD6p7sPSHHHDiJuN/_middlewareManifest.js");
		add("_next/static/2CXVybD6p7sPSHHHDiJuN/_ssgManifest.js");
	}};
	public static HashMap<String, byte[]> redirector(String in) {
		HashMap<String, byte[]> res = new HashMap<>();
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
