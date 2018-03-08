package com.xzchaoo.learn.security.encrypt;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * @author xzchaoo
 * @date 2017/12/23
 */
public class KeyWrapperTest {
	@Test
	public void test() throws Exception {
		SecretKey sk = KeyGenerator.getInstance("AES").generateKey();
		System.out.println(Hex.encodeHexString(sk.getEncoded()));
		Cipher c = Cipher.getInstance("AES");
		c.init(Cipher.WRAP_MODE, sk);
		System.out.println(Hex.encodeHexString(c.wrap(sk)));

	}
}
