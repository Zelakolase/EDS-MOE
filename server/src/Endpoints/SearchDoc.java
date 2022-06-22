package Endpoints;

import java.util.HashMap;

import lib.JSON;
import lib.SparkDB;

public class SearchDoc {
	/**
	 * Search for a document using the document code
	 * Request : {"code" : "a"}<br>
	 * Response : {"status" : "failed" , "msg" : "b"} or {"document_name" : "c" , "verifier" : "d" , "writer" : "e" , "date_of_publication" : "f"}
	 * @param Key Encryption Key
	 * @param BODY Request Body
	 */
	public String run(byte[] BODY, String Key) throws Exception {
			String res = "error";
			HashMap<String, String> in = JSON.QHM(new String(BODY));
			String PublicCode = in.get("code").replaceAll("-", ""); // 123-456-789 --> 123456789
			SparkDB db = new SparkDB();
			db.readFromFile("./conf/doc/"+PublicCode.substring(0, 3)+".db", Key); // read ./conf/doc/000.db
			if(db.getColumn("code").contains(PublicCode)) {
				// The document code is present
				int targetRowIndex = db.getIDs(new HashMap<String, String>() {{
					put("code", PublicCode);
				}}, 1).get(0);
				res = JSON.HMQ(new HashMap<String,String>() {{
					put("document_name", db.getColumn("doc_name").get(targetRowIndex));
					put("verifier", db.getColumn("verifier").get(targetRowIndex));
					put("writer", db.getColumn("writer").get(targetRowIndex));
					put("date_of_publication", db.getColumn("date").get(targetRowIndex));
				}});
			}else {
				// The document code is not present
				res = JSON.HMQ(new HashMap<String, String>() {{
					put("status","failed");
					put("msg","Document code is not found");
				}});
			}
			return res;
	}
}
