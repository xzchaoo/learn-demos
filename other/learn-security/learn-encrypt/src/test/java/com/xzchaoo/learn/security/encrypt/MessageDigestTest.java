package com.xzchaoo.learn.security.encrypt;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;

/**
 * 常见的消息摘要算法有 MD5 SHA系列, 这些算法对于给定的输入, 其输出都是一样的, 所以会遭受字典攻击
 * hmac算法的输入参数是 原始数据 + key, 你可以认为key起到一个salt的作用
 */
public class MessageDigestTest {
    @Test
    public void test_sha1() throws Exception {
        //MessageDigest.isEqual(byte[], byte[]) 用于比较两个字节数组
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update("你好世界".getBytes(StandardCharsets.UTF_8));
        String result = Hex.toHexString(md.digest());
        System.out.println(result);
    }

    @Test
    public void test_stream() throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] rawData = "admin".getBytes(StandardCharsets.UTF_8);
        DigestInputStream dis = new DigestInputStream(new ByteArrayInputStream(rawData), md);
        //将dis读完就是结果
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(dis, baos);
        //结果要通过 md.digest() 获得 而不是 baos
        //System.out.println(Hex.toHexString(baos.toByteArray()));
        //21232f297a57a5a743894a0e4a801fc3
        System.out.println(Hex.toHexString(md.digest()));

        md.reset();
        baos = new ByteArrayOutputStream();
        DigestOutputStream dos = new DigestOutputStream(baos, md);
        dos.write(rawData);
        dos.close();
        System.out.println(Hex.toHexString(md.digest()));
        //System.out.println(Hex.toHexString(baos.toByteArray()));
    }

    @Test
    public void test_HMAC() throws Exception {
        //hmac的命名方式 Hmac<底层算法>  比如 HmacMD5 HmacSHA1
        Mac hmac = Mac.getInstance("HmacSHA1");
        SecretKey key = KeyGenerator.getInstance("HmacSHA1").generateKey();
        hmac.init(key);
        hmac.update("admin".getBytes(StandardCharsets.UTF_8));
        System.out.println(Hex.toHexString(hmac.doFinal()));
    }

    @Test
    public void test_基于对称加密的mac() throws Exception {
        //DES是一种对称加密
        Security.addProvider(new BouncyCastleProvider());
        for (String alg : Arrays.asList("DES")) {
            Mac hmac = Mac.getInstance(alg);
            SecretKey key = KeyGenerator.getInstance(alg).generateKey();
            hmac.init(key);
            hmac.update("admin".getBytes(StandardCharsets.UTF_8));
            System.out.println(Hex.toHexString(hmac.doFinal()));
        }
    }
}
