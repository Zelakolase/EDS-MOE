
import java.util.HashMap;

public class main {

	public static void main(String[] args) {
		String IP = "127.0.0.1";
		String username = args[0];
		String password = args[1];
		byte[] res = {};
		String R = "\r\n\r\n";
		HashMap<String, String> out = new HashMap<>();
		try {
			log.s("School name : "+new String(new API(IP,"api.name").send()).split(R));
			out = JSON.QHM(new String(new API(IP,"api.about").send()));
			log.s("Number of current queries is "+out.get("query_num")+" and the current document count is "+out.get("document_num"));
			out = JSON.QHM(new String(new API(IP,"api.login").send(
					JSON.HMQ(new HashMap<String, String>() {{
						put("user",username);
						put("pass",password);
					}}).getBytes()
					)).split(R)[1]);
			if(out.containsKey("status")) {
				log.e("Login is failed. "+out.get("msg"));
				System.exit(1);
			}else {
				log.s("Login is successful, Session ID is "+out.get("session_id")+". The first name is "+out.get("first_name"));
			}
			log.i("Testing document insertion for test.txt written by tester.");
			String sid = out.get("session_id");
			out = JSON.QHM(new String(new API(IP, "api.generate").send(JSON.HMQ(
					new HashMap<String, String>() {{
						put("session_id", sid);
						put("doc_name", "test.txt");
						put("writer","tester");
					}}
					).getBytes())).split(R)[1]);
			if(out.containsKey("status")) {
				log.e("Code generation is failed. "+out.get("msg"));
				System.exit(1);
			}else {
				log.s("Successful code generation! Code is "+out.get("code"));
			}
			String code = out.get("code");
			out = JSON.QHM(new String(new API(IP, "api.DataDoc").send(new HashMap<String, String>(){{
				put("session_id", sid);
				put("code",code);
				put("extension", "txt");
			}} ,"Hello World!".getBytes())).split(R)[1]);
			if(out.get("status").equals("success")) {
				log.s("Successful document insertion!");
			} else {
				log.e("Failed document insertion. "+out.get("msg"));
				System.exit(1);
			}
			out = JSON.QHM(new String(new API(IP, "api.SearchDoc").send(JSON.HMQ(
					new HashMap<String, String>() {{
						put("code", code);
					}}
					).getBytes())).split(R)[1]);
			if(out.containsKey("status")) {
				log.e("Failed Searching the document. "+out.get("msg"));
				System.exit(1);
			}else {
				log.s("Successful retreival. "+out);
			}
			String stuff = new String(new API(IP, "api.DownloadDoc").send(JSON.HMQ(
					new HashMap<String, String>() {{
						put("code", code);
					}}
					).getBytes())).split(R)[1];
			if(stuff.contains("\\{")) {
				log.e("Failed downloading document. "+stuff);
				System.exit(1);
			}else {
				log.s("Successful retreival. Content : "+stuff);
			}
			out = JSON.QHM(new String(new API(IP, "api.VerifyDoc").send(new HashMap<String, String>(){{
				put("code",code);
			}} ,"Hello World!".getBytes())).split(R)[1]);
			log.i("Verifing document result : "+out.get("msg"));
			
		}catch(Exception e) {
			e.printStackTrace();
			log.e("Response : \n"+new String(res));
		}
	}
}