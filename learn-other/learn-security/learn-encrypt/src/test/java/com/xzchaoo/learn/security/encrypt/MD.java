package com.xzchaoo.learn.security.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.util.DigestFactory;
import org.bouncycastle.jcajce.provider.digest.MD4;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.DigestOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

import static org.junit.Assert.*;

/**
 * 数字摘要:
 *      完全公开: MD5 sha: md5 sha1 sha256 用codec就行了 sha-224需要额外的jar包
 *      双方需要共享一个密钥: mac: HmacwithMD5 HmacwithSHAXXX
 *      CRCXX也是一种消息摘要
 * 数字签名算法: 不可抵赖性 比如windows的产品密钥就是这样做的: 散列算法With费对称加密算法
 * PBE: Password Based Encryption 基于密码的加密, 和 对称加密很像, 但是密码是可以由自己指定的, 而其他算法的密钥公钥私钥等 是由特定算法生成的, 实际上PBE是建立在DES或AES(对称加密算法)之上的
 *  PBE=明文+密码(用户指定)生成的盐+迭代次数
 *  常见实现=PBEWithMD%AndDES 等...
 * Created by Administrator on 2017/4/15.
 * 非对称加密 AES DES(淘汰) DESede
 * 对称加密: RSA*  算法/工作模式/填充方式
 */
public class MD {
	private static void md(String alt, String text, String expected) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance(alt);
		md.update("admin".getBytes("utf-8"));
		byte[] encrypted = md.digest();
		String str = Hex.encodeHexString(encrypted);
		assertEquals(expected, str);
	}

	@Test
	public void md5() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		md("md5", "admin", "21232f297a57a5a743894a0e4a801fc3");
	}

	@Test
	public void sha1() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		md("sha-1", "admin", "d033e22ae348aeb5660fc2140aec35850c4da997");
	}

	@Test
	public void sha256() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		md("sha-256", "admin", "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918");
	}

	@Test
	public void sha384() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		md("sha-384", "admin", "9ca694a90285c034432c9550421b7b9dbd5c0f4b6673f05f6dbce58052ba20e4248041956ee8c9a2ec9f10290cdc0782");
	}

	@Test
	public void sha512() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		md("sha-512", "admin", "c7ad44cbad762a5da0a452f9e854fdc1e0e7a52a38015f23f3eab1d80b931dd472634dfac71cd34ebc35d16ab7fb8a90c81f975113d6c7538dc69dd8de9077ec");
	}

	@Test
	public void md5Stream() throws NoSuchAlgorithmException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DigestOutputStream dos = new DigestOutputStream(baos, MessageDigest.getInstance("md5"));
		dos.write("admin".getBytes("utf-8"));
//		dos.flush();
//		baos.flush();
		String s = Hex.encodeHexString(dos.getMessageDigest().digest());
		System.out.println(s);
	}

	@Test
	public void d() throws NoSuchAlgorithmException, InvalidKeySpecException {
//		AlgorithmParameters ap = AlgorithmParameters.getInstance("rsa");
//		ap.init();

		//用于生成ap
		//AlgorithmParameterGenerator g = AlgorithmParameterGenerator.getInstance("DES");
		//所有的城生气都有 大小和随机元的概念
		//System.out.println(g);

		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		//kpg.initialize(); 可以进行一些参数的设置 否则就是采用默认设置
		KeyPair kp = kpg.generateKeyPair();
		PrivateKey privateKey = kp.getPrivate();
		PublicKey publicKey = kp.getPublic();
		System.out.println(Hex.encodeHexString(privateKey.getEncoded()));
//		System.out.println(privateKey.getFormat());
//		System.out.println(privateKey.getClass().getName());
		KeyFactory kg = KeyFactory.getInstance("RSA");
		System.out.println(Hex.encodeHexString(kg.generatePrivate(new PKCS8EncodedKeySpec(privateKey.getEncoded())).getEncoded()));

		//System.out.println(Hex.encodeHexString(publicKey.getEncoded()));
	}

	@Test
	public void des() throws Exception {
		//用于生成sk
		KeyGenerator kg = KeyGenerator.getInstance("des");
		//kg.init
		SecretKey skey = kg.generateKey();
		String key = Hex.encodeHexString(skey.getEncoded());
		System.out.println(key);
		Cipher cipher = Cipher.getInstance("des");
		cipher.init(Cipher.ENCRYPT_MODE, skey);
		cipher.update("admin".getBytes("utf-8"));
		System.out.println(Hex.encodeHexString(cipher.doFinal()));

		//KeyGenerator.getInstance("")
//		KeyFactory kf = KeyFactory.getInstance("des");
//		PrivateKey privateKey = kf.generatePrivate(kf.getKeySpec(skey, DESKeySpec.class));
		SecretKeyFactory skf = SecretKeyFactory.getInstance("des");
		KeySpec ks = skf.getKeySpec(skey, DESKeySpec.class);
		SecretKey skey2 = skf.generateSecret(ks);
		assertEquals(skey, skey2);
	}

	@Test
	public void DESede() throws Exception {
		//用于生成sk
		KeyGenerator kg = KeyGenerator.getInstance("DESede");
		//kg.init
		SecretKey skey = kg.generateKey();
		String key = Hex.encodeHexString(skey.getEncoded());
		System.out.println(key);
		//Cipher cipher = Cipher.getInstance("DESede");
		Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, skey);
		cipher.update("admin".getBytes("utf-8"));
		System.out.println(Hex.encodeHexString(cipher.doFinal()));

		//KeyGenerator.getInstance("")
