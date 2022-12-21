
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.file.Path;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import lib.ArraySplit;
import lib.Network;
import lib.log;
/**
 * Core Web Server Class
 * @author morad
 */
public abstract class Server {
	int port = 80; // Port Number
	volatile int MaxConcurrentRequests = 200; // Max requests to process at a time
	private volatile int CurrentConcurrentRequests = 0; // Current Concurrent Requests
	volatile boolean GZip = true; // GZip compression? (default true)
	volatile int MAX_REQ_SIZE = 100000; // Max bytes to read in kb. (default 100MB)
	int backlog = 50; // Max requests to wait for processing, default is 5000MB
	String WWWDir = "www";
	String AddedResponseHeaders = ""; // Custom Response headers
	/**
	 * Set Maximum Concurrent Requests/Threads
	 * @param in Number of max. threads/requests
	 */
	public void setMaximumConcurrentRequests(int in) {
		MaxConcurrentRequests = in;
	}
	/**
	 * Do you want GZip Compression?
	 * @param in true: GZip, false: no GZip
	 */
	public void setGZip(boolean in) {
		GZip = in;
	}
	/**
	 * Maximum request size to be read in KBs
	 * @param in Max size in KBs
	 */
	public void setMaximumRequestSizeInKB(int in) {
		MAX_REQ_SIZE = in;
	}
	/**
	 * Backlog specification
	 */
	public void setBacklog(int in) {
		backlog = in;
	}
	public void HTTPSStart(int port, String KeyStorePath, String KeyStorePassword) {
		HTTPSStart(port, KeyStorePath, KeyStorePassword, "TLSv1.3", "JKS", "SunX509");
	}
	/**
	 * Start HTTPS Server
	 * @param port Port for the web server
	 * @param KeyStorePath
	 * @param KeyStorePassword
	 * @param TLSVersion TLSv1.3
	 * @param KeyStoreType JKS
	 * @param KeyManagerFactoryType SunX509
	 */
	public void HTTPSStart(int port, String KeyStorePath, String KeyStorePassword, String TLSVersion,
			String KeyStoreType, String KeyManagerFactoryType) {
		// HTTPS Server start, default values
		ExecutorService executor = Executors.newFixedThreadPool(MaxConcurrentRequests);
		try {
			char[] keyStorePassword = KeyStorePassword.toCharArray();
			ServerSocket SS = getSSLContext(Path.of(KeyStorePath), keyStorePassword, TLSVersion, KeyStoreType,
					KeyManagerFactoryType).getServerSocketFactory().createServerSocket(port, backlog);
			Arrays.fill(keyStorePassword, '0');
			int req_num = 0;
			while (true) {
				/*
				 * Max 10 retries (1ms delay between each) for every request to process if
				 * MaxConcurrentReqs is reached
				 */
				int tries = 0; // current tries
				inner: while (tries < 10) {
					if (tries > 0)
						Thread.sleep(1);
					if (CurrentConcurrentRequests <= MaxConcurrentRequests) {
						Socket S = SS.accept();
						S.setKeepAlive(false);
						S.setTcpNoDelay(true);
						S.setReceiveBufferSize(65536);
						S.setSendBufferSize(65536);
						S.setSoTimeout(60000);
						executor.execute(new Engine(S, S.getRemoteSocketAddress()));
						CurrentConcurrentRequests++;
						if(req_num >= Integer.MAX_VALUE) req_num = 0;
						else req_num++;
						break inner;
					} else {
						tries++;
					}
				}
			}
		} catch (Exception e) {

		} finally {
			executor.shutdownNow();
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
	/**
	 * The first entry point for ANY request.
	 * @author morad
	 */
	public class Engine extends Thread {
		// The main request processor
		Socket S;
		SocketAddress SA;
		Engine(Socket in, SocketAddress SA) {
			S = in;
			this.SA = SA;
		}

		@Override
		public void run() {
			try {
				BufferedInputStream DIS = new BufferedInputStream(S.getInputStream(),65536);
				BufferedOutputStream DOS = new BufferedOutputStream(S.getOutputStream(),65536);
				byte[] Request = Network.read(DIS, MAX_REQ_SIZE).toByteArray();
				List<byte[]> ALm = ArraySplit.split(Request, new byte[] { 13, 10, 13, 10 }); // split by /r/n/r/n
				/*
				 * Dynamic Mode
				 */
				final HashMap<String, byte[]> Reply = main(ALm, DIS, DOS, (MAX_REQ_SIZE * 1024) - Request.length);
				boolean cache = false;
				if(! new String(Reply.get("mime")).equals("application/json")) cache = true;
				if(Reply.containsKey("extension")) AddedResponseHeaders += "extension: "+new String(Reply.get("extension"))+"\r\n";
				Network.write(DOS, Reply.get("content"), new String(Reply.get("mime")), new String(Reply.get("code")),
						GZip, AddedResponseHeaders, cache);
				S.close();
			} catch (Exception e) {

			} finally {
				CurrentConcurrentRequests--;
				AddedResponseHeaders = "";
			}
			this.interrupt();
		}

	}
	/**
	 * The dynamic Engine
	 * @param aLm Request split by '\r\n'
	 * @param dIS DataInputStream for the connection
	 * @param dOS DataOutputStream for the connection
	 * @param max_size Maximum request size in bytes.
	 * @return The request body, MIME type, Response code.
	 * @throws Exception
	 */
	abstract HashMap<String, byte[]> main(List<byte[]> aLm, BufferedInputStream dIS, BufferedOutputStream dOS, int max_size) throws Exception;
}
