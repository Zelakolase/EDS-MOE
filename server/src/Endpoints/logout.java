package Endpoints;

import java.util.HashMap;
import java.util.Map;

import lib.JSON;
import lib.SparkDB;

public class logout {
	public Map<String, Object> run(byte[] BODY, SparkDB users, Map<String, String> SESSION_IDS) throws Exception{
		Map<String, Object> out = new HashMap<>();
			HashMap<String, String> in = JSON.QHM(new String(BODY));
			if (SESSION_IDS.containsKey(in.get("session_id"))) {
				String password = users.get(new HashMap<String, String>() {{
					put("full_name", SESSION_IDS.get(in.get("session_id")));
				}}, "pass",1).get(0);
				if (password.equals(in.get("pass"))) {
					out.put("body",JSON.HMQ(new HashMap<String, String>() {
						{
							put("status", "success");
						}
					}));
					SESSION_IDS.remove(in.get("session_id"));
				} else {
					out.put("body", JSON.HMQ(new HashMap<String, String>() {{
						put("status","failed");
					}}));
				}
			}else {
				out.put("body", JSON.HMQ(new HashMap<String, String>() {
					{
						put("status", "failed");
					}
				}));
			}
			out.put("SID", SESSION_IDS);
			return out;
	}
}
