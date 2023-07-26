package Endpoints;

import java.util.HashMap;

import lib.FilePaths;
import lib.JSON;
import lib.SparkDB;

public class SearchDoc {
	public String run(byte[] BODY, String Key) throws Exception {
			String res = "error";
			HashMap<String, String> in = JSON.QHM(new String(BODY));
			String PublicCode = in.get("code").replaceAll("-", ""); // 123-456-789 --> 123456789
			SparkDB db = new SparkDB();
			db.readFromFile(FilePaths.ShardDirectory.getValue() + PublicCode.substring(0, 3)+".db", Key); // read ./conf/doc/000.db
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
