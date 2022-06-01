
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HTTPSRequest {
	private String IP = "";
	private int PORT = 0;
	private String API_POINT = "";

	public HTTPSRequest(String ip, int port, String api) {
		this.IP = ip;
		this.PORT = port;
		this.API_POINT = api;
	}
	public byte[] send() {
		return send(new byte[] {});
	}
	public byte[] send(byte[] body) {
		return send(new HashMap<String, String>(), body);
	}
	public byte[] send(HashMap<String, String> Headers, byte[] body) {
		try {
			String request = "GET /"+API_POINT+"\r\n";
			SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			SSLSocket socket = (SSLSocket) factory.createSocket(IP, 443);
			socket.startHandshake();
			for (Entry<String, String> e : Headers.entrySet()) {
				request += (e.getKey() + ": " + e.getValue()+"\r\n");
			}
			request += "\r\n";
			BufferedOutputStream BOS = new BufferedOutputStream(socket.getOutputStream(), 1024);
			BufferedInputStream BIS = new BufferedInputStream(socket.getInputStream(), 1024);
			BOS.write(request.getBytes());
			BOS.write(body);
			BOS.flush();
			return BIS.readAllBytes();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
