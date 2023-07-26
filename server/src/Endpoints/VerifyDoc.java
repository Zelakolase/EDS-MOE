package Endpoints;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;

import lib.FilePaths;
import lib.JSON;
import lib.SparkDB;

public class VerifyDoc {
	public String run(byte[] BODY, String code, String Key) throws Exception {
		String res = "error";
		code = code.replaceAll("-", "");
		SparkDB db = new SparkDB();
		db.readFromFile(FilePaths.ShardDirectory.getValue()+code.substring(0,3)+".db", Key);
		if(db.getColumn("code").contains(code)) {
			final String tempcode = code;
			boolean compare = db.get(new HashMap<String, String>() {{
				put("code",tempcode);
			}},"sha",1).get(0).equals(Arrays.toString(MessageDigest.getInstance("SHA3-512").digest(BODY)));
			String msg = compare ? "The file is identical with the verify code"
					: "The file isn't identical with the verify code";
			res = JSON.HMQ(new HashMap<String, String>() {
				{
					put("msg", msg);
				}
			});
		}else {
			res = JSON.HMQ(new HashMap<String, String>() {
				{
					put("msg","Verify Code isn't found");
				}
			});
		}
		return res;
	}

}
