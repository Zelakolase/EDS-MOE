package lib;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

public class ErrorWriter {
	public static HashMap<String, byte[]> wnAPI(Exception e, String ENCRYPTION_KEY, String Path) throws Exception {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String ErrID = lib.RandomGenerator.getSaltString(5, 0);
		IO.write("./other/error.log",AES.encrypt(
				"Error ID: "+ErrID+" | "
				+"Path: "+Path+" | "
				+"Error details: "+sw.toString()
				+ "\n\n\n",ENCRYPTION_KEY) , true);
		return new HashMap<>() {{
			put("content",("Error! If you see this error, please contact the server administrator with the error ID.<br>"
					+"Error ID: "+ErrID).getBytes());
			put("mime","text/html".getBytes());
			put("code",lib.HTTPCode.SERVICE_UNAVAILABLE.getBytes());
		}};
	}
	public static HashMap<String, Object> wAPI(Exception e, String ENCRYPTION_KEY, String APIPoint) throws Exception {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String ErrID = lib.RandomGenerator.getSaltString(5, 0);
		IO.write("./other/error.log",AES.encrypt(
				"Error ID: "+ErrID+" | "
				+"API point: "+APIPoint+" | "
				+"Error details: "+sw.toString()
				+ "\n\n\n",ENCRYPTION_KEY) , true);
		return new HashMap<>() {{
			put("body",("Error! If you see this error, please contact the server administrator with the error ID.<br>"
					+"Error ID: "+ErrID).getBytes());
			put("mime","text/html");
		}};
	}
}
