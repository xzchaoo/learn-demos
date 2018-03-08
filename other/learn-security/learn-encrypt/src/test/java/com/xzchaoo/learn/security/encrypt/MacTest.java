package com.xzchaoo.learn.security.encrypt;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import java.security.Security;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * @author xzchaoo
 * @date 2017/12/23
 */
public class MacTest {
	@Test
	public void test() throws Exception {
		Mac mac = Mac.getInstance("HmacSHA1");
		SecretKey k = KeyGenerator.getInstance("HmacSHA1").generateKey();
		mac.init(k);
	}

	@Test
	public void test2() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		Mac mac = Mac.getInstance("PBEWithHmacSHA1", "BC");
		SecretKey sk = SecretKeyFactory.getInstance("PBEWithHmacSHA1", "BC")
			.generateSecret(new PBEKeySpec("haha".toCharArray(), new byte[8], 100));
		mac.init(sk);
		System.out.println(Hex.encodeHexString(mac.doFinal("hehe".getBytes())));
	}

	@Test
	public void test3() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		Mac mac = Mac.getInstance("AES", "BC");
	}
}
