package Endpoints;

import java.io.File;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import lib.AES;
import lib.IO;
import lib.JSON;
import lib.RandomGenerator;
import lib.SparkDB;

public class DataDoc {
	/**
	 * Puts raw data to disk, and completes document insertion
	 * Request: Body: Raw file data - Headers: 'session_id' and 'extension' and 'code'
	 * Response : {"status" : "success/failed"}. if status is failed, added -> {"msg","a"}
	 * @param BODY Request Body
	 * @param session_id Session ID grabbed from headers
	 * @param code Document Code grabbed from headers
	 * @param extension Extension for input file grabbed from headers
	 * @param sESSION_IDS Session IDs list
	 * @param ENCRYPTION_KEY Server Encryption Key
	 */
	public String run(byte[] BODY, String session_id, String code, String extension, Map<String, String> sESSION_IDS, String ENCRYPTION_KEY) throws Exception {
		String res = "error";
			if (sESSION_IDS.containsKey(session_id)) {
				String path = "./docs/" + RandomGenerator.getSaltString(20, 0) + "." + extension; // ./docs/aakF5igjdfigjdo.pdf
				new File(path).createNewFile();
				IO.write(path, AES.encrypt(BODY, ENCRYPTION_KEY), false);
				SparkDB db = new SparkDB();
				code = code.replaceAll("-", "");
				db.readFromFile("./conf/doc/"+code.substring(0,3)+".db", ENCRYPTION_KEY);
				final String tempC = code;
				int targetModifyIndex = db.getIDs(new HashMap<String, String>() {{
					put("code",tempC);
				}},1).get(0);
				db.modify(targetModifyIndex, new HashMap<String, String>() {{
					put("path", path);
					put("sha", Arrays.toString(MessageDigest.getInstance("SHA-256").digest(BODY)));
				}});
				IO.write("./conf/doc/"+code.substring(0,3)+".db", AES.encrypt(db.toString(), ENCRYPTION_KEY), false);
				res = JSON.HMQ(new HashMap<String, String>() {
					{
						put("status", "success");
					}
				});
			} else {
				res = JSON.HMQ(new HashMap<String, String>() {
					{
						put("status", "failed");
						put("msg", "Session ID isn't found");
					}
				});
			}
		return res;
	}
}
