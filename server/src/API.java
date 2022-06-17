import java.util.HashMap;
import java.util.Map;

import Endpoints.DataDoc;
import Endpoints.DownloadDoc;
import Endpoints.SearchDoc;
import Endpoints.VerifyDoc;
import Endpoints.about;
import Endpoints.generate;
import Endpoints.login;
import Endpoints.logout;
import Endpoints.name;
import lib.AES;
import lib.HTTPCode;
import lib.IO;
import lib.JSON;
import lib.SparkDB;
/**
 * API Entry Point
 * @author morad
 */
public class API {
	/**
	 * Encryption key of the server
	 */
	String ENCRYPTION_KEY = "";
	/**
	 * Session IDs. Key: ID, Value: Full name
	 */
	Map<String, String> SESSION_IDS;
	/**
	 * Verifiers DB
	 */
	SparkDB users = new SparkDB();
	/**
	 * DocsDB
	 */
	SparkDB docs = new SparkDB();
	/**
	 * Default MIME Type
	 */
	String mime = "application/json";
	/**
	 * Redirects API calls to desired functions in 'Endpoints' package
	 * @param Elshanta_temp Inputs, may vary in amount and type
	 * @return response body and MIME type
	 */
	public HashMap<String, Object> redirector(HashMap<String, Object> Elshanta_temp) throws Exception {
		String code = HTTPCode.OK;
		String PCode = "", session_id = "";
		String extension = "";
		byte[] BODY = null; // HTTPS Body
		HashMap<String, Object> res = new HashMap<>();
		// Encryption Key
		ENCRYPTION_KEY = (String) Elshanta_temp.get("encryption_key");
		// Request Body
		if (Elshanta_temp.containsKey("body")) BODY = (byte[]) Elshanta_temp.get("body");
		// Session IDs
		if (Elshanta_temp.containsKey("session_ids")) SESSION_IDS = (Map<String, String>) Elshanta_temp.get("session_ids");
		// Verifiers DB
		if (Elshanta_temp.containsKey("users_db")) users = (SparkDB) Elshanta_temp.get("users_db");
		// Document Code from headers
		if (Elshanta_temp.containsKey("code")) PCode = (String) Elshanta_temp.get("code");
		// Session ID from headers
		if (Elshanta_temp.containsKey("session_id")) session_id = (String) Elshanta_temp.get("session_id");
		// Extension from headers. eg. pdf/txt/mp4
		if (Elshanta_temp.containsKey("extension")) extension = (String) Elshanta_temp.get("extension");
		// Docs DB
		if (Elshanta_temp.containsKey("docs_db")) docs = (SparkDB) Elshanta_temp.get("docs_db");
		(new Thread() {
			@Override
			public void run() {
				try {
					IO.write("./conf/queries.txt",
							AES.encrypt(String.valueOf(Integer.parseInt(AES.decrypt(new String(IO.read("./conf/queries.txt")), ENCRYPTION_KEY)) + 1),ENCRYPTION_KEY),
							false);
				}catch (Exception e) {
					// Silent
				}
			}
		}).start();
		String in = (String) Elshanta_temp.get("api");
		if (in.equals("name")) {
			res.put("body", new name().run(ENCRYPTION_KEY).getBytes());
			res.put("code", code.getBytes());
		} else if (in.equals("about")) {
			res.put("body", new about().run(ENCRYPTION_KEY, docs).getBytes());
			res.put("code", code.getBytes());
		} else if (in.equals("login")) {
			Map<String, Object> login = new login().run(BODY,users, SESSION_IDS);
			res.put("body", ((String) login.get("body")).getBytes()); // login
			res.put("code", code.getBytes());
			SESSION_IDS = (Map<String, String>) login.get("SID");
		} else if (in.equals("SearchDoc")) {
			res.put("body", new SearchDoc().run(BODY, ENCRYPTION_KEY).getBytes());
			res.put("code", code.getBytes());
		}
			else if (in.equals("DownloadDoc")) {
				Map<String, byte[]> temp = new DownloadDoc().run(BODY, ENCRYPTION_KEY);
			res.put("body", temp.get("body"));
			mime = new String(temp.get("mime"));
			res.put("code", code.getBytes());
		} else if (in.equals("generate")) {
			res.put("body", new generate().run(BODY, SESSION_IDS, ENCRYPTION_KEY, docs).getBytes()); // generate verification code
			res.put("code", code.getBytes());
		} else if (in.equals("VerifyDoc")) {
			res.put("body", new VerifyDoc().run(BODY, PCode, ENCRYPTION_KEY).getBytes()); // verify a document
			res.put("code", code.getBytes());
		} else if (in.equals("logout")) {
			Map<String, Object> temp = new logout().run(BODY, users, SESSION_IDS);
			res.put("body", ((String) temp.get("body")).getBytes()); // verify a document
			res.put("code", code.getBytes());
			SESSION_IDS = (Map<String, String>) temp.get("SID");
		} else if (in.equals("DataDoc")) {
			res.put("body", new DataDoc().run(BODY, session_id, PCode, extension, SESSION_IDS, ENCRYPTION_KEY).getBytes()); // SAD S.2
			res.put("code", code.getBytes());
		} else {
			res.put("body", JSON.HMQ(new HashMap<String, String>() {
				{
					put("status", "failed");
					put("msg", "API request isn't understood");
				}
			}).getBytes());
			code = HTTPCode.BAD_REQUEST;
			res.put("code", code.getBytes());
		}
		if (res.get("body").equals("error")) code = HTTPCode.INTERNAL_SERVER_ERROR;
		if (Elshanta_temp.containsKey("session_ids")) res.put("session_ids", SESSION_IDS);
		if (Elshanta_temp.containsKey("docs_db")) res.put("docs", docs);
		res.put("mime", mime.getBytes());
		return res;
	}
}
