
import java.util.HashMap;

public class main {

	public static void main(String[] args) {
		String IP = "127.0.0.1";
		int PORT = 443;
		String username = args[0];
		String password = args[1];
		byte[] res = {};
		try {
		log.i("School name : " + JSON.QHM(new String(new HTTPSRequest(IP,PORT,"api.name").send()).split("\r\n\r\n")[1]).get("name"));
		log.i("About -> " + new String(new HTTPSRequest(IP,PORT,"api.name").send()).split("\r\n\r\n")[1]);
		log.i("logging in with user "+username+" and password "+password);
		HTTPSRequest login = new HTTPSRequest(IP,PORT,"api.login");
		res = login.send(JSON.HMQ(new HashMap<String, String>() {{
			put("user", username);
			put("pass", password);
		}}).getBytes());
		HashMap<String, String> reply = JSON.QHM(new String(res).split("\r\n\r\n")[1]);
		if(reply.containsKey("status")) log.e("Failed! Message: "+reply.get("msg"));
		else log.s("Successful! Session ID is "+reply.get("session_id")+" for user "+reply.get("first_name"));
		String SID = reply.get("session_id");
		log.i("Submitting a file with name test.txt and writer Tester and date 05/01/2022.");
		log.i("Step.1 : Generating a verify code...");
		HTTPSRequest generate = new HTTPSRequest(IP,PORT,"api.generate");
		res = generate.send(JSON.HMQ(new HashMap<String, String>() {{
			put("doc_name","test.txt");
			put("date","05/01/2022");
			put("writer","Tester");
			put("session_id",SID);
		}}).getBytes());
		reply = JSON.QHM(new String(res).split("\r\n\r\n")[1]);
		if(reply.containsKey("status")) log.e("Failed! Message: "+reply.get("msg"));
		else log.s("Successful! Verify code is "+reply.get("verify_code"));
		String verify_code = reply.get("verify_code");
		log.i("Step.2 : Submitting data to verify_code: "+verify_code);
		HTTPSRequest doc = new HTTPSRequest(IP, PORT, "api.doc");
		res = doc.send(new HashMap<String, String>(){{
			put("session_id",SID);
			put("extension","txt");
			put("verify_code",verify_code);
		}},"Hello World!".getBytes());
		reply = JSON.QHM(new String(res).split("\r\n\r\n")[1]);
		if(reply.containsKey("status")) log.e("Failed! Message: "+reply.get("msg"));
		else log.s("Successful! Public code is "+reply.get("public_code"));
		String public_code = reply.get("public_code");
		log.i("Searching for the document..");
		HTTPSRequest sfad = new HTTPSRequest(IP, PORT, "api.sfad");
		res = sfad.send(JSON.HMQ(new HashMap<String, String>() {{
			put("public_code",public_code);
		}}).getBytes());
		reply = JSON.QHM(new String(res).split("\r\n\r\n")[1]);
		if(reply.containsKey("status")) log.e("Failed! Message: "+reply.get("msg"));
		else {
			reply = JSON.QHM(new String(res).split("\r\n\r\n")[1]); 
			boolean isSuccess = false;
			if(reply.get("document_name").equals("test.txt")
					&& reply.get("verifier").equals(username)
					&& reply.get("writer").equals("Tester")) isSuccess = true;
			if(isSuccess) log.s("The feature is working properly!");
			else log.e("The feature is not working!\n"+new String(res));
		}
		log.i("Verifing the document..");
		HTTPSRequest vad = new HTTPSRequest(IP, PORT, "api.vad");
		res = vad.send(new HashMap<String, String>() {{put("verify_code",verify_code);}}, "Hello World!".getBytes());
		if(JSON.QHM(new String(res).split("\r\n\r\n")[1]).get("msg").equals("The file is identical with the verify code")) log.s("The feature is working properly!");
		else log.e("Error! Message : " + JSON.QHM(new String(res).split("\r\n\r\n")[1]).get("msg"));
		log.i("Downloading the document..");
		HTTPSRequest dac = new HTTPSRequest(IP, PORT, "api.dac");
		res = dac.send(JSON.HMQ(new HashMap<String, String>() {{put("public_code",public_code);}}).getBytes());
		if(new String(res).split("\r\n\r\n")[1].contains("\\{")) log.i("Returned a JSON Response : "+new String(res));
		else {
			if(new String(res).split("\r\n\r\n")[1].equals("Hello World!")) log.s("The feature is working properly!");
			else log.e("The feature is not working properly!\n"+new String(res));
		}
		}catch(Exception e) {
			log.e("Unknown!\n"+new String(res));
		}
	}
}