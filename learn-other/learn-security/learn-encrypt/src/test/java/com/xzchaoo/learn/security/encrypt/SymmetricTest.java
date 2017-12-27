package com.xzchaoo.learn.security.encrypt;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.junit.Test;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * created by xzchaoo at 2017/12/23
 *
 * @author xzchaoo
 */
public class SymmetricTest {
	@Test
	public void test_generate_key() throws Exception {
		KeyGenerator generator = KeyGenerator.getInstance("AES", "BC");
		generator.init(192);
		Key encryptionKey = generator.generateKey();
		Key decryptionKey = new SecretKeySpec(encryptionKey.getEncoded(), encryptionKey.getAlgorithm());
		System.out.println(decryptionKey);
	}

	@Test
	public void test_aes() throws Exception {
		byte[] input = new byte[]{
			0x00, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77,
			(byte) 0x88, (byte) 0x99, (byte) 0xaa, (byte) 0xbb,
			(byte) 0xcc, (byte) 0xdd, (byte) 0xee, (byte) 0xff};

		byte[] keyBytes = new byte[]{
			0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
			0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
			0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17};
		//提供了byte[] 与 密钥的转换能力
		SecretKeySpec sk = new SecretKeySpec(keyBytes, "AES");

		Cipher c = Cipher.getInstance("AES/ECB/NoPadding");
		//初始化c为加密模式
		c.init(Cipher.ENCRYPT_MODE, sk);
		byte[] cipherText = new byte[input.length];
		int ctLength = c.update(input, 0, input.length, cipherText, 0);
		ctLength += c.doFinal(cipherText, ctLength);
		System.out.println("加密 " + ctLength + " " + Hex.encodeHexString(cipherText));

		byte[] plainTextBytes = new byte[ctLength];
		//初始化c为解密模式
		c.init(Cipher.DECRYPT_MODE, sk);
		int ptLength = c.update(cipherText, 0, ctLength, plainTextBytes, 0);
		ptLength += c.doFinal(plainTextBytes, ptLength);
		System.out.println(Hex.encodeHex(plainTextBytes));
	}

	@Test
	public void test_aes2() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		byte[] input = new byte[]{
			0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
			0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
			0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
			0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
		};

		byte[] keyBytes = new byte[]{
			0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
			0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
			0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17};

		//提供了byte[] 与 密钥的转换能力
		SecretKeySpec sk = new SecretKeySpec(keyBytes, "AES");

		Cipher c = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
		System.out.println(c.getBlockSize());
		//初始化c为加密模式
		c.init(Cipher.ENCRYPT_MODE, sk);
		//getOutputSize的返回值 一定是 大于等于 有效大小的
		byte[] cipherText = new byte[c.getOutputSize(input.length)];
		int ctLength = c.update(input, 0, input.length, cipherText, 0);
		ctLength += c.doFinal(cipherText, ctLength);
		System.out.println("加密 " + ctLength + " " + Hex.encodeHexString(cipherText));

		byte[] plainTextBytes = new byte[ctLength];
		//初始化c为解密模式
		c.init(Cipher.DECRYPT_MODE, sk);
		int ptLength = c.update(cipherText, 0, ctLength, plainTextBytes, 0);
		ptLength += c.doFinal(plainTextBytes, ptLength);
		System.out.println(Hex.encodeHex(ByteUtils.subArray(plainTextBytes, 0, ptLength)));
	}

	@Test
	public void test_des2() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		byte[] input = new byte[]{
			0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
			0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
			0x00, 0x01, 0x02};
		byte[] keyBytes = new byte[]{
			0x01, 0x23, 0x45, 0x67,
			(byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef};
		byte[] ivBytes = new byte[]{
			0x00, 0x01, 0x02, 0x03, 0x00, 0x00, 0x00, 0x01};

		SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");
		IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		Cipher cipher = Cipher.getInstance("DES/CTR/NoPadding", "BC");

		System.out.println("input : " + Hex.encodeHexString(input));

		// encryption pass

		cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

		byte[] cipherText = new byte[cipher.getOutputSize(input.length)];

		int ctLength = cipher.update(input, 0, input.length, cipherText, 0);

		ctLength += cipher.doFinal(cipherText, ctLength);

		System.out.println("cipher: " + Hex.encodeHexString(ArrayUtils.subarray(cipherText, 0, ctLength))
			+ " bytes: " + ctLength);

		// decryption pass

		cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

		byte[] plainText = new byte[cipher.getOutputSize(ctLength)];

		int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);

		ptLength += cipher.doFinal(plainText, ptLength);

		System.out.println("plain : " + Hex.encodeHexString(ArrayUtils.subarray(plainText, 0, ptLength))
			+ " bytes: " + ptLength);
	}

	@Test
	public void test_des() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		byte[] input = new byte[]{
			0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
			0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
			0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
			0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
		};

		byte[] keyBytes = new byte[]{
			0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
		};

		//这里使用的是CBC模式 因此需要提供IV
		//注意IV的大小必须和使用的算法的块大小一致
		byte[] ivBytes = new byte[]{0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00};

		//提供了byte[] 与 密钥的转换能力
		SecretKeySpec sk = new SecretKeySpec(keyBytes, "DES");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
		Cipher c = Cipher.getInstance("DES/CBC/PKCS7Padding", "BC");
		System.out.println(c.getBlockSize());
		//初始化c为加密模式
		c.init(Cipher.ENCRYPT_MODE, sk, ivParameterSpec);
		//getOutputSize的返回值 一定是 大于等于 有效大小的
		byte[] cipherText = new byte[c.getOutputSize(input.length)];
		int ctLength = c.update(input, 0, input.length, cipherText, 0);
		ctLength += c.doFinal(cipherText, ctLength);
		System.out.println("加密 " + ctLength + " " + Hex.encodeHexString(cipherText));

		byte[] plainTextBytes = new byte[ctLength];
		//初始化c为解密模式
		c.init(Cipher.DECRYPT_MODE, sk, ivParameterSpec);
		int ptLength = c.update(cipherText, 0, ctLength, plainTextBytes, 0);
		ptLength += c.doFinal(plainTextBytes, ptLength);
		System.out.println(Hex.encodeHex(ByteUtils.subArray(plainTextBytes, 0, ptLength)));
		c.init(Cipher.DECRYPT_MODE, sk, ivParameterSpec);
		System.out.println(Hex.encodeHex(c.doFinal(cipherText)));
	}

}
