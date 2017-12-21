package com.xzchaoo.learn.security.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Objects;
import java.util.zip.CRC32;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2016/10/27.
 */
public class KeyPairGeneratorMain {
    private void test对称(String alg, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(alg);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal("你好师姐".getBytes());

        cipher.init(Cipher.DECRYPT_MODE, key);
        String text = new String(cipher.doFinal(encrypted));
        assertEquals("你好师姐", text);
    }

    private void test对称(String alg) throws Exception {
        //RC2 对称加密
        //生成密钥
        KeyGenerator kg = KeyGenerator.getInstance(alg);
        SecretKey secretKey1 = kg.generateKey();

        //key -> byte[]
        byte[] secretKeyBytes1 = secretKey1.getEncoded();

        //byte[] -> key
        //因为 SecretKeySpec 本身继承 SecretKey 接口, 所以不需要 SecretKeyFactory 的帮助
        SecretKeySpec secretKeySpec1 = new SecretKeySpec(secretKeyBytes1, alg);
        SecretKey secretKey2 = secretKeySpec1;
        byte[] secretKeyBytes2 = secretKey2.getEncoded();
        assertTrue(Objects.deepEquals(secretKeyBytes1, secretKeyBytes2));

        test对称(alg, secretKey2);
    }

    @Test
    public void test_对称加密算法() throws Exception {
        test对称("DES");
        test对称("RC2");
        test对称("AES");
    }

    /**
     * rsa签名
     */
    @Test
    public void test_MD5WithRSA() throws Exception {
        //RSA是非对称的所以用 KeyPairGenerator
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        //kpg.initialize(); 可以进行一些参数的设置 否则就是采用默认设置

        //生成密钥对
        KeyPair kp = kpg.generateKeyPair();

        //获得签名算法
        Signature s = Signature.getInstance("MD5WithRSA");
        //TODO 这里传入私钥 公钥行吗?
        s.initSign(kp.getPrivate());

        //要签名的数据
        byte[] data = "你好师姐".getBytes();
        s.update(data);

        //签名后的结果
        byte[] result = s.sign();

        //初始化验证
        s.initVerify(kp.getPublic());
        s.update(data);
        //验证结果
        boolean status = s.verify(result);
        assertTrue(status);
    }

    /**
     * dh算法描述:1. 甲 生成 公钥和秘钥  将 公钥 发给乙
     * 2. 乙根据甲的公钥 (以它作为一个参数) 生成自己的公钥和密钥, 将自己的公钥发给甲
     * 3. 甲根据 自己的私钥和乙的公钥 可以生成出一个 对称加密的密钥
     * 4. 乙根据 自己的私钥和甲的公钥 可以生成出一个 对称加密的密钥
     * 5. 这两个密钥是一样的 (当然了 甲乙要选用相同的对称加密算法)
     * 6. 以后的通信双方就采用这个对称加密算法!
     */
    @Test
    public void test_DHdemo() throws Exception {
        final String ALG = "DH";
        final String ALG2 = "AES";

        //1.
        KeyPairGenerator kpg1 = KeyPairGenerator.getInstance(ALG);
        //kpg1.initialize(1024);//DH算法要求size是 512~1024 的 64的倍数 默认是1024
        KeyPair kp1 = kpg1.generateKeyPair();
        DHPublicKey publicKey1 = (DHPublicKey) kp1.getPublic();
        DHPrivateKey privateKey1 = (DHPrivateKey) kp1.getPrivate();

        byte[] publicKeyBytes1 = publicKey1.getEncoded();

        //2.
        //2.1 接受甲的公钥

        //乙 获得的 甲的密钥
        DHPublicKey publicKey21 = (DHPublicKey) KeyFactory.getInstance(ALG).generatePublic(new X509EncodedKeySpec(publicKeyBytes1));

        KeyPairGenerator kpg2 = KeyPairGenerator.getInstance(ALG);
        kpg2.initialize(publicKey21.getParams());//注意这里!
        KeyPair kp2 = kpg2.generateKeyPair();
        DHPublicKey publicKey2 = (DHPublicKey) kp2.getPublic();
        DHPrivateKey privateKey2 = (DHPrivateKey) kp2.getPrivate();

        //3. 甲生成密钥
        DHPublicKey publicKey12 = (DHPublicKey) KeyFactory.getInstance(ALG).generatePublic(new X509EncodedKeySpec(publicKey2.getEncoded()));
        KeyAgreement ka1 = KeyAgreement.getInstance(ALG);
        ka1.init(privateKey1);
        ka1.doPhase(publicKey12, true);
        SecretKey sk1 = ka1.generateSecret(ALG2);

        //4. 乙生成密钥
        KeyAgreement ka2 = KeyAgreement.getInstance(ALG);
        ka2.init(privateKey2);
        ka2.doPhase(publicKey21, true);
        SecretKey sk2 = ka2.generateSecret(ALG2);

        //5. 从此以后双方采用 ALG2 算法进行交互 密钥是 sk1 和 sk2 (他们是相等的)
        assertEquals(Base64.encodeBase64String(sk1.getEncoded()), Base64.encodeBase64String(sk2.getEncoded()));
    }