//		KeyFactory kf = KeyFactory.getInstance("des");
//		PrivateKey privateKey = kf.generatePrivate(kf.getKeySpec(skey, DESKeySpec.class));
		SecretKeyFactory skf = SecretKeyFactory.getInstance("DESede");
		KeySpec ks = skf.getKeySpec(skey, DESedeKeySpec.class);
		SecretKey skey2 = skf.generateSecret(ks);
		assertEquals(skey, skey2);
	}

	@Test
	public void des2() throws Exception {
//		08c1d604343d23a2
//		55827973ae7b017c

		//用于创建sk
		SecretKeyFactory skf = SecretKeyFactory.getInstance("des");

		SecretKey skey = skf.generateSecret(new DESKeySpec(Hex.decodeHex("08c1d604343d23a2".toCharArray())));
		Cipher cipher = Cipher.getInstance("des");
		cipher.init(Cipher.ENCRYPT_MODE, skey);
		cipher.update("admin".getBytes("utf-8"));
		String s = Hex.encodeHexString(cipher.doFinal());
		System.out.println(s);
		assertEquals("55827973ae7b017c", s);
	}

	@Test
	public void generateRSAKeyPair() throws Exception {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("rsa");
		KeyPair kp = kpg.generateKeyPair();
		System.out.println(kp.getPrivate().getFormat());
		System.out.println(Base64.encodeBase64String(kp.getPrivate().getEncoded()));
		System.out.println(kp.getPublic().getFormat());
		System.out.println(Base64.encodeBase64String(kp.getPublic().getEncoded()));
	}

	@Test
	public void sign1() throws Exception {
		KeyFactory kf = KeyFactory.getInstance("rsa");
		PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64("MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKupYP6Cu3iO5KWDsjzmocQKW/c2GTwEq8tnWz8eujGJfEsM2u/PmQBwr9kS1ohVOBiV8yKqlKDm1nlX66xkNmZsVoE2Ueu8tcayoV2opyu7Wl92O5KgkJTYSdJZy9AnptwkhcZkUeK3WoDWxj+xDqnjmR1Sw6mQRg18EPcS5JwzAgMBAAECgYArShCSvkcmlOpmLvrwzrx2oqIkinj8oLTuG4dwOIB944z7pCdGGJUd/kzqMgz13n/B+3aQBHJszSnC+gANvkvVSLe6TdeDpiz7uOKl+9mWEBfVJLWao+I/HykbQjggd5cW6CE184XgFkyVXz2uwx+5EzvEdMf4C0qft0dmqcMnIQJBANVPoPo4z9a+im0PSA2PsKmcc6uLlcePdg9j7guUfxUdY5Fi2Xs+iHzgWHapx66n0ceGlbHRCjQ/pAMvvT+ySuMCQQDOA/anuVvgQXbTRACYDAEPowrnAbjTCuTsjVCLEc4kN7006P4H4zNgllLCHUYyeX2mXifkqZ8raF6RoXSLV5pxAkEAoN6Rx8Oqswg1+zPIIjoYgIG2FkyJ49uErOyvLNqL0M++NHn5DGJHqu3CqZM3PbfglHoiUN0Wtfq85Y5OSkhbAwJBAIdzQavbChYy1H5o+n79fYEbcKKbljpjskn5S5h0F3Xmk+cCvWrQLcp2a2knOC0dhchppY8JGxa+I1dasTagXXECQDC93/CxrODi23M74OtZoNBwmagYN5GdXW0lYCSWTO5Ra+2bHgRyC3Yrx63eO/+sn8yKI1yTOr3sY//vxVpUHG4=")));
		PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(Base64.decodeBase64("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCrqWD+grt4juSlg7I85qHEClv3Nhk8BKvLZ1s/HroxiXxLDNrvz5kAcK/ZEtaIVTgYlfMiqpSg5tZ5V+usZDZmbFaBNlHrvLXGsqFdqKcru1pfdjuSoJCU2EnSWcvQJ6bcJIXGZFHit1qA1sY/sQ6p45kdUsOpkEYNfBD3EuScMwIDAQAB")));

		//目前支持 NONEwithDSA SHA1withDSA
		//MD2withRSA MD5withRSA SHA1withRSA SHA256withRSA SHA384withRSA SHA512withRSA
		//签名是需要单向加密算法 和 散列算法结合
		//公钥加密 私钥验证
		Signature s = Signature.getInstance("MD5withRSA");
		s.initSign(privateKey);
		s.update("admin".getBytes("utf-8"));
		byte[] signed = s.sign();
		assertEquals("emAUysEBA/Gpaj6gB/B7XK00xfN7tQ4V8fMuH0YS7++700Tp6U2+dI3tJaycSbuEdl1wVqH2ezDiVc9jGhBfkbmuo+gIfSFZrjVKCUQ8KqBVuLXqD+3zuID/hYcIdkNM/fqWJ22xr14GWr8kZJbMixeSAEZISfDqtrlQccDQgog=", Base64.encodeBase64String(signed));

		Signature sv = Signature.getInstance("MD5withRSA");
		sv.initVerify(publicKey);
		sv.update("admin".getBytes("utf-8"));
		assertTrue(sv.verify(signed));
	}

	@Test
	public void keyStore() throws Exception {
		System.out.println(KeyStore.getDefaultType());
		KeyStore ks = KeyStore.getInstance("jks");
		//ks.store();
	}

	@Test
	public void generateSecretKey() throws Exception {
		KeyGenerator kf = KeyGenerator.getInstance("HmacMD5");
		SecretKey skey = kf.generateKey();
		String b64 = Base64.encodeBase64String(skey.getEncoded());
		System.out.println(b64);
		System.out.println(skey.getFormat());
		System.out.println(skey.getClass().getName());
	}

	@Test
	public void mac() throws Exception {
		//=普通的散列函数+双方都知道的key(相当于是盐?)
		String skey = "JyGC++Rvx0OtfgqGJ8v9YmsayY1JkQZL9bJqiJ8J1ILDzgBbXOIQtHAIVwB7vFtwxdR4J8gMjDIpHchpyhyq+g==";
		//SecretKeySpec 本身也实现了 SecretKey 接口
		SecretKeySpec sks = new SecretKeySpec(Base64.decodeBase64(skey), "RAW");
		Mac mac = Mac.getInstance("HmacMD5");
		mac.init(sks);
		mac.update("admin".getBytes("utf-8"));
		byte[] bytes = mac.doFinal();
		String s = Base64.encodeBase64String(bytes);
		assertEquals("K8mlPMpvOry2ujr0Zd9Ekw==", s);
	}

	@Test
	public void md4() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		//手动注册
