import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.List;

import lib.AES;
import lib.HTTPCode;
import lib.HeaderToHashmap;
import lib.IO;
import lib.SparkDB;
import lib.log;

public class Engine extends Server {
	/**
	 * Stage 3, HTTPS
	 */
	static String ENCRYPTION_KEY;
	static SparkDB MIME = new SparkDB();
	static HashMap<String, String> SESSION_IDS = new HashMap<>(); // id, username
	static SparkDB users = new SparkDB();
	// Here goes the constant objects that never removed after request process

	Engine(String ENC) {
		ENCRYPTION_KEY = ENC;
	}

	public void run() {
		try {
		MemMonitor MM = new MemMonitor();
		MM.start();
		MIME.readfromfile("mime.db");
		users.readfromstring(AES.decrypt(new String(IO.read("./conf/users.db")), ENCRYPTION_KEY));
		this.setMaximumConcurrentRequests(1500);
		this.setMaximumRequestSizeInKB(100000); // 100MB
		this.setGZip(false);
		this.setBacklog(10000);
		this.AddedResponseHeaders = "X-XSS-Protection: 1; mode=block\r\n" + "X-Frame-Options: DENY\r\n"
				+ "X-Content-Type-Options: nosniff\r\n";
		this.HTTPSStart(443, "./keystore.jks", "SWSTest");
		}catch(Exception e) {
			log.e(e, "Engine", "run()");
		}
	}

	@Override
	HashMap<String, byte[]> main(List<byte[]> aLm, DataInputStream DIS, DataOutputStream DOS) {
		try {
			HashMap<String, String> headers = HeaderToHashmap.convert(new String(aLm.get(0))); // headers
			HashMap<String, byte[]> response = new HashMap<>(); // content, mime, code
			HashMap<String, Object> Elshanta = new HashMap<>(); // stuff to send to API class
			if (headers.get("path").startsWith("/api.")) {
				/**
				 * API request detected
				 */
				String ser = headers.get("path").replaceFirst("/api.", "");
				if (ser.equals("login") || ser.equals("dac") || ser.equals("vad") || ser.equals("sfad") || ser.equals("generate") || ser.equals("doc")) {
					Elshanta.put("body", PostRequestMerge.merge(aLm, DIS, headers));
				}
				Elshanta.put("api", ser);
				Elshanta.put("encryption_key", ENCRYPTION_KEY);
				Elshanta.put("session_ids", SESSION_IDS);
				Elshanta.put("users_db", users);
				if(headers.containsKey("verify_code")) Elshanta.put("verify_code", headers.get("verify_code"));
				if(headers.containsKey("session_id")) Elshanta.put("session_id", headers.get("session_id"));
				if(headers.containsKey("extension")) Elshanta.put("extension", headers.get("extension"));
				HashMap<String, byte[]> res = API.redirector(Elshanta); // type, body
				response.put("content", res.get("body"));
				response.put("code", HTTPCode.OK.getBytes());
				response.put("mime", "application/json".getBytes());
			} else {
				/**
				 * Static file request detected
				 */
				HashMap<String, byte[]> static_res = FileProcess.redirector(headers.get("path").substring(1));
				response.put("content", static_res.get("body"));
				response.put("code", static_res.get("code"));
				String[] pathSplit = headers.get("path").split("\\.");
				response.put("mime", MIME.get("extension", pathSplit[pathSplit.length - 1], "mime").getBytes());
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<>() {
				{
					put("content", "Server error".getBytes());
					put("code", HTTPCode.SERVICE_UNAVAILABLE.getBytes());
					put("mime", "text/html".getBytes());
				}
			};
			
		}
	}
}
