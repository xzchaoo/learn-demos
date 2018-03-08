package com.xzchaoo.learn.security.encrypt;

import org.junit.Test;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;

/**
 * 可以让双方(可以更多方)在一个不安全的环境下生成一个安全的keypair
 *
 * @author xzchaoo
 * @date 2017/12/24
 */
public class DHTest {
	@Test
	public void test() throws Exception {
		KeyPairGenerator kpg1 = KeyPairGenerator.getInstance("DH");
		//kpg1.initialize(1024);//DH算法要求size是 512~1024 的 64的倍数 默认是1024
		KeyPair kp1 = kpg1.generateKeyPair();
		DHPublicKey publicKey1 = (DHPublicKey) kp1.getPublic();
		DHPrivateKey privateKey1 = (DHPrivateKey) kp1.getPrivate();

		byte[] publicKeyBytes1 = publicKey1.getEncoded();
		DHPublicKey publicKey21 = (DHPublicKey) KeyFactory.getInstance("DH").generatePublic(new X509EncodedKeySpec(publicKeyBytes1));
		KeyPairGenerator kpg2 = KeyPairGenerator.getInstance("DH");
		//kpg2.initialize(publicKey21.getParams());//注意这里!
		KeyPair kp2 = kpg2.generateKeyPair();
		DHPublicKey publicKey2 = (DHPublicKey) kp2.getPublic();
		DHPrivateKey privateKey2 = (DHPrivateKey) kp2.getPrivate();


	}
}