//		Security.addProvider(new BouncyCastleProvider());
//MessageDigest.getInstance("md4");

		MD4.Digest digest = new MD4.Digest();
		digest.update("admin".getBytes("utf-8"));
		String s = Hex.encodeHexString(digest.digest());
		System.out.println(s);
		//f9d4049dd6a4dc35d40e5265954b2a46
	}

	@Test
	public void pkcs5s2() throws UnsupportedEncodingException {
		PKCS5S2ParametersGenerator g = new PKCS5S2ParametersGenerator(DigestFactory.createSHA256());
		g.init("密码".getBytes("utf-8"), "盐".getBytes("utf-8"), 1);//迭代次数
		//key长度
		byte[] k = ((KeyParameter) g.generateDerivedParameters(256)).getKey();

	}

	@Test
	public void aes() throws Exception {
		//KeyGenerator kg = KeyGenerator.getInstance("AES");
		//kg.init(); 128 192 256 >128 的需要补丁文件
//		SecretKey key = kg.generateKey();
//		System.out.println(key.getFormat());
//		System.out.println(Base64.encodeBase64String(key.getEncoded()));
		SecretKeySpec key = new SecretKeySpec(Base64.decodeBase64("XWxtHF6rjPm0qxsOFqV0BQ=="), "AES");

		//ECB/PKCS5Padding
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		cipher.update("admin".getBytes("utf-8"));
		cipher.getParameters();

		String s = Base64.encodeBase64String(cipher.doFinal());
		//System.out.println(s);
		assertEquals("ztuQzq/DUfnZyFMumTS9Ng==", s);

		//kg.init();
//		AlgorithmParameterGenerator apg = AlgorithmParameterGenerator.getInstance("AES/CBC/PKCS5Padding");
//		//apg.init();
//		AlgorithmParameters ap = apg.generateParameters();
//		System.out.println(ap.getAlgorithm());
//		System.out.println(ap.getClass().getName());
	}
}
