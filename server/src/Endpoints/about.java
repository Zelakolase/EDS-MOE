package Endpoints;


import java.util.ArrayList;
import java.util.HashMap;

import lib.AES;
import lib.IO;
import lib.JSON;
import lib.SparkDB;

public class about {

	/**
	 * About data
	 * Request : GET Request
	 * Response : {"query_num" : "a" , "document_num" : "b"}
	 * @param Key Encryption Key
	 * @param docs DocsDB
	 */
	public String run(String Key, SparkDB docs) throws Exception{
			HashMap<String, String> data = new HashMap<>();
			data.put("query_num",AES.decrypt(new String(IO.read("./conf/queries.txt")), Key));
			ArrayList<String> docnums = new ArrayList<>();
			docnums = docs.getColumn("size"); // Get all sizes
			int docnum = 0;
			for(String temp : docnums) {
				if(!temp.equals("0")) docnum += Integer.parseInt(temp);
			}
			data.put("document_num", String.valueOf(docnum));
			return JSON.HMQ(data);
	}
}
