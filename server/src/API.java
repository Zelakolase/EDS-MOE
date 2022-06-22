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
	 * School name
	 */
	String school = "";
	/**
	 * MIMEs
	 */
	SparkDB MIME = new SparkDB();
	/**
	 * AES Object
	 */
	AES aes;
	/**
	 * Redirects API calls to desired functions in 'Endpoints' package
	 * @param Elshanta_temp Inputs, may vary in amount and type
	 * @return response body and MIME type and additional headers if necessary
	 */
	public HashMap<String, Object> redirector(HashMap<String, Object> Elshanta_temp) throws Exception {
		String code = HTTPCode.OK; // Default HTTP Code
		String PCode = "", session_id = "";
		String extension = "";
		byte[] BODY = null; // HTTPS Body
		HashMap<String, Object> res = new HashMap<>();
		// School Name
		if(Elshanta_temp.containsKey("name")) school = (String) Elshanta_temp.get("name");
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
		// MIMEs
		MIME = (SparkDB) Elshanta_temp.get("mime");
		// AES Obj
		aes = (AES) Elshanta_temp.get("aes");
		// We processed one query, write it down.
					IO.write("./conf/queries.txt",
							aes.encrypt(String.valueOf(Integer.parseInt(aes.decrypt(new String(IO.read("./conf/queries.txt")))) + 1)),
							false);
		String in = (String) Elshanta_temp.get("api");
		if (in.equals("name")) {
			res.put("body", new name().run(ENCRYPTION_KEY, school).getBytes());
			res.put("code", code.getBytes());
		} else if (in.equals("about")) {
			res.put("body", new about().run(ENCRYPTION_KEY, docs, aes).getBytes());
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
				Map<String, byte[]> temp = new DownloadDoc().run(BODY, ENCRYPTION_KEY, aes);
			res.put("body", temp.get("body"));
			mime = MIME.get(new HashMap<String, String>() {
				{
					put("extension", new String(temp.get("mime")));
				}
			}, "mime", 1).get(0);
			res.put("extension", new String(temp.get("mime")).getBytes());
			res.put("code", code.getBytes());
		} else if (in.equals("generate")) {
			res.put("body", new generate().run(BODY, SESSION_IDS, ENCRYPTION_KEY, docs, aes).getBytes()); // generate verification code
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
			res.put("body", new DataDoc().run(BODY, session_id, PCode, extension, SESSION_IDS, ENCRYPTION_KEY, aes).getBytes()); // SAD S.2
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
