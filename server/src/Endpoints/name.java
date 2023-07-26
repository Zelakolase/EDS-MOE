package Endpoints;

import java.util.HashMap;

import lib.JSON;
import lib.SparkDB;

public class name {
	public String run(String ENCRYPTION_KEY, SparkDB metadata) throws Exception{
			return JSON.HMQ(new HashMap<String, String>() {
				{
					put("name", metadata.get(0).get("entityName"));
				}
			});
	}
}
