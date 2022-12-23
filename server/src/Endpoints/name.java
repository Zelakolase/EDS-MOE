package Endpoints;

import java.util.HashMap;

import lib.JSON;

public class name {
	public String run(String ENCRYPTION_KEY, String SCHOOL) throws Exception{
			return JSON.HMQ(new HashMap<String, String>() {
				{
					put("name", SCHOOL);
				}
			});
	}
}
