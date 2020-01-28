package com.takim.siem.auth;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.takim.siem.constant.Constant;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenAuthManager {

	private static final Logger logger = LoggerFactory.getLogger(TokenAuthManager.class);

	private static final String SECRET_KEY = "helloworld";

	public String createJWT(String clientIp) {

		// JWT 암호화 알고리즘
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		// JWT를 암호화할 암호키
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		// JWT Claims 설정
		JwtBuilder builder = Jwts.builder().setId(UUID.randomUUID().toString()).setIssuedAt(now)
				.setIssuer(Constant.TOKEN_ISSUER).setExpiration(new Date(nowMillis + 1000 * 60 * 60 * 24 * 365))
				.claim("ip", clientIp).signWith(signatureAlgorithm, signingKey);

		return builder.compact();
	}

	public boolean isPermitted(String token, String requestIP) {

		// Token Claim 추출
		Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
				.parseClaimsJws(token).getBody();

		// 요청한 IP와 Token내 IP가 동일한가? && Token 발행자가 "takimLab"인가?
		if (claims.get("ip").equals(requestIP) && claims.getIssuer().equals(Constant.TOKEN_ISSUER)) {
			logger.info("Token 검증...접근 허용 IP => 요청 IP : " + requestIP);
			return true;
		} else {
			logger.info("Token 검증...접근 미허용 IP => 요청 IP : " + requestIP);
			return false;
		}
	}
}
