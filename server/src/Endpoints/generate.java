package Endpoints;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import lib.AES;
import lib.FilePaths;
import lib.IO;
import lib.JSON;
import lib.RandomGenerator;
import lib.SparkDB;

public class generate {
	public String run(byte[] BODY, Map<String, String> SESSION_IDS, String ENCRYPTION_KEY, SparkDB docs, AES aes, SparkDB Metadata) throws Exception {
			String res = "";
			HashMap<String, String> in = JSON.QHM(new String(BODY));
			if (SESSION_IDS.containsKey(in.get("session_id"))) {
				// a. choose smallest index
				int targetIndex = 0;
				ArrayList<Integer> smallests = docs.getIDs(new HashMap<String, String>() {{
					put("size", Collections.min(docs.getColumn("size")));
				}});
				targetIndex = new Random().nextInt(smallests.size());
				// b. if size == 10k, sort with min_query. get minimum and target index
				if(docs.get(targetIndex).get("size").equals("10000")) targetIndex = docs.getColumn("min_query").indexOf(Collections.min(docs.getColumn("min_query")));
				// c. go to prefix.db, if size == 10k, delete row age minimum. min_query in docs is next age after deleted
				SparkDB db = new SparkDB();
				db.readFromFile(FilePaths.ShardDirectory.getValue() + docs.getColumn("prefix").get(targetIndex)+".db", ENCRYPTION_KEY);
				if(db.size() == 10000) {
					new File(db.getColumn("path").get(0)).delete();
					db.delete(0);
					docs.modify(targetIndex, new HashMap<String, String>() {{
						put("min_query",Collections.min(db.getColumn("age")));
					}});
				}
				// d. put data such that age is current doc.txt
				String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
				String PCode = "";
				do {
					PCode = docs.getColumn("prefix").get(targetIndex)+RandomGenerator.getSaltString(6, 2);
				}while(db.getColumn("code").contains(PCode));
				final String PFCode = PCode;
				db.add(new HashMap<String, String>() {{
					put("code", PFCode);
					put("path", "0");
					put("doc_name", in.get("doc_name"));
					put("verifier", SESSION_IDS.get(in.get("session_id")));
					put("writer", in.get("writer"));
					put("date", timeStamp);
					put("sha", "0");
					put("age", Metadata.get(0).get("numQueries"));
				}});
				// e. increase numDocs by 1
				long docNumber = Long.valueOf(Metadata.get(0).get("numDocs"));
				Metadata.modify(0, new HashMap<>() {{
					put("numDocs", String.valueOf(docNumber + 1));
				}});
				// f. write changes to disk. 000.db and docs change
				IO.write(FilePaths.ShardDirectory.getValue() +docs.getColumn("prefix").get(targetIndex)+".db", aes.encrypt(db.toString()), false);
				docs.modify(targetIndex, new HashMap<String, String>() {{
					put("size", String.valueOf(db.size()));
				}});
				HashMap<String, String> temp = new HashMap<>();
				temp.put("code", PCode);
				res = JSON.HMQ(temp);
				// g. write to Table.db
				SparkDB T = new SparkDB();
				T.readFromFile(FilePaths.ConfigurationDirectory.getValue() + "Table.db", ENCRYPTION_KEY);
				T.add(new HashMap<String, String>() {{
					put("DocName", in.get("doc_name"));
					put("Verifier", SESSION_IDS.get(in.get("session_id")));
					put("Writer", in.get("writer"));
					put("DocNum", PFCode);
				}});
				IO.write(FilePaths.ConfigurationDirectory.getValue() + "Table.db", aes.encrypt(T.toString()), false);
			} else {
				new JSON();
				res = JSON.HMQ(new HashMap<String, String>() {
					{
						put("status", "failed");
						put("msg", "Session ID isn't correct");
					}
				});
			}
			return res;
	}

}
