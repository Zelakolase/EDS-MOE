package lib;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
/**
 * AES Encryption/Decryption Library
 * @author morad
 */
public class AES {
	private static SecretKeySpec secretKey;
	private static byte[] key;
	public AES(String sec) {
		setKey(sec);
	}
	/**
	 * Takes first 32 bytes (256 bits) of the SHA256 of the original key
	 * @param myKey Cipher Key
	 */
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
	/**
	 * Encrypt String data in Cipher key
	 * @param strToEncrypt Data to be encrypted
	 * @return The encrypted data after encryption
	 */
	public String encrypt(String strToEncrypt) throws Exception {
		try {
			return new String(encrypt(strToEncrypt.getBytes()));
		} catch (Exception e) {
			return "ERR.ERR.ERR";
		}
	}
	/**
	 * Encrypt byte data in Cipher key
	 * @param strToEncrypt Data to be encrypted
	 * @return The encrypted data after encryption
	 */
	public byte[] encrypt(byte[] strToEncrypt) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(Arrays.copyOf(key, 16)));
			return Base64.getEncoder().encode(cipher.doFinal(strToEncrypt));
		} catch (Exception e) {
			return "ERR.ERR.ERR".getBytes();
		}
	}
	/**
	 * Decrypt byte data in Cipher key
	 * @param strToDecrypt Data to be decrypted
	 * @return The decrypted data
	 */
	public byte[] decrypt(byte[] strToDecrypt) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(Arrays.copyOf(key, 16)));
			return cipher.doFinal(Base64.getDecoder().decode(strToDecrypt));
		} catch (Exception e) {
			e.printStackTrace();
			return "ERR.ERR.ERR".getBytes();
		}
	}
	/**
	 * Decrypt String data in Cipher key
	 * @param strToDecrypt Data to be decrypted
	 * @return The decrypted data
	 */
	public String decrypt(String strToDecrypt) throws Exception {
		try {
			return new String(decrypt(strToDecrypt.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
			return "ERR.ERR.ERR";
		}
	}
}