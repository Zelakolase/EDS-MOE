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
	 * Request: Body: Raw file data - Headers: 'session_id' and 'extension' and 'code'<br>
	 * Response : {"status" : "success/failed"}. if status is failed, added -> {"msg","a"}
	 * @param BODY Request Body
	 * @param session_id Session ID grabbed from headers
	 * @param code Document Code grabbed from headers
	 * @param extension Extension for input file grabbed from headers
	 * @param sESSION_IDS Session IDs list
	 * @param ENCRYPTION_KEY Server Encryption Key
	 * @param aes AES Object
	 */
	public String run(byte[] BODY, String session_id, String code, String extension, Map<String, String> sESSION_IDS, String ENCRYPTION_KEY, AES aes) throws Exception {
		String res = "error";
			if (sESSION_IDS.containsKey(session_id)) {
				String path = "./docs/" + RandomGenerator.getSaltString(20, 0) + "." + extension; // ./docs/aakF5igjdfigjdo.pdf
				new File(path).createNewFile();
				IO.write(path, aes.encrypt(BODY), false);
				SparkDB db = new SparkDB();
				code = code.replaceAll("-", "");
				db.readFromFile("./conf/doc/"+code.substring(0,3)+".db", ENCRYPTION_KEY);
				final String tempC = code;
				int targetModifyIndex = db.getIDs(new HashMap<String, String>() {{
					put("code",tempC);
				}},1).get(0);
				if(db.getColumn("path").get(targetModifyIndex).equals("0") && db.getColumn("sha").get(targetModifyIndex).equals("0")) {
				db.modify(targetModifyIndex, new HashMap<String, String>() {{
					put("path", path);
					put("sha", Arrays.toString(MessageDigest.getInstance("SHA-256").digest(BODY)));
				}});
				IO.write("./conf/doc/"+code.substring(0,3)+".db", aes.encrypt(db.toString()), false);
				final String FCode = code;
				res = JSON.HMQ(new HashMap<String, String>() {
					{
						put("code", FCode);
					}
				});
				}else {
					res = JSON.HMQ(new HashMap<String, String>() {
						{
							put("status", "failed");
							put("msg", "Data was previously written to the document code.");
						}
					});
				}
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
