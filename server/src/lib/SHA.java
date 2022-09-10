package lib;

import java.security.MessageDigest;

public class SHA {
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    public static String gen(String in) {
        try {
            return bytesToHex(MessageDigest.getInstance("SHA3-512").digest(in.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
