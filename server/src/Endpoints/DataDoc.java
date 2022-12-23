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
					put("sha", Arrays.toString(MessageDigest.getInstance("SHA3-512").digest(BODY)));
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
