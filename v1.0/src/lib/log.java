package lib;

import java.io.PrintWriter;
import java.io.StringWriter;

public class log {
	static boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
	public static final String RESET = isWindows ? "\033[0;37m" : "\u001B[0m";
	public static final String RED = isWindows ? "\033[0;31m" : "\u001B[31m";
	public static final String GREEN = isWindows ? "\033[0;32m" : "\u001B[32m";
	public static final String CYAN = isWindows ? "\033[0;36m" : "\u001B[36m";

	/**
	 * Display error msg
	 *
	 * @param in message to print
	 */
	public static void e(String in) {
		System.out.println(RED + "[Error] " + in + RESET);
	}

	/**
	 * Display success msg
	 *
	 * @param in message to print
	 */
	public static void s(String in) {
		System.out.println(GREEN + "[Success] " + in + RESET);
	}

	/**
	 * Display informative msg
	 *
	 * @param in message to print
	 */
	public static void i(String in) {
		System.out.println(CYAN + "[Info] " + in + RESET);
	}

	/**
	 * Display error msg
	 *
	 * @param e         the exception object to gain data from
	 * @param className the name of the class where the error happened
	 * @param FuncName  the name of the function where the error happened
	 */
	public static void e(Exception e, String className, String FuncName) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		log.e(className + "." + FuncName + "(..)" + " : " + sw.toString());
	}
}
