package lib;
/**
 * HTTP Path filter to prevent LFI
 * @author Morad A.
 */
public class PathFilter {
	public static String filter(String path) {
		String res = path;
		res = res.replaceAll("\\.\\.", ""); // LFI protection, i guess?
		res = res.replaceAll("//", "/");
		if (res.endsWith("/")) res = res + "index.html";
		return res;
	}
}
