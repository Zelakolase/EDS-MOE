package Endpoints;

import java.util.HashMap;
import java.util.Map;

import lib.JSON;
import lib.RandomGenerator;
import lib.SHA;
import lib.SparkDB;
import lib.TOTP;
import lib.TOTP.Secret;

public class login {
	public Map<String, Object> run(byte[] BODY, SparkDB users, Map<String, String> SESSION_IDS) throws Exception {
			Map<String, Object> out = new HashMap<>();
			HashMap<String, String> in = JSON.QHM(new String(BODY));
			String SHAedUsername = SHA.gen(in.get("user"));
			String SHAedPassword = SHA.gen(in.get("pass"));
			if (users.Mapper.get("user").contains(SHA.gen(in.get("user")))
					&& users.get(new HashMap<String, String>() {{
						put("user",SHAedUsername);
					}},"pass",1).get(0)
					.equals(SHAedPassword)
					&&
					new TOTP().validate(Secret.fromBase32(users.get(new HashMap<>() {{
						put("user", SHAedUsername);
					}}, "otp",1).get(0)), in.get("otp"))
					) {
				String random = RandomGenerator.getSaltString(50, 0); // Session ID
				SESSION_IDS.put(random, users.get(new HashMap<String, String>(){{
					put("user", SHAedUsername);
				}},"full_name",1).get(0));
				out.put("body", JSON.HMQ(new HashMap<String, String>() {
					{
						put("session_id", random);
						put("first_name", users.get(new HashMap<String, String>() {{
							put("user", SHAedUsername);
						}}, "full_name",1).get(0));
					}
				}));
			} else {
				out.put("body", JSON.HMQ(new HashMap<String, String>() {
					{
						put("status", "failed");
						put("msg", "Username or password or OTP isn't correct");
					}
				}));
			}

			out.put("SID", SESSION_IDS);
			return out;
	}
}
