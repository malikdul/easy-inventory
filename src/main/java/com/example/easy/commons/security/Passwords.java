package com.example.easy.commons.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * A utility class to hash passwords and check passwords vs hashed values. It
 * uses a combination of hashing and unique salt. The algorithm used is
 * PBKDF2WithHmacSHA1 which, although not the best for hashing password (vs.
 * bcrypt) is still considered robust and
 * <a href="http://security.stackexchange.com/a/6415/12614"> recommended by NIST
 * </a>. The hashed value has 256 bits.
 */
public class Passwords
{

	private static final Random RANDOM = new SecureRandom();
	private static final int ITERATIONS = 10000;
	private static final int KEY_LENGTH = 256;

	/**
	 * static utility class
	 */
	private Passwords()
	{
	}

	/**
	 * Returns a random salt to be used to hash a password.
	 *
	 * @return a 16 bytes random salt
	 */
	public static byte[] getNextSalt()
	{
		byte[] salt = new byte[127];
		RANDOM.nextBytes(salt);
		return salt;
		// return BCrypt.gensalt(12).getBytes();
	}

	/**
	 * Returns a salted and hashed password using the provided hash.<br>
	 * Note - side effect: the password is destroyed (the char[] is filled with
	 * zeros)
	 *
	 * @param password
	 *            the password to be hashed
	 * @param salt
	 *            a 16 bytes salt, ideally obtained with the getNextSalt method
	 *
	 * @return the hashed password with a pinch of salt
	 */
	public static byte[] hash(char[] password, byte[] salt)
	{
		PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
		Arrays.fill(password, Character.MIN_VALUE);
		try
		{
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			return skf.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e)
		{
			throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
		} finally
		{
			spec.clearPassword();
		}
	}

	/**
	 * Returns true if the given password and salt match the hashed value, false
	 * otherwise.<br>
	 * Note - side effect: the password is destroyed (the char[] is filled with
	 * zeros)
	 *
	 * @param password
	 *            the password to check
	 * @param salt
	 *            the salt used to hash the password
	 * @param expectedHash
	 *            the expected hashed value of the password
	 *
	 * @return true if the given password and salt match the hashed value, false
	 *         otherwise
	 */
	public static boolean isExpectedPassword(char[] password, byte[] salt, byte[] expectedHash)
	{
		byte[] pwdHash = hash(password, salt);
		Arrays.fill(password, Character.MIN_VALUE);
		if (pwdHash.length != expectedHash.length)
			return false;
		for (int i = 0; i < pwdHash.length; i++)
		{
			if (pwdHash[i] != expectedHash[i])
				return false;
		}
		return true;
	}

	/**
	 * Generates a random password of a given length, using letters and digits.
	 *
	 * @param length
	 *            the length of the password
	 *
	 * @return a random password
	 */
	public static String generateRandomPassword(int length)
	{
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++)
		{
			int c = RANDOM.nextInt(62);
			if (c <= 9)
			{
				sb.append(String.valueOf(c));
			} else if (c < 36)
			{
				sb.append((char) ('a' + c - 10));
			} else
			{
				sb.append((char) ('A' + c - 36));
			}
		}
		return sb.toString();
	}

	public static void main(String[] args)
	{
		/*
		 * byte[] pslat = Passwords.getNextSalt();
		 * System.out.println("slat::"+pslat); System.out.println(new
		 * String(pslat));
		 * 
		 * byte[] hash = Passwords.hash("tempPa2#sswrod".toCharArray(), pslat);
		 * 
		 * System.out.println(hash);
		 * 
		 * 
		 * System.out.println(Passwords.isExpectedPassword("tempPa2#sswrod".
		 * toCharArray(), pslat, hash));
		 */

		System.out.println(UUID.randomUUID().toString());
		String originalPassword = "i123456";
		
		String generatedSecuredPasswordHash = BCrypt.hashpw(originalPassword, BCrypt.gensalt(12));
		System.out.println("PASSWORD::" + generatedSecuredPasswordHash);

		originalPassword = "i123456";

		boolean matched = BCrypt.checkpw(originalPassword, generatedSecuredPasswordHash);
		System.out.println("matched::"+matched);
		try
		{
			long startTime = System.currentTimeMillis();
			String authToken = BCrypt.hashpw(Passwords.generateRandomPassword(1024), BCrypt.gensalt(12));
			System.out.println("12char aut token time::" + (System.currentTimeMillis() - startTime));
			System.out.println(authToken);

			authToken = BCrypt.hashpw(Passwords.generateRandomPassword(6), BCrypt.gensalt(8));
			System.out.println("8 char aut token time::" + (System.currentTimeMillis() - startTime));
			System.out.println(authToken);

		
		authToken = BCrypt.hashpw(Passwords.generateRandomPassword(6), BCrypt.gensalt(6));
		System.out.println("6 char aut token time::" + (System.currentTimeMillis() - startTime));
		System.out.println(authToken);
		
		
		System.out.println("########################################################3");
		SecretKey secretKey = null;
		try {
		secretKey = KeyGenerator.getInstance("AES").generateKey();
		} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
		}
		// get base64 encoded version of the key
		String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
		System.out.println(encodedKey);
		} catch (Exception exp)
		{
			exp.printStackTrace();
		} catch (Error e)
		{
			e.printStackTrace();
		}

	}
}