package Endpoints;

import java.util.HashMap;

import lib.AES;
import lib.IO;
import lib.JSON;

public class name {
	/**
	 * The name of the school
	 * Request : GET Request
	 * Response : {"name" : "a"}
	 */
	public String run(String ENCRYPTION_KEY) throws Exception{
			return JSON.HMQ(new HashMap<String, String>() {
				{
					put("name", AES.decrypt(new String(IO.read("./conf/info.txt")), ENCRYPTION_KEY));
				}
			});
	}
}
