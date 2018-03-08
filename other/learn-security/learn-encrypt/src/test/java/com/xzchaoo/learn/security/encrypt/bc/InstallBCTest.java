package com.xzchaoo.learn.security.encrypt.bc;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import java.security.Provider;
import java.security.Security;
import java.util.Iterator;

import javax.crypto.Cipher;

import static org.junit.Assert.*;

/**
 * 这是手动安装法, 另外一种需要修改java的政策文件
 */
public class InstallBCTest {
    @Test
    public void test_install() throws Exception {
        //手动安装
        assertNull(Security.getProvider("BC"));
        //返回值是一个int 表示 provider的顺序 越小优先级越高
        assertTrue(Security.addProvider(new BouncyCastleProvider()) > 0);
        assertNotNull(Security.getProvider("BC"));

        //可以通过下面的方式强制指定使用某个provider提供的算法
        Cipher c = Cipher.getInstance("Blowfish/ECB/NoPadding", "BC");
        assertEquals("BC", c.getProvider().getName());


        //列出provider支持的算法
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
}
