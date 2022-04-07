
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import lib.ArraySplit;
import lib.Network;
import lib.log;

public abstract class Server {
	int port = 80; // Port Number
	int MaxConcurrentRequests = 200; // Max requests to process at a time
	private int CurrentConcurrentRequests = 0; // Current Concurrent Requests
	boolean GZip = true; // GZip compression? (default true)
	int MAX_REQ_SIZE = 100000; // Max bytes to read in kb. (default 100MB)
	int backlog = 50; // Max requests to wait for processing, default is 5000MB
	String WWWDir = "www";
	String AddedResponseHeaders = ""; // Custom Response headers

	public void setMaximumConcurrentRequests(int in) {
		MaxConcurrentRequests = in;
	}

	public void setGZip(boolean in) {
		GZip = in;
	}

	public void setMaximumRequestSizeInKB(int in) {
		MAX_REQ_SIZE = in;
	}

	public void setBacklog(int in) {
		backlog = in;
	}
	public void HTTPSStart(int port, String KeyStorePath, String KeyStorePassword) {
		HTTPSStart(port, KeyStorePath, KeyStorePassword, "TLSv1.3", "JKS", "SunX509");
	}
	public void HTTPSStart(int port, String KeyStorePath, String KeyStorePassword, String TLSVersion,
			String KeyStoreType, String KeyManagerFactoryType) {
		// HTTPS Server start, default values
		try {
			char[] keyStorePassword = KeyStorePassword.toCharArray();
			ServerSocket SS = getSSLContext(Path.of(KeyStorePath), keyStorePassword, TLSVersion, KeyStoreType,
					KeyManagerFactoryType).getServerSocketFactory().createServerSocket(port, backlog);
			Arrays.fill(keyStorePassword, '0');
			while (true) {
				/*
				 * Max 10,000 retries (1ms delay between each) for every request to process if
				 * MaxConcurrentReqs is reached
				 */
				int tries = 0; // current tries
				inner: while (tries < 10001) {
					if (tries > 0) Thread.sleep(1);
					if (CurrentConcurrentRequests <= MaxConcurrentRequests) {
						Engine e = new Engine(SS.accept());
						e.start();
						CurrentConcurrentRequests++;
						break inner;
					} else {
						tries++;
					}
				}
			}
		} catch (Exception e) {
			log.e(e, Server.class.getName(), "HTTPSStart");
		}
	}

	private SSLContext getSSLContext(Path keyStorePath, char[] keyStorePass, String TLSVersion, String KeyStoreType,
			String KeyManagerFactoryType) {
		try {
			var keyStore = KeyStore.getInstance(KeyStoreType);
			keyStore.load(new FileInputStream(keyStorePath.toFile()), keyStorePass);
			var keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactoryType);
			keyManagerFactory.init(keyStore, keyStorePass);
			var sslContext = SSLContext.getInstance(TLSVersion);
			sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
			return sslContext;
		} catch (Exception e) {
			log.e(e, Server.class.getName(), "getSSLContext");
			return null;
		}
	}

	private byte[] toPrimitives(Byte[] oBytes) {
		byte[] bytes = new byte[oBytes.length];
		for (int i = 0; i < oBytes.length; i++) {
			bytes[i] = oBytes[i];
		}
		return bytes;
	}

	public class Engine extends Thread {
		// The main request processor
		Socket S;

		Engine(Socket in) {
			S = in;
		}

		@Override
		public void run() {
			try {
				DataInputStream DIS = new DataInputStream(S.getInputStream());
				DataOutputStream DOS = new DataOutputStream(S.getOutputStream());

				ArrayList<Byte> RequestInAL = Network.read(DIS, MAX_REQ_SIZE);
				byte[] Request = toPrimitives(RequestInAL.toArray(Byte[]::new));

				List<byte[]> ALm = ArraySplit.split(Request, new byte[] { 13, 10, 13, 10 }); // split by /r/n/r/n
				HashMap<String, byte[]> Reply = new HashMap<>(); // Reply
				/*
				 * Dynamic Mode
				 */
				Reply = main(ALm, DIS, DOS, (MAX_REQ_SIZE*1000) - Request.length);

				Network.write(DOS,
						Reply.get("content"),
						new String(Reply.get("mime")),
						new String(Reply.get("code")),
						GZip, AddedResponseHeaders);
				S.close();
			} catch (Exception e) {
				log.e(e, Engine.class.getName(), "run");
			} finally {
				CurrentConcurrentRequests--;
			}
			this.interrupt();
		}

	}

	abstract HashMap<String, byte[]> main(List<byte[]> aLm, DataInputStream DIS, DataOutputStream DOS, int max_size);
}
