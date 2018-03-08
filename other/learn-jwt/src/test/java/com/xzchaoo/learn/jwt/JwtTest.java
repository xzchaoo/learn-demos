package com.xzchaoo.learn.jwt;

import org.junit.Test;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

/**
 * @author xzchaoo
 * @date 2018/1/1
 */
public class JwtTest {
	@Test
	public void test() {
		Key key = MacProvider.generateKey();
		String compactJws = Jwts.builder()
			.setIssuer("bilibili")
			.setSubject("xzchaoo")
			.setExpiration(new Date(System.currentTimeMillis() + 3600000))
			.claim("a", "b")
			.compressWith(CompressionCodecs.DEFLATE)
			.signWith(SignatureAlgorithm.HS512, key)
			.compact();
		System.out.println(compactJws);
		System.out.println(Base64.getUrlEncoder().encodeToString(key.getEncoded()));
		try {
			Jws<Claims> jws = Jwts.parser().setSigningKey(key).parseClaimsJws(compactJws);
			System.out.println(jws.getSignature());
			System.out.println(jws);
			assert jws.getBody().getSubject().equals("xzchaoo");
		} catch (JwtException e) {

		}
	}
}