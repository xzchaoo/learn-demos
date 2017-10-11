package com.xzchaoo.learn.security.encrypt.bc;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class SimpleSymmetricPaddingExample {
	@Test
	public void test() throws Exception {
		Security.addProvider(new BouncyCastleProvider());

		byte[] originalData = {0x1, 0x2};
		System.out.println("input : " + Hex.toHexString(originalData));

		byte[] keyBytes = new byte[]{
			0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
			0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
			0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17};

		SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "BC");
//		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
		//Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding", "BC"); 这种方式就要求你自己去对齐了
		System.out.println("block size=" + cipher.getBlockSize());

		// encryption pass
		cipher.init(Cipher.ENCRYPT_MODE, key);

		byte[] cipherText = new byte[cipher.getOutputSize(originalData.length)];

		int ctLength = cipher.update(originalData, 0, originalData.length, cipherText, 0);

		ctLength += cipher.doFinal(cipherText, ctLength);

		System.out.println("cipher: " + Hex.toHexString(cipherText) + " bytes: " + ctLength);

		// decryption pass
		cipher.init(Cipher.DECRYPT_MODE, key);

		byte[] plainText = new byte[cipher.getOutputSize(ctLength)];

		int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);

		ptLength += cipher.doFinal(plainText, ptLength);

		//可以看到输出的结果是被填充的(plainText) ptLength表示了这数组里的前ptLength 才是真实有效的数据
		System.out.println("plain : " + Hex.toHexString(plainText) + " bytes: " + ptLength);
	}
}