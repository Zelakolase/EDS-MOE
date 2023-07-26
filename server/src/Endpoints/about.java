package Endpoints;


import java.util.HashMap;

import lib.AES;
import lib.JSON;
import lib.SparkDB;

/**
 * Class for API Endpoint '/api.about'
 * @author Morad A.
 */
public class about {
	public String run(String Key, SparkDB metadata, AES aes) throws Exception {
		HashMap<String, String> data = new HashMap<>();
		long queryNumber = Long.valueOf(metadata.get(0).get("numQueries"));
		long docNumber = Long.valueOf(metadata.get(0).get("numDocs"));
		data.put("query_num", String.valueOf(queryNumber));
		data.put("document_num", String.valueOf(docNumber));
		return JSON.HMQ(data);
	}
}