    @Test
    public void AESdemo2() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128);//>128 需要增强JCE
        SecretKey sk = kg.generateKey();
        System.out.println(kg.getAlgorithm());
        System.out.println(sk.getEncoded().length);
        test对称("AES", sk);
        test对称("AES/ECB/PKCS5Padding", sk);
        test对称("AES/ECB/PKCS7Padding", sk);//bc才有

        ///ECB/PKCS5Padding
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        //加密后的结果
        cipher.init(Cipher.ENCRYPT_MODE, sk);
        cipher.update("你好师姐22233".getBytes(StandardCharsets.UTF_8));
        byte[] result = cipher.doFinal("干嘛".getBytes());


        //cipher.getOutputSize(3) 貌似是可以获得预期的输出大小 输入参数是要加密的数据的长度

        SecretKeySpec sks = new SecretKeySpec(sk.getEncoded(), kg.getAlgorithm());
        SecretKey sk2 = sks;
        cipher.init(Cipher.DECRYPT_MODE, sk2);
        assertEquals("你好师姐22233干嘛", new String(cipher.doFinal(result), StandardCharsets.UTF_8));
    }

    /**
     * PBE 是一种可以让你指定密码的 加密/解密算法
     *
     * @throws Exception
     */
    @Test
    public void PBEdemo() throws Exception {
        //基于MD5(签名) 和 DES(对称加密)
        //加密和解密的时候必须提供 完全相同的 密码 和 salt
        //通常发送者和接受者都知道密码, salt由发送者产生 随着 数据一起送过去 接受者拿到salt和加密后的数据 再通过密码恢复出来
        //有些场景下甚至连 salt 也可以是固定的

        //http://blog.csdn.net/kongqz/article/details/6290206
        for (String ALG : Arrays.asList(
            "PBEWithMD5AndDES"
        )) {
            final SecureRandom sr = new SecureRandom();
            final int ITERATION_COUNT = 100;
            //动态生成salt salt的长度必须是8
            final byte[] salt = sr.generateSeed(8);

            //这个密码可以自己指定
            final String password = "70862045";
            PBEKeySpec ks = new PBEKeySpec(password.toCharArray());

            SecretKey sk = SecretKeyFactory.getInstance(ALG).generateSecret(ks);
            PBEParameterSpec ps = new PBEParameterSpec(salt, ITERATION_COUNT);
            //这个密钥其实是固定的
            //System.out.println(Base64.encodeBase64String(sk.getEncoded()));

            Cipher c = Cipher.getInstance(ALG, new BouncyCastleProvider());
            c.init(Cipher.ENCRYPT_MODE, sk, ps);
            byte[] encryptedData = c.doFinal("你好师姐".getBytes());
            //System.out.println(Base64.encodeBase64String(encryptedData));

            c.init(Cipher.DECRYPT_MODE, sk, ps);
            byte[] decryptedData = c.doFinal(encryptedData);
            assertEquals("你好师姐", new String(decryptedData));
        }
    }

    @Test
    public void test_CRC32() throws Exception {
        //循环冗余校验算法
        //还有CRCXxx
        //CheckedInputStream
        //CheckedOutputStream
        CRC32 crc32 = new CRC32();
        crc32.update("你好师姐".getBytes());
        System.out.println(crc32.getValue());
    }

    //RSA 也可用于签名
    @Test
    public void DSAdemo() throws Exception {
        //DSA 是一个非对称签名算法
        final String ALG = "DSA";
        byte[] data = "你好师姐".getBytes();

        KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALG);
        kpg.initialize(1024);
        KeyPair kp = kpg.generateKeyPair();

        assertEquals(ALG, kpg.getAlgorithm());
        Signature signature = Signature.getInstance(ALG);
        //用私钥初始化
        signature.initSign(kp.getPrivate());
        //计算签名
        signature.update(data);
        byte[] signResult = signature.sign();

        //用公钥初始化
        signature.initVerify(kp.getPublic());
        //验证签名
        signature.update(data);
        assertTrue(signature.verify(signResult));

        signature.initVerify(kp.getPublic());
        signature.update("fake".getBytes());
        assertFalse(signature.verify(signResult));


