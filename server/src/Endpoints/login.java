package Endpoints;

import java.util.HashMap;
import java.util.Map;

import lib.JSON;
import lib.RandomGenerator;
import lib.SparkDB;

public class login {
	/**
	 * Login functionality for verifiers
	 * Request : {"user" : "a" , "pass" : "b"}
	 * Response : {"session_id" : "c" , "first_name" : "d"} or {"status" : "failed" , "msg" : "e"}
	 * @param BODY Request Body
	 * @param users UsersDB
	 * @param SESSION_IDS Session IDs list
	 */
	public Map<String, Object> run(byte[] BODY, SparkDB users, Map<String, String> SESSION_IDS) throws Exception {
			Map<String, Object> out = new HashMap<>();
			HashMap<String, String> in = JSON.QHM(new String(BODY));
			if (users.Mapper.get("user").contains(in.get("user"))
					&& users.get(new HashMap<String, String>() {{
						put("user",in.get("user"));
					}},"pass",1).get(0)
					.equals(in.get("pass"))) {
				String random = RandomGenerator.getSaltString(50, 0); // Session ID
				SESSION_IDS.put(random, users.get(new HashMap<String, String>(){{
					put("user",in.get("user"));
				}},"full_name",1).get(0));
				out.put("body", JSON.HMQ(new HashMap<String, String>() {
					{
						put("session_id", random);
						put("first_name", users.get(new HashMap<String, String>() {{
							put("user",in.get("user"));
						}}, "full_name",0).get(0));
					}
				}));
			} else {
				out.put("body", JSON.HMQ(new HashMap<String, String>() {
					{
						put("status", "failed");
						put("msg", "Username or password isn't correct");
					}
				}));
			}
			out.put("SID", SESSION_IDS);
			return out;
	}
}
