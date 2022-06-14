package lib;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	private static SecretKeySpec secretKey;
	private static byte[] key;

	public static void setKey(String myKey) {
		try {
			MessageDigest sha = null;
			key = myKey.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-256");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 32);
			secretKey = new SecretKeySpec(key, "AES");
		} catch (Exception e) {

		}
	}

	public static String encrypt(String strToEncrypt, String secret) throws Exception {
		try {
			setKey(secret);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(Arrays.copyOf(key, 16)));
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
		} catch (Exception e) {
			return "ERR.ERR.ERR";
		}
	}

	public static byte[] encrypt(byte[] strToEncrypt, String secret) throws Exception {
		try {
			setKey(secret);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(Arrays.copyOf(key, 16)));
			return Base64.getEncoder().encode(cipher.doFinal(strToEncrypt));
		} catch (Exception e) {
			return "ERR.ERR.ERR".getBytes();
		}
	}

	public static byte[] decrypt(byte[] strToDecrypt, String secret) throws Exception {
		try {
			setKey(secret);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(Arrays.copyOf(key, 16)));
			return cipher.doFinal(Base64.getDecoder().decode(strToDecrypt));
		} catch (Exception e) {
			e.printStackTrace();
			return "ERR.ERR.ERR".getBytes();
		}
	}

	public static String decrypt(String strToDecrypt, String secret) throws Exception {
		try {
			setKey(secret);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(Arrays.copyOf(key, 16)));
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt.getBytes("UTF-8"))));
		} catch (Exception e) {
			e.printStackTrace();
			return "ERR.ERR.ERR";
		}
	}
}