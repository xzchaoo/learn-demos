package com.xzchaoo.learn.security.encrypt;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import java.security.Provider;
import java.security.Security;
import java.util.Iterator;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SimplePolicyTest {

	@Test
	public void test_provider_capacities() {
		Security.addProvider(new BouncyCastleProvider());
		Provider provider = Security.getProvider("BC");

		Iterator it = provider.keySet().iterator();

		while (it.hasNext()) {
			String entry = (String) it.next();

			// this indicates the entry actually refers to another entry

			if (entry.startsWith("Alg.Alias.")) {
				entry = entry.substring("Alg.Alias.".length());
			}

			String factoryClass = entry.substring(0, entry.indexOf('.'));
			String name = entry.substring(factoryClass.length() + 1);

			System.out.println(factoryClass + ": " + name);
		}

	}

	@Test
	public void test_policy() throws Exception {
		byte[] data = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};

		// create a 64 bit secret key from raw bytes
		SecretKey key64 = new SecretKeySpec(
			new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07},
			"Blowfish");

		// create a cipher and attempt to encrypt the data block with our key
		Cipher c = Cipher.getInstance("Blowfish/ECB/NoPadding");

		c.init(Cipher.ENCRYPT_MODE, key64);
		c.doFinal(data);
		System.out.println("64 bit test: passed");

		// create a 192 bit secret key from raw bytes
		SecretKey key192 = new SecretKeySpec(
			new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
				0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
				0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17},
			"Blowfish");

		// now try encrypting with the larger key
		c.init(Cipher.ENCRYPT_MODE, key192);
		c.doFinal(data);

		System.out.println("192 bit test: passed");

		System.out.println("Tests completed");
	}
}