//		//对一个对象进行签名
//		SignedObject so = new SignedObject("你好师姐", kp.getPrivate(), signature);
//		//这里的object是深复制
//		//System.out.println(so.getObject());
//
//		//获得签名结果
//		byte[] results = so.getSignature();
//
//		//然后你将这个so对象远程发给别人 别人拿到之后先转成 so 假设转换成功
//		//然后用下面的方法进行验证 看签名是否正确
//		System.out.println(so.verify(kp.getPublic(), signature));
    }

    /**
     * des是对称加密 用  test对称("DES") 也可而已
     *
     * @throws Exception
     */
    @Test
    public void test_des() throws Exception {
        //生成密钥
        KeyGenerator kg = KeyGenerator.getInstance("DES");
        SecretKey secretKey1 = kg.generateKey();
        System.out.println(secretKey1.getClass().getName());
        //key->byte[]
        byte[] secretKeyBytes1 = secretKey1.getEncoded();

        //DES有自己的 DESKeySpec
        //byte[] -> key
        DESKeySpec desKeySpec = new DESKeySpec(secretKeyBytes1);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey2 = skf.generateSecret(desKeySpec);
        System.out.println(secretKey2.getClass().getName());
        assertTrue(Objects.deepEquals(secretKey1, secretKey2));

        test对称("DES", secretKey1);
    }

    @Test
    public void RSAdemo() throws Exception {
        //RSA是非对称加密 所以要用 KeyPairGenerator
        //生成一个密钥对
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        //设置key的长度 如果不设置的话会有一个默认值 具体是多少就不知道了
        kpg.initialize(1024);
        KeyPair kp = kpg.generateKeyPair();

        //将公钥和私钥保存成byte[], 这样方便传输
        //XxxKey -> byte[]
        PublicKey publicKey1 = kp.getPublic();
        PrivateKey privateKey1 = kp.getPrivate();
        System.out.println(publicKey1.getClass().getName());
        System.out.println(privateKey1.getClass().getName());
        byte[] publicKeyBytes1 = publicKey1.getEncoded();
        byte[] privateKeyBytes1 = privateKey1.getEncoded();


        //从byte[] 恢复成 相应的key
        //byte[] -> XxxKey
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes1);//公钥只能用x509
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes1);//私钥只能用pkcs8
        //KeyFactory kf = KeyFactory.getInstance("RSA/ECB/NoPadding");
        KeyFactory kf = KeyFactory.getInstance("RSA/ECB/PKCS1Padding");
        PublicKey publicKey2 = kf.generatePublic(publicKeySpec);
        PrivateKey privateKey2 = kf.generatePrivate(privateKeySpec);

        byte[] publicKeyBytes2 = publicKey2.getEncoded();
        byte[] privateKeyBytes2 = privateKey2.getEncoded();
        System.out.println(Objects.deepEquals(publicKeyBytes1, publicKeyBytes2));
        System.out.println(Objects.deepEquals(privateKeyBytes1, privateKeyBytes2));
    }

    @Test
    public void main4() throws Exception {
        //keystore 称作密钥库 一般类型是jks 其实还有pkcs12 和 jceks 但是因为出口限制只有pkcs12 单pkcs12不是很好用 所以一般都是jks
        //一个ks 可以用于存放 密钥 或 证书
        KeyStore ks = KeyStore.getInstance("JKS");
        //ks.load();
        //ks.store();
        //ks.aliases()
        //ks.getCertificate()
        System.out.println("你好世界");
    }
}

