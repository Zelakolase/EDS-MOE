package Endpoints;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;

import lib.JSON;
import lib.SparkDB;

public class VerifyDoc {
	/**
	 * Verify a document
	 * Request : Body : raw data of file - Headers : 'code'<br>
	 * Response : {"msg" : "x"}, where x can be 'Verify Code isn't found' or 'The file is identical with the verify code' or 'The file isn't identical with the verify code'
	 * @param BODY Request Body
	 * @param code Document Code from headers
	 * @param Key Encryption Key
	 */
	public String run(byte[] BODY, String code, String Key) throws Exception {
		String res = "error";
		code = code.replaceAll("-", "");
		SparkDB db = new SparkDB();
		db.readFromFile("./conf/doc/"+code.substring(0,3)+".db", Key);
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
