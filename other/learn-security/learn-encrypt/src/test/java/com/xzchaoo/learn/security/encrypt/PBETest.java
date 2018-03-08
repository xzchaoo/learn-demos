package com.xzchaoo.learn.security.encrypt;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author xzchaoo
 * @date 2017/12/23
 */
public class PBETest {
	@Test
	public void test() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		byte[] input = new byte[]{
			0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
			0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
			0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};
		byte[] keyBytes = new byte[]{
			0x73, 0x2f, 0x2d, 0x33, (byte) 0xc8, 0x01, 0x73,
			0x2b, 0x72, 0x06, 0x75, 0x6c, (byte) 0xbd, 0x44,
			(byte) 0xf9, (byte) 0xc1, (byte) 0xc1, 0x03, (byte) 0xdd,
			(byte) 0xd9, 0x7c, 0x7c, (byte) 0xbe, (byte) 0x8e};
		byte[] ivBytes = new byte[]{
			(byte) 0xb0, 0x7b, (byte) 0xf5, 0x22, (byte) 0xc8,
			(byte) 0xd6, 0x08, (byte) 0xb8};

		Cipher c = Cipher.getInstance("DESede/CBC/PKCS7Padding", "BC");
		c.init(
			Cipher.ENCRYPT_MODE,
			new SecretKeySpec(keyBytes, "DESede"),
			new IvParameterSpec(ivBytes)
		);
		byte[] out = c.doFinal(input);

		char[] password = "password".toCharArray();
		byte[] salt = new byte[]{0x7d, 0x60, 0x43, 0x5f, 0x02, (byte) 0xe9, (byte) 0xe0, (byte) 0Xae};
		int iterationCount = 2048;
		PBEKeySpec pbeSpec = new PBEKeySpec(password);
		SecretKeyFactory keyFact = SecretKeyFactory.getInstance("PBEWithSHAAnd3KeyTripleDES", "BC");
		Cipher cDec = Cipher.getInstance("PBEWithSHAAnd3KeyTripleDES", "BC");
		Key sKey = keyFact.generateSecret(pbeSpec);
		cDec.init(Cipher.DECRYPT_MODE, sKey, new PBEParameterSpec(salt, iterationCount));
		System.out.println("加密后的结果=" + Hex.encodeHexString(out));
		System.out.println("key=" + Hex.encodeHexString(sKey.getEncoded()));
		System.out.println("iv=" + Hex.encodeHexString(c.getIV()));
		System.out.println("解密后的结果=" + Hex.encodeHexString(cDec.doFinal(out)));
	}
}
